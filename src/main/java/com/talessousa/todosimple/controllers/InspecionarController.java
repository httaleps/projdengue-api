package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Inspecionar;
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
@RequestMapping("/inspecionar")
@Validated
public class InspecionarController {

    @Autowired
    private InspecionarService inspecionarService;

    @GetMapping("/{id}")
    public ResponseEntity<Inspecionar> findById(@PathVariable Long id) {
        Inspecionar obj = inspecionarService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Inspecionar>> findAll() {
        List<Inspecionar> list = inspecionarService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Inspecionar obj) {
        Inspecionar newObj = inspecionarService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Inspecionar obj) {
        obj.setId(id);
        inspecionarService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inspecionarService.delete(id);
        return ResponseEntity.noContent().build();
    }
}