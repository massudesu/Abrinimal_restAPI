package org.dt2.abrinimal.controllers;

import org.dt2.abrinimal.entities.Adocoes;
import org.dt2.abrinimal.repositories.AdocoesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/adocoes")
public class AdocoesController {//Nicolas Massuda<@massudesu>
    private final AdocoesRepository repository;

    public AdocoesController(AdocoesRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Adocoes> mostrar(){
        return repository.findAll();
    }

    @PostMapping
    public String criar(@RequestBody Adocoes ado){//Nicolas Massuda<@massudesu>
        try {
            repository.save(ado);
            return "Salvo com sucesso";
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados incorretos", e);
        }//Nicolas Massuda<@massudesu>
    }

    @PutMapping("/{id}")
    public String atualizar(@PathVariable Long id, @RequestBody Adocoes dados){
        Adocoes ado = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado."));
        ado.setDataAdocao(dados.getDataAdocao());
        ado.setTermoAssinado(dados.getTermoAssinado());
        ado.setObservacoes(dados.getObservacoes());
        ado.setAnimal(dados.getAnimal());
        ado.setAdotante(dados.getAdotante());
        ado.setVoluntario(dados.getVoluntario());//Nicolas Massuda<@massudesu>
        try {
            repository.save(ado);
            return "Atualizado com sucesso";
        }//Nicolas Massuda<@massudesu>
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados incorretos", e);
        }
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        try {
            if (!repository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado.");
            }
            repository.deleteById(id);
            repository.flush();
        } catch (ResponseStatusException e) {
            throw e;//Nicolas Massuda<@massudesu>
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir este registro porque ele está vinculado a outros registros. Remova primeiro os vínculos relacionados.", e);
        }
    }
}
