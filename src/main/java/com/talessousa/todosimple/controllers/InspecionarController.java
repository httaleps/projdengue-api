package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Inspecionar;
import com.talessousa.todosimple.services.InspecionarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inspecionar")
public class InspecionarController {

    @Autowired
    private InspecionarService inspecionarService;

    @PostMapping
    public ResponseEntity<Inspecionar> create(@RequestBody Inspecionar inspecionar) {
        Inspecionar savedInspecionar = inspecionarService.save(inspecionar);
        return new ResponseEntity<>(savedInspecionar, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inspecionar> findById(@PathVariable Long id) {
        return inspecionarService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Inspecionar>> findAll() {
        return ResponseEntity.ok(inspecionarService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Inspecionar inspecionar) {
        return inspecionarService.findById(id).map(p -> {
            p.setDataInspecao(inspecionar.getDataInspecao());
            p.setObservacao(inspecionar.getObservacao());
            p.setResultado(inspecionar.getResultado());
            inspecionarService.save(p);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (inspecionarService.findById(id).isPresent()) {
            inspecionarService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}