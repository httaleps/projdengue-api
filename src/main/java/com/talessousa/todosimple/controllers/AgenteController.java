package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Agente;
import com.talessousa.todosimple.models.Inspecionar;
import com.talessousa.todosimple.services.AgenteService;
import com.talessousa.todosimple.services.InspecionarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/agente")
@Validated
public class AgenteController {

    @Autowired
    private AgenteService agenteService;

    @Autowired
    private InspecionarService inspecionarService;

    @GetMapping("/{id}")
    public ResponseEntity<Agente> findById(@PathVariable Long id) {
        Agente obj = agenteService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Agente>> findAll() {
        List<Agente> list = agenteService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Agente obj) {
        Agente newObj = agenteService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Agente obj) {
        obj.setId(id);
        agenteService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agenteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/inspecionar")
    public ResponseEntity<List<Inspecionar>> getInspecoesPorAgente(@PathVariable Long id) {
        agenteService.findById(id);
        List<Inspecionar> list = inspecionarService.findByAgenteId(id);
        return ResponseEntity.ok().body(list);
    }
}