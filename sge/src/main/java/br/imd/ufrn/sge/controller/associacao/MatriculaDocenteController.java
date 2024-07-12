package br.imd.ufrn.sge.controller.associacao;


import br.imd.ufrn.sge.models.DiscenteMateria;
import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import br.imd.ufrn.sge.models.docente.MatriculaDocente;
import br.imd.ufrn.sge.service.DiscenteMateriaService;
import br.imd.ufrn.sge.service.MatriculaDiscenteService;
import br.imd.ufrn.sge.service.MatriculaDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matricula_docente")
public class MatriculaDocenteController {

    @Autowired
    MatriculaDocenteService matriculaDocenteService;

    @GetMapping("/{id}")
    public ResponseEntity<?> obterMatriculaPorId(@PathVariable Long id) {
        Optional<MatriculaDocente> matricula = matriculaDocenteService.findById(id);

        if (!matricula.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(matricula.get());
    }

    @GetMapping("/docente/{matricula_docente}")
    public ResponseEntity<?> obterMatriculaPorMatriculaDocente(@PathVariable String matricula_docente) {
        Optional<MatriculaDocente> matricula = matriculaDocenteService.encontrarMatriculaDocentePorNumeroMatricula(matricula_docente);

        if (!matricula.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(matricula.get());
    }
}
