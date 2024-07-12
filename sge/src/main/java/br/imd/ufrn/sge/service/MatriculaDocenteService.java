package br.imd.ufrn.sge.service;

import br.imd.ufrn.sge.models.docente.MatriculaDocente;
import br.imd.ufrn.sge.repository.MatriculaDocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MatriculaDocenteService {

    @Autowired
    MatriculaDocenteRepository matriculaDocenteRepository;

    public Optional<MatriculaDocente> encontrarMatriculaPorIdDocente(Long idDocente) {
        return matriculaDocenteRepository.findByIdDocente(idDocente);
    }

    public Optional<MatriculaDocente> findById(Long id) {
        return matriculaDocenteRepository.findById(id);
    }

    public MatriculaDocente salvarMatricula(MatriculaDocente matriculaDocente) {
        return matriculaDocenteRepository.save(matriculaDocente);
    }

    public Optional<MatriculaDocente> encontrarMatriculaDocentePorNumeroMatricula(String numeroMatricula) {
        return matriculaDocenteRepository.findByNumeroMatricula(numeroMatricula);
    }
    public void salvar(MatriculaDocente matDo) {
        matriculaDocenteRepository.save(matDo);
    }
}
