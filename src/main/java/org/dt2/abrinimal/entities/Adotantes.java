package org.dt2.abrinimal.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.dt2.abrinimal.enums.MoradiaEnum;

import java.util.List;

@Entity
public class Adotantes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String telefone;//Nicolas Massuda<@massudesu>
    private String endereco;
    private MoradiaEnum tipoMoradia;

    @OneToMany(mappedBy = "adotante")
    @JsonIgnore
    private List<Adocoes> adocoes;

    public Adotantes(Long id, String nome, String cpf, String telefone, String endereco, MoradiaEnum tipoMoradia, List<Adocoes> adocoes) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
        this.tipoMoradia = tipoMoradia;
        this.adocoes = adocoes;
    }

    public Adotantes(String nome, String cpf, String telefone, String endereco, MoradiaEnum tipoMoradia, List<Adocoes> adocoes) {
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
    }//Nicolas Massuda<@massudesu>

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public MoradiaEnum getTipoMoradia() {
        return tipoMoradia;
    }

    public void setTipoMoradia(MoradiaEnum tipoMoradia) {
        this.tipoMoradia = tipoMoradia;
    }

    public List<Adocoes> getAdocoes() {
        return adocoes;
    }
    //Nicolas Massuda<@massudesu>
    public void setAdocoes(List<Adocoes> adocoes) {
        this.adocoes = adocoes;
    }
}