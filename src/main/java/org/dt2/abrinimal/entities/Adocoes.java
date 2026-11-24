package org.dt2.abrinimal.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Adocoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataAdocao;
    private Boolean termoAssinado;
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "id_animal", nullable = false)
    private Animais animal;

    @ManyToOne
    @JoinColumn(name = "id_adotante", nullable = false)
    private Adotantes adotante;

    @ManyToOne
    @JoinColumn(name = "id_voluntario", nullable = false)
    private Voluntarios voluntario;

    public Adocoes(Long id, LocalDate dataAdocao, Boolean termoAssinado, String observacoes, Animais animal, Adotantes adotante, Voluntarios voluntario) {
        //Nicolas Massuda<@massudesu>
        this.id = id;
        this.dataAdocao = dataAdocao;
        this.termoAssinado = termoAssinado;
        this.observacoes = observacoes;
        this.animal = animal;
        this.adotante = adotante;
        this.voluntario = voluntario;
    }

    public Adocoes(LocalDate dataAdocao, Boolean termoAssinado, String observacoes, Animais animal, Adotantes adotante, Voluntarios voluntario) {
        this.dataAdocao = dataAdocao;
        this.termoAssinado = termoAssinado;
        this.observacoes = observacoes;
        this.animal = animal;
        this.adotante = adotante;
        this.voluntario = voluntario;
    }
    public Adocoes(){}

    public Long getId() {
        return id;//Nicolas Massuda<@massudesu>
    }

    public LocalDate getDataAdocao() {
        return dataAdocao;
    }

    public void setDataAdocao(LocalDate dataAdocao) {
        this.dataAdocao = dataAdocao;
    }

    public Boolean getTermoAssinado() {
        return termoAssinado;
    }

    public void setTermoAssinado(Boolean termoAssinado) {
        this.termoAssinado = termoAssinado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Animais getAnimal() {
        return animal;
    }

    public void setAnimal(Animais animal) {
        this.animal = animal;
    }

    public Adotantes getAdotante() {
        return adotante;
    }

    public void setAdotante(Adotantes adotante) {
        this.adotante = adotante;
    }

    public Voluntarios getVoluntario() {
        return voluntario;
    }//Nicolas Massuda<@massudesu>

    public void setVoluntario(Voluntarios voluntario) {
        this.voluntario = voluntario;
    }
}