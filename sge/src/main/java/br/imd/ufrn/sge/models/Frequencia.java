package br.imd.ufrn.sge.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Frequencia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_frequencia", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_discente_materia", nullable = false)
    @JsonBackReference
    private DiscenteMateria discenteMateria;

    @Column(name = "timestamp")
    private LocalDateTime data = LocalDateTime.now();

    @Column(name = "presenca")
    private boolean presenca;


    public boolean isPresenca() {
        return presenca;
    }

    public void setPresenca(boolean presenca) {
        this.presenca = presenca;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public DiscenteMateria getDiscenteMateria() {
        return discenteMateria;
    }

    public void setDiscenteMateria(DiscenteMateria discenteMateria) {
        this.discenteMateria = discenteMateria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Frequencia)) return false;
        Frequencia that = (Frequencia) o;
        return presenca == that.presenca &&
                Objects.equals(data, that.data) &&
                Objects.equals(discenteMateria, that.discenteMateria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, presenca, discenteMateria);
    }
}