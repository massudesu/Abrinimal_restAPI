package org.dt2.abrinimal.controllers;

import org.dt2.abrinimal.entities.Adotantes;
import org.dt2.abrinimal.repositories.AdotantesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/adotantes")
public class AdotantesController {
    private final AdotantesRepository repository;
    //Nicolas Massuda<@massudesu>
    public AdotantesController(AdotantesRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Adotantes> mostrar(){
        return repository.findAll();
    }

    @PostMapping
    public String criar(@RequestBody Adotantes adotante){
        try {
            repository.save(adotante);
            return "Salvo com sucesso";
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados incorretos", e);
        }
    }

    @PutMapping("/{id}")
    public String atualizar(@PathVariable Long id, @RequestBody Adotantes dados){
        Adotantes adotante = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado."));
        adotante.setNome(dados.getNome());
        adotante.setCpf(dados.getCpf());
        adotante.setTelefone(dados.getTelefone());
        adotante.setEndereco(dados.getEndereco());
        adotante.setTipoMoradia(dados.getTipoMoradia());
        try {
            repository.save(adotante);
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
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir este registro porque ele está vinculado a outros registros. Remova primeiro os vínculos relacionados.", e);
        }
    }//@massudesu
}
