package br.imd.ufrn.sge.repository;

import br.imd.ufrn.sge.models.discente.ObservacaoDiscente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ObservacaoDiscenteRepository extends JpaRepository<ObservacaoDiscente, Long> {

    @Query("SELECT o FROM ObservacaoDiscente o WHERE o.matriculaDiscente.id = :idMatriculaDiscente")
    List<ObservacaoDiscente> findByMatriculaDiscenteId(Long idMatriculaDiscente);

}
