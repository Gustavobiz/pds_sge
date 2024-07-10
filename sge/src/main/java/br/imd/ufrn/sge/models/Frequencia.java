package br.imd.ufrn.sge.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Frequencia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_frequencia", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private DiscenteMateria discenteMateria;

    @Column(name = "timestamp")
    @JsonProperty("timestamp")
    private Long timestamp;

    @Column(name = "presenca")
    private boolean presenca;


    public boolean isPresenca() {
        return presenca;
    }

    public void setPresenca(boolean presenca) {
        this.presenca = presenca;
    }

    public Long getId() {
        return id;
    }

    public Long getTimeStamp() {
        return this.timestamp;
    }

    public void setDiscenteMateria(DiscenteMateria discenteMateria) {
        this.discenteMateria = discenteMateria;
    }
}