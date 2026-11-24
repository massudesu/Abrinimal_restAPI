package org.dt2.abrinimal.controllers;

import org.dt2.abrinimal.entities.Animais;
import org.dt2.abrinimal.repositories.AnimaisRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/animais")
public class AnimaisController {
    private final AnimaisRepository repository;

    public AnimaisController(AnimaisRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Animais> mostrar(){
        return repository.findAll();
    }

    @PostMapping
    public String criar(@RequestBody Animais animal){
        try {
            repository.save(animal);
            return "Salvo com sucesso";
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados incorretos", e);
        }
    }

    @PutMapping("/{id}")
    public String atualizar(@PathVariable Long id, @RequestBody Animais dados){
        Animais animal = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado."));
        animal.setNome(dados.getNome());
        animal.setEspecie(dados.getEspecie());
        animal.setPorte(dados.getPorte());
        animal.setIdadeEstimada(dados.getIdadeEstimada());
        animal.setStatusAdocao(dados.getStatusAdocao());
        animal.setDataResgate(dados.getDataResgate());
        try {
            repository.save(animal);
            return "Atualizado com sucesso";
        }
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
    }
}
