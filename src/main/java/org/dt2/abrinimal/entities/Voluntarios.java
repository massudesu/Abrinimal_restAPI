package org.dt2.abrinimal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Voluntarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    private String funcao;//Nicolas Massuda<@massudesu>

    @OneToMany(mappedBy = "voluntario")
    @JsonIgnore
    private List<Adocoes> adocoesRegistradas;

    public Voluntarios(Long id, String nome, String email, String telefone, String funcao, List<Adocoes> adocoesRegistradas) {
        this.id = id;
        this.nome = nome;
        this.email = email;//Nicolas Massuda<@massudesu>
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
    //Nicolas Massuda<@massudesu>
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
    //Nicolas Massuda<@massudesu>
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


