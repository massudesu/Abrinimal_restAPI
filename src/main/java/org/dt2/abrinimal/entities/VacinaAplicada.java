package org.dt2.abrinimal.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "vacinas_aplicadas")
public class VacinaAplicada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nomeVacina;
    private LocalDate dataAplicacao;
    private LocalDate proximaDose;

    @ManyToOne
    @JoinColumn(name = "id_animal", nullable = false)
    private Animais animal;

    public VacinaAplicada(Long id, String nomeVacina, LocalDate dataAplicacao, LocalDate proximaDose, Animais animal) {
        this.id = id;
        this.nomeVacina = nomeVacina;
        this.dataAplicacao = dataAplicacao;
        this.proximaDose = proximaDose;
        this.animal = animal;
    }

    public VacinaAplicada(String nomeVacina, LocalDate dataAplicacao, LocalDate proximaDose, Animais animal) {
        this.nomeVacina = nomeVacina;
        this.dataAplicacao = dataAplicacao;
        this.proximaDose = proximaDose;
        this.animal = animal;
    }
    public VacinaAplicada(){}

    public Long getId() {
        return id;
    }

    public String getNomeVacina() {
        return nomeVacina;
    }

    public void setNomeVacina(String nomeVacina) {
        this.nomeVacina = nomeVacina;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public LocalDate getProximaDose() {
        return proximaDose;
    }

    public void setProximaDose(LocalDate proximaDose) {
        this.proximaDose = proximaDose;
    }

    public Animais getAnimal() {
        return animal;
    }

    public void setAnimal(Animais animal) {
        this.animal = animal;
    }
}