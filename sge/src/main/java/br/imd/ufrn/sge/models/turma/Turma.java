package br.imd.ufrn.sge.models.turma;

import br.imd.ufrn.sge.models.discente.Discente;
import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import br.imd.ufrn.sge.models.docente.Docente;
import br.imd.ufrn.sge.models.docente.MatriculaDocente;
import br.imd.ufrn.sge.models.materia.Materia;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_turma", nullable = false)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @ManyToMany(mappedBy = "turmas")
    private Set<MatriculaDocente> docentes;


    /**
     * Definindo junção de turmas com materias
     * */

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Materia> materias;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<MatriculaDocente> getDocentes() {
        return docentes;
    }

    public void setDocentes(Set<MatriculaDocente> docentes) {
        this.docentes = docentes;
    }

    public Set<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(Set<Materia> materias) {
        this.materias = materias;
    }
}