package br.imd.ufrn.sge.repository;

import br.imd.ufrn.sge.models.docente.MatriculaDocente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaDocenteRepository extends JpaRepository<MatriculaDocente, Long> {

    @Query("SELECT m FROM MatriculaDocente m WHERE m.matriculaDocente.dadosPessoais.id = :idPessoa")
    List<MatriculaDocente> findByIdPessoa(Long idPessoa);

    @Query("SELECT m FROM MatriculaDocente m WHERE m.matriculaDocente.id = :idDocente")
    Optional<MatriculaDocente> findByIdDocente(Long idDocente);

    @Query("SELECT m FROM MatriculaDocente m WHERE m.matricula = :numeroMatricula")
    Optional<MatriculaDocente> findByNumeroMatricula(String numeroMatricula);
}
