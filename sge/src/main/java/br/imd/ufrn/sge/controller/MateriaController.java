package br.imd.ufrn.sge.controller;

import br.imd.ufrn.sge.models.materia.Materia;
import br.imd.ufrn.sge.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/materias", produces="application/json")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @GetMapping
    public List<Materia> listarMaterias() {
        return materiaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterMateriaPorId(@PathVariable Long id) {
        Optional<Materia> materia= materiaService.encontrarPorId(id);

        if (materia.isPresent()){
            return ResponseEntity.ok().body(materia.get());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Materia com o ID " + id + " não encontrada");
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<Materia> criarMateria(@RequestBody Materia materia) {
        Materia novaMateria = materiaService.salvar(materia);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMateria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMateria(@PathVariable Long id) {
        Optional<Materia> materiaExistente = materiaService.encontrarPorId(id);
        if (materiaExistente.isPresent()) {
            materiaService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Materia> atualizarMateria(@PathVariable Long id, @RequestBody Materia materia) {
        Optional<Materia> materiaExistente = materiaService.encontrarPorId(id);
        if (materiaExistente.isPresent()) {
            Materia dadosMateria = materiaExistente.get();
            dadosMateria.setNome(materia.getNome());
            dadosMateria.setDescricao(materia.getDescricao());
            Materia materiaAtualizada = materiaService.salvar(dadosMateria);
            return ResponseEntity.ok().body(materiaAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
