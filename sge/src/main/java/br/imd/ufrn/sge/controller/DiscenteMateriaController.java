package br.imd.ufrn.sge.controller;

import br.imd.ufrn.sge.models.DiscenteMateria;
import br.imd.ufrn.sge.models.Frequencia;
import br.imd.ufrn.sge.framework.aprovacao_materia.*;
import br.imd.ufrn.sge.service.DiscenteMateriaService;
import br.imd.ufrn.sge.service.FrequenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/discente-materia", produces="application/json")
public class DiscenteMateriaController {

        @Autowired
        private DiscenteMateriaService disMatService;

        @Autowired
        private FrequenciaService frequenciaService;

        @Autowired
        private AprovacaoSimples aprovacaoSimples;



    @GetMapping("/")
        public List<DiscenteMateria> listarNotas() {
            return disMatService.listarTodos();
        }


    @GetMapping("/{id}")
    public ResponseEntity<?> obterDiscenteMateriaPorId(@PathVariable Long id) {
        Optional<DiscenteMateria> nota = disMatService.encontrarPorId(id);

        if (nota.isPresent()){
            return ResponseEntity.ok().body(nota.get());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro de Notas com o ID " + id + " não encontrado");
        }
    }
    @GetMapping("/discente/{matricula_discente}")
    public ResponseEntity<?> obterMateriaPorMatriculaDiscente(@PathVariable Long matricula_discente) {
        List<DiscenteMateria> notasEncontradas = disMatService.encontrarPorMatriculaDiscente(matricula_discente);

        if (!notasEncontradas.isEmpty()) {
            return ResponseEntity.ok().body(notasEncontradas);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno com a matrícula " + matricula_discente + " não encontrado");
        }
    }

    @GetMapping("/materia/{id}")
    public ResponseEntity<?> obterDiscentePorIdMateria(@PathVariable Long id) {
        List<DiscenteMateria> notasEncontradas = disMatService.encontrarPorIdMateria(id);

        if (!notasEncontradas.isEmpty()) {
            return ResponseEntity.ok().body(notasEncontradas);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Matéria com o ID " + id + " não encontrada");
        }
    }

    @PutMapping(value="/notas/{id}")
    public ResponseEntity<DiscenteMateria> atualizarNotas(
            @PathVariable Long id,
            @RequestBody DiscenteMateria disMat
    ){
            Optional<DiscenteMateria> disMatExistente = disMatService.encontrarPorId(id);
        if (disMatExistente.isPresent()) {
            DiscenteMateria discenteMateria = disMatExistente.get();
            if(disMat.getUnidade1() != null)
                discenteMateria.setUnidade1(disMat.getUnidade1());
            if(disMat.getUnidade2() != null)
                discenteMateria.setUnidade2(disMat.getUnidade2());
            if(disMat.getUnidade3() != null)
                discenteMateria.setUnidade3(disMat.getUnidade3());
            if(disMat.getProvaFinal() != null) {
                discenteMateria.setProvaFinal(disMat.getProvaFinal());
                // Subtituir AProvaçãoSimples por estratégia de aprovação de sua escolha
                aprovacaoSimples.aprovaAluno(discenteMateria, discenteMateria.getFrequencias());
            }
            DiscenteMateria notaAtualizada = disMatService.salvar(discenteMateria);
            return ResponseEntity.ok().body(notaAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/frequencia/{id}")
    public ResponseEntity<DiscenteMateria> atualizarFrequencia(@PathVariable Long id, @RequestBody List<Frequencia> frequencias) {
        Optional<DiscenteMateria> disMatExistente = disMatService.encontrarPorId(id);
        if (disMatExistente.isPresent()) {
            DiscenteMateria discenteMateria = disMatExistente.get();

            // Save the new Frequencia records
            List<Frequencia> savedFrequencias = frequenciaService.salvar(frequencias);

            // Add the new Frequencias to the DiscenteMateria
            for (Frequencia frequencia : savedFrequencias) {
                discenteMateria.addFrequencia(frequencia);
            }

            DiscenteMateria disMatAtualizada = disMatService.salvar(discenteMateria);
            return ResponseEntity.ok().body(disMatAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/calcular-nota/{id}/{tipo}")
    public ResponseEntity<?> calcularNota(@PathVariable Long id, @PathVariable String tipo) {
        Optional<DiscenteMateria> disMatExistente = disMatService.encontrarPorId(id);
        if (disMatExistente.isPresent()) {
            DiscenteMateria discenteMateria = disMatExistente.get();
            try {
                float notaCalculada = disMatService.calcularNota(discenteMateria.getUnidade1(),discenteMateria.getUnidade2(),discenteMateria.getUnidade3(), tipo);
                return ResponseEntity.ok().body(notaCalculada);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
