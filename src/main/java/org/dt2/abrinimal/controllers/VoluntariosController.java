package org.dt2.abrinimal.controllers;

import org.dt2.abrinimal.entities.Voluntarios;
import org.dt2.abrinimal.repositories.VoluntariosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/voluntarios")
public class VoluntariosController {
    private final VoluntariosRepository repository;
    //Nicolas Massuda<@massudesu>
    public VoluntariosController(VoluntariosRepository repository){
        this.repository = repository;
    }

    @GetMapping                                                                                                                                                                                                                                                                                                                             //Nicolas Massuda<@massudesu>
    public List<Voluntarios> mostrar(){
        return repository.findAll();
    }

    @PostMapping
    public String criar(@RequestBody Voluntarios voluntario){
        try {
            repository.save(voluntario);
            return "Salvo com sucesso";
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados incorretos", e);
        }
    }
    //Nicolas Massuda<@massudesu>
    @PutMapping("/{id}")
    public String atualizar(@PathVariable Long id, @RequestBody Voluntarios dados){
        Voluntarios voluntario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado."));
        voluntario.setNome(dados.getNome());
        voluntario.setEmail(dados.getEmail());
        voluntario.setTelefone(dados.getTelefone());
        voluntario.setFuncao(dados.getFuncao());
        try {
            repository.save(voluntario);
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
            }//Nicolas Massuda<@massudesu>
            repository.deleteById(id);
            repository.flush();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir este registro porque ele está vinculado a outros registros. Remova primeiro os vínculos relacionados.", e);
        }
    }//Nicolas Massuda<@massudesu>
}
