package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Inspecionar;
import com.talessousa.todosimple.models.Localizacao;
import com.talessousa.todosimple.models.Reporte;
import com.talessousa.todosimple.services.InspecionarService;
import com.talessousa.todosimple.services.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reporte")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;
    
    @Autowired
    private InspecionarService inspecionarService;

    @PostMapping
    public ResponseEntity<Reporte> create(@RequestBody Reporte reporte) {
        Reporte savedReporte = reporteService.save(reporte);
        return new ResponseEntity<>(savedReporte, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> findById(@PathVariable Long id) {
        return reporteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Reporte>> findAll() {
        return ResponseEntity.ok(reporteService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Reporte reporte) {
        return reporteService.findById(id).map(r -> {
            r.setStatus(reporte.getStatus());
            r.setDescricao(reporte.getDescricao());
            reporteService.save(r);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (reporteService.findById(id).isPresent()) {
            reporteService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/inspecionar")
    public ResponseEntity<List<Inspecionar>> getInspecoesPorReporte(@PathVariable Long id) {
        if (!reporteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Inspecionar> inspecoes = inspecionarService.findByReporteId(id);
        return ResponseEntity.ok(inspecoes);
    }

    @GetMapping("/{id}/localizacao")
    public ResponseEntity<Localizacao> getLocalizacaoDoReporte(@PathVariable Long id) {
        return reporteService.findById(id)
                .map(reporte -> ResponseEntity.ok(reporte.getLocalizacao()))
                .orElse(ResponseEntity.notFound().build());
    }
}