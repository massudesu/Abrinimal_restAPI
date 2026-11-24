package org.dt2.abrinimal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Voluntarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;
    @Email
    private String email;
    @Size(min = 11, max = 11)
    private String telefone;
    private String funcao;

    @OneToMany(mappedBy = "voluntario")
    @JsonIgnore
    private List<Adocoes> adocoesRegistradas;

    public Voluntarios(Long id, String nome, String email, String telefone, String funcao, List<Adocoes> adocoesRegistradas) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.funcao = funcao;
        this.adocoesRegistradas = adocoesRegistradas;
    }

    public Voluntarios(String nome, String email, String telefone, String funcao, List<Adocoes> adocoesRegistradas) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.funcao = funcao;
        this.adocoesRegistradas = adocoesRegistradas;
    }
    public Voluntarios(){}

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public List<Adocoes> getAdocoesRegistradas() {
        return adocoesRegistradas;
    }

    public void setAdocoesRegistradas(List<Adocoes> adocoesRegistradas) {
        this.adocoesRegistradas = adocoesRegistradas;
    }
}


