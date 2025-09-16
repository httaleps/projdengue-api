package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Agente;
import com.talessousa.todosimple.services.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/agente")
public class AgenteController {

    @Autowired
    private AgenteService agenteService;

    @PostMapping
    public ResponseEntity<Agente> create(@RequestBody Agente agente) {
        Agente savedAgente = agenteService.save(agente);
        return new ResponseEntity<>(savedAgente, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agente> findById(@PathVariable Long id) {
        return agenteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Agente>> findAll() {
        return ResponseEntity.ok(agenteService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Agente agente) {
        return agenteService.findById(id).map(p -> {
            p.setMatricula(agente.getMatricula());
            p.setSetor(agente.getSetor());
            agenteService.save(p);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (agenteService.findById(id).isPresent()) {
            agenteService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}