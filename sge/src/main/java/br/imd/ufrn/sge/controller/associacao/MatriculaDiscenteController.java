package br.imd.ufrn.sge.controller.associacao;


import br.imd.ufrn.sge.models.DiscenteMateria;
import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import br.imd.ufrn.sge.service.DiscenteMateriaService;
import br.imd.ufrn.sge.service.MatriculaDiscenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matricula")
public class MatriculaDiscenteController {

    @Autowired
    MatriculaDiscenteService matriculaDiscenteService;

    @Autowired
    DiscenteMateriaService discenteMateriaService;

    @GetMapping("/{id}")
    public ResponseEntity<?> obterMatriculaPorId(@PathVariable Long id) {
        Optional<MatriculaDiscente> matricula = matriculaDiscenteService.findById(id);

        if (!matricula.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(matricula.get());
    }

    @GetMapping("/discente/{matricula_discente}")
    public ResponseEntity<?> obterMatriculaPorMatriculaDiscente(@PathVariable String matricula_discente) {
        Optional<MatriculaDiscente> matricula = matriculaDiscenteService.encontrarMatriculaDiscentePorNumeroMatricula(matricula_discente);

        if (!matricula.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(matricula.get());
    }

    @PutMapping("/discente/aprovacao/{matricula_discente}")
    public ResponseEntity<?> atualizarAprovacaoAnual(@PathVariable String matricula_discente) {
        Optional<MatriculaDiscente> matriculaDiscente = matriculaDiscenteService.encontrarMatriculaDiscentePorNumeroMatricula(matricula_discente);
        if (!matriculaDiscente.isPresent()) {
            List<DiscenteMateria> discenteMaterias = discenteMateriaService.encontrarPorMatriculaDiscente(matricula_discente);

            List<MatriculaDiscente.Status> materiaStatus = discenteMaterias.stream()
                    .map(DiscenteMateria::getStatus)
                    .map(status -> MatriculaDiscente.Status.valueOf(status.name()))
                    .collect(Collectors.toList());

            MatriculaDiscente newMatriculaDiscente = matriculaDiscenteService.atualizarStatusAprovacao(matriculaDiscente.get(),materiaStatus);
            return ResponseEntity.ok().body(newMatriculaDiscente);
        }
        return ResponseEntity.notFound().build();
    }
}
