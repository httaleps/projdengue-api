package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Inspecionar;
import com.talessousa.todosimple.models.Localizacao;
import com.talessousa.todosimple.models.Reporte;
import com.talessousa.todosimple.models.projection.ReporteProjection;
import com.talessousa.todosimple.services.InspecionarService;
import com.talessousa.todosimple.services.ReporteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/reporte")
@Validated 
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private InspecionarService inspecionarService;

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> findById(@PathVariable Long id) {
        Reporte obj = reporteService.findById(id);
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/usuario")
    public ResponseEntity <List<ReporteProjection>> findAllByUser() {
        List<ReporteProjection> objs = reporteService.findAllByUsuario();
        return ResponseEntity.ok(objs);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Reporte obj) {
        Reporte saved = reporteService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody Reporte obj, @PathVariable Long id) {
        obj.setId(id);
        reporteService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reporteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoints Relacionados ---

    @GetMapping("/{id}/inspecionar")
    public ResponseEntity<List<Inspecionar>> getInspecoesPorReporte(@PathVariable Long id) {
        reporteService.findById(id);
        List<Inspecionar> list = inspecionarService.findByReporteId(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}/localizacao")
    public ResponseEntity<Localizacao> getLocalizacaoDoReporte(@PathVariable Long id) {
        Reporte obj = reporteService.findById(id);
        return ResponseEntity.ok().body(obj.getLocalizacao());
    }

    @GetMapping("/meus-reportes")
    public ResponseEntity<List<ReporteProjection>> findMyReports() {
        List<ReporteProjection> list = reporteService.findAllByUsuario();
        return ResponseEntity.ok().body(list);
    }
}