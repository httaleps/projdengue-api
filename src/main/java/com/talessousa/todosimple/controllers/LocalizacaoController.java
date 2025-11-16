package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Localizacao;
import com.talessousa.todosimple.models.projection.ReporteProjection;
import com.talessousa.todosimple.services.LocalizacaoService;
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
@RequestMapping("/localizacao")
@Validated
public class LocalizacaoController {

    @Autowired
    private LocalizacaoService localizacaoService;

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/{id}")
    public ResponseEntity<Localizacao> findById(@PathVariable Long id) {
        Localizacao obj = localizacaoService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Localizacao>> findAll() {
        List<Localizacao> list = localizacaoService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Localizacao obj) {
        Localizacao newObj = localizacaoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Localizacao obj) {
        obj.setId(id);
        localizacaoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        localizacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reportes")
    public ResponseEntity<List<ReporteProjection>> getReportesPorLocalizacao(@PathVariable Long id) {
        localizacaoService.findById(id);
        List<ReporteProjection> list = reporteService.findByLocalizacaoId(id);
        return ResponseEntity.ok().body(list);
    }
}