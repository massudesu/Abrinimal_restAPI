package org.dt2.abrinimal.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.Length;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Entity
public class Adotantes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @CPF@UniqueElements
    private String cpf;
    @Size(min = 11, max = 11)
    private String telefone;
    private String endereco;
    private String tipoMoradia;

    @OneToMany(mappedBy = "adotante")
    @JsonIgnore
    private List<Adocoes> adocoes;

    public Adotantes(Long id, String nome, String cpf, String telefone, String endereco, String tipoMoradia, List<Adocoes> adocoes) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
        this.tipoMoradia = tipoMoradia;
        this.adocoes = adocoes;
    }

    public Adotantes(String nome, String cpf, String telefone, String endereco, String tipoMoradia, List<Adocoes> adocoes) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
        this.tipoMoradia = tipoMoradia;
        this.adocoes = adocoes;
    }
    public Adotantes(){}

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipoMoradia() {
        return tipoMoradia;
    }

    public void setTipoMoradia(String tipoMoradia) {
        this.tipoMoradia = tipoMoradia;
    }

    public List<Adocoes> getAdocoes() {
        return adocoes;
    }

    public void setAdocoes(List<Adocoes> adocoes) {
        this.adocoes = adocoes;
    }
}