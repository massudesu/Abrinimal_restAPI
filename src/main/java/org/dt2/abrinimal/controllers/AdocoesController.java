package org.dt2.abrinimal.controllers;

import org.dt2.abrinimal.entities.Adocoes;
import org.dt2.abrinimal.entities.Animais;
import org.dt2.abrinimal.repositories.AdocoesRepository;
import org.dt2.abrinimal.repositories.AnimaisRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/adocoes")
public class AdocoesController {
    private static final String ADOTADO = "Adotado";
    private static final String DISPONIVEL = "Disponível";

    private final AdocoesRepository repository;
    private final AnimaisRepository animaisRepository;

    public AdocoesController(AdocoesRepository repository, AnimaisRepository animaisRepository){
        this.repository = repository;
        this.animaisRepository = animaisRepository;
    }

    @GetMapping
    public List<Adocoes> mostrar(){
        return repository.findAll();
    }

    @PostMapping
    @Transactional
    public String criar(@RequestBody Adocoes ado){
        try {
            Animais animal = buscarAnimal(ado.getAnimal());
            ado.setAnimal(animal);
            repository.save(ado);

            animal.setStatusAdocao(ADOTADO);
            animaisRepository.save(animal);
            return "Salvo com sucesso";
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados incorretos", e);
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public String atualizar(@PathVariable Long id, @RequestBody Adocoes dados){
        Adocoes ado = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado."));
        try {
            Animais antigo = ado.getAnimal();
            Animais novo = buscarAnimal(dados.getAnimal());

            ado.setDataAdocao(dados.getDataAdocao());
            ado.setTermoAssinado(dados.getTermoAssinado());
            ado.setObservacoes(dados.getObservacoes());
            ado.setAnimal(novo);
            ado.setAdotante(dados.getAdotante());
            ado.setVoluntario(dados.getVoluntario());
            repository.save(ado);

            // se trocou o animal da adoção, o anterior volta a ficar disponível
            if (antigo != null && !antigo.getId().equals(novo.getId())) {
                liberarAnimal(antigo);
            }
            novo.setStatusAdocao(ADOTADO);
            animaisRepository.save(novo);
            return "Atualizado com sucesso";
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados incorretos", e);
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletar(@PathVariable Long id) {
        Adocoes ado = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado."));
        try {
            Animais animal = ado.getAnimal();
            repository.delete(ado);
            repository.flush();
            liberarAnimal(animal);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir este registro porque ele está vinculado a outros registros. Remova primeiro os vínculos relacionados.", e);
        }
    }

    // carrega o animal do banco a partir do id enviado no JSON ({"animal": {"id": 1}})
    private Animais buscarAnimal(Animais referencia) {
        if (referencia == null || referencia.getId() == null) {
            throw new IllegalArgumentException("Animal não informado");
        }
        return animaisRepository.findById(referencia.getId())
                .orElseThrow(() -> new IllegalArgumentException("Animal não encontrado"));
    }

    // ao excluir/trocar a adoção, o animal só volta a "Disponível" se estava "Adotado"
    // (um "Indisponível" definido manualmente é mantido)
    private void liberarAnimal(Animais animal) {
        if (animal != null && ADOTADO.equalsIgnoreCase(animal.getStatusAdocao())) {
            animal.setStatusAdocao(DISPONIVEL);
            animaisRepository.save(animal);
        }
    }
}
