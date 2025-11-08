package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Inspecionar;
import com.talessousa.todosimple.models.Localizacao;
import com.talessousa.todosimple.models.Reporte;
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

@RestController
@RequestMapping("/reporte")
@Validated // Habilita validações de PathVariable/RequestParam se houver
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private InspecionarService inspecionarService;

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> findById(@PathVariable Long id) {
        // O serviço já lança ObjectNotFoundException (404) se não achar
        Reporte obj = reporteService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Reporte>> findAll() {
        List<Reporte> list = reporteService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Reporte obj) {
        Reporte newObj = reporteService.create(obj);
        // Cria a URI do novo recurso (ex: http://localhost:8080/reporte/1)
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Reporte obj) {
        obj.setId(id); // Garante que o ID do corpo da requisição é o mesmo da URL
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
        // Garante que o reporte existe antes de buscar inspeções
        reporteService.findById(id);
        List<Inspecionar> list = inspecionarService.findByReporteId(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}/localizacao")
    public ResponseEntity<Localizacao> getLocalizacaoDoReporte(@PathVariable Long id) {
        Reporte obj = reporteService.findById(id);
        return ResponseEntity.ok().body(obj.getLocalizacao());
    }
}