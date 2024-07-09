package br.imd.ufrn.sge.models;
import br.imd.ufrn.sge.models.discente.Discente;
import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import br.imd.ufrn.sge.models.materia.Materia;
import br.imd.ufrn.sge.models.turma.Turma;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DiscenteMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_discente_materia", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "matricula_discente", nullable = false)
    //@JsonManagedReference("discenteMateriaMatricula")
    private MatriculaDiscente matricula_discente;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia materia;

    @Column(name = "unidade_1")
    private Float unidade1;

    @Column(name = "unidade_2")
    private Float unidade2;

    @Column(name = "unidade_3")
    private Float unidade3;

    @Column(name = "prova_final")
    private Float provaFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MatriculaDiscente.Status status = MatriculaDiscente.Status.MATRICULADO;

    public enum Status {
        MATRICULADO,
        APROVADO,
        REPROVADO
    }

    @OneToMany(mappedBy = "discenteMateria", cascade = CascadeType.ALL)
    private List<Frequencia> frequencias = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getUnidade1() {
        return unidade1;
    }

    public void setUnidade1(Float unidade1) {
        this.unidade1 = unidade1;
    }

    public Float getUnidade2() {
        return unidade2;
    }

    public void setUnidade2(Float unidade2) {
        this.unidade2 = unidade2;
    }

    public Float getUnidade3() {
        return unidade3;
    }

    public void setUnidade3(Float unidade3) {
        this.unidade3 = unidade3;
    }

    public Float getProvaFinal() {
        return provaFinal;
    }

    public void setProvaFinal(Float provaFinal) {
        this.provaFinal = provaFinal;
    }

    public MatriculaDiscente getMatricula_discente() {
        return matricula_discente;
    }

    public void setMatricula_discente(MatriculaDiscente matricula_discente) {
        this.matricula_discente = matricula_discente;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public List<Frequencia> getFrequencias() {
        return frequencias;
    }

    public void addFrequencia(Frequencia frequencia) {
        this.frequencias.add(frequencia);
    }

    public void setFrequencias(List<Frequencia> frequencias) {
        this.frequencias = frequencias;
    }

    public MatriculaDiscente.Status getStatus() {
        return status;
    }

    public void setStatus(MatriculaDiscente.Status status) {
        this.status = status;
    }
}