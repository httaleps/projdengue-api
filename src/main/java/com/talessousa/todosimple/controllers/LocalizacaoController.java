package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Localizacao;
import com.talessousa.todosimple.models.Reporte;
import com.talessousa.todosimple.services.LocalizacaoService;
import com.talessousa.todosimple.services.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/localizacao")
public class LocalizacaoController {

    @Autowired
    private LocalizacaoService localizacaoService;
    
    @Autowired
    private ReporteService reporteService;

    @PostMapping
    public ResponseEntity<Localizacao> create(@RequestBody Localizacao localizacao) {
        Localizacao savedLocalizacao = localizacaoService.save(localizacao);
        return new ResponseEntity<>(savedLocalizacao, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Localizacao> findById(@PathVariable Long id) {
        return localizacaoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Localizacao>> findAll() {
        return ResponseEntity.ok(localizacaoService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Localizacao localizacao) {
        return localizacaoService.findById(id).map(p -> {
            p.setLogradouro(localizacao.getLogradouro());
            p.setNumero(localizacao.getNumero());
            p.setBairro(localizacao.getBairro());
            p.setCidade(localizacao.getCidade());
            p.setUf(localizacao.getUf());
            p.setCep(localizacao.getCep());
            p.setLatitude(localizacao.getLatitude());
            p.setLongitude(localizacao.getLongitude());
            localizacaoService.save(p);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (localizacaoService.findById(id).isPresent()) {
            localizacaoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/reportes")
    public ResponseEntity<List<Reporte>> getReportesPorLocalizacao(@PathVariable Long id) {
        if (!localizacaoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Reporte> reportes = reporteService.findByLocalizacaoId(id);
        return ResponseEntity.ok(reportes);
    }
}