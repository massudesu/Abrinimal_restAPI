package org.dt2.abrinimal.controllers;

import org.dt2.abrinimal.entities.VacinaAplicada;//Nicolas Massuda<@massudesu>
import org.dt2.abrinimal.repositories.VacinaAplicadaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/vacinaaplicada")
public class VacinaAplicadaController {
    private final VacinaAplicadaRepository repository;

    public VacinaAplicadaController(VacinaAplicadaRepository repository){
        this.repository = repository;
    }

    @GetMapping//Nicolas Massuda<@massudesu>
    public List<VacinaAplicada> mostrar(){
        return repository.findAll();
    }
    //Nicolas Massuda<@massudesu>
    @PostMapping
    public String criar(@RequestBody VacinaAplicada vacina){
        try {
            repository.save(vacina);
            return "Salvo com sucesso";
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados incorretos", e);
        }
    }

    @PutMapping("/{id}")
    public String atualizar(@PathVariable Long id, @RequestBody VacinaAplicada dados){
        VacinaAplicada vacina = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado."));
        vacina.setNomeVacina(dados.getNomeVacina());
        vacina.setDataAplicacao(dados.getDataAplicacao());
        vacina.setProximaDose(dados.getProximaDose());
        vacina.setAnimal(dados.getAnimal());
        try {
            repository.save(vacina);
            return "Atualizado com sucesso";
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados incorretos", e);
        }
    }

    @DeleteMapping("/{id}")//Nicolas Massuda<@massudesu>
    public void deletar(@PathVariable Long id) {
        try {
            if (!repository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado.");
            }
            repository.deleteById(id);
            repository.flush();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {//Nicolas Massuda<@massudesu>                                                                                                                                                                                                                                                        //Nicolas Massuda<@massudesu>
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir este registro porque ele está vinculado a outros registros. Remova primeiro os vínculos relacionados.", e);
        }
    }
}
