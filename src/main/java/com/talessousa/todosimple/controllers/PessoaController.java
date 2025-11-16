package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Pessoa;
import com.talessousa.todosimple.models.projection.ReporteProjection;
import com.talessousa.todosimple.services.PessoaService;
import com.talessousa.todosimple.services.ReporteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pessoa")
@Validated
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable Long id) {
        Pessoa obj = pessoaService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> findAll() {
        List<Pessoa> list = pessoaService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Pessoa obj) {
        Pessoa newObj = pessoaService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Pessoa obj) {
        obj.setId(id);
        pessoaService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reportes")
    public ResponseEntity<List<ReporteProjection>> getReportesPorPessoa(@PathVariable Long id) {
        pessoaService.findById(id);
        List<ReporteProjection> list = reporteService.findByPessoaId(id);
        return ResponseEntity.ok().body(list);
    }
}