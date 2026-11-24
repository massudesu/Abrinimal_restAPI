package org.dt2.abrinimal.entities;
//Nicolas Massuda<@massudesu>

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.dt2.abrinimal.enums.PorteEnum;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Animais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String especie;
    private PorteEnum porte;
    private Integer idadeEstimada;
    private String  statusAdocao;
    private LocalDate dataResgate;
//Nicolas Massuda<@massudesu>

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<VacinaAplicada> vacinas;

    public Animais(Long id, String nome, String especie, PorteEnum porte, Integer idadeEstimada,String  statusAdocao, LocalDate dataResgate, List<VacinaAplicada> vacinas) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.porte = porte;
        this.idadeEstimada = idadeEstimada;
        this.statusAdocao = statusAdocao;
        this.dataResgate = dataResgate;
        this.vacinas = vacinas;
    }

    public Animais(String nome, String especie, PorteEnum porte, Integer idadeEstimada, String  statusAdocao, LocalDate dataResgate, List<VacinaAplicada> vacinas) {
        this.nome = nome;
        this.especie = especie;
        this.porte = porte;
        this.idadeEstimada = idadeEstimada;
        this.statusAdocao = statusAdocao;
        this.dataResgate = dataResgate;
        this.vacinas = vacinas;
    }
    public Animais(){}

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public PorteEnum getPorte() {
        return porte;
    }

    public void setPorte(PorteEnum porte) {
        this.porte = porte;
    }

    public Integer getIdadeEstimada() {
        return idadeEstimada;
    }//Nicolas Massuda<@massudesu>

    public void setIdadeEstimada(Integer idadeEstimada) {
        this.idadeEstimada = idadeEstimada;
    }

    public String  getStatusAdocao() {
        return statusAdocao;
    }

    public void setStatusAdocao(String  statusAdocao) {
        this.statusAdocao = statusAdocao;
    }

    public LocalDate getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(LocalDate dataResgate) {
        this.dataResgate = dataResgate;
    }

    public List<VacinaAplicada> getVacinas() {
        return vacinas;
    }

    public void setVacinas(List<VacinaAplicada> vacinas) {
        this.vacinas = vacinas;
    }
}
