package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Pessoa;
import com.talessousa.todosimple.models.Reporte;
import com.talessousa.todosimple.services.PessoaService;
import com.talessousa.todosimple.services.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;
    
    @Autowired
    private ReporteService reporteService;

    @PostMapping
    public ResponseEntity<Pessoa> create(@RequestBody Pessoa pessoa) {
        Pessoa savedPessoa = pessoaService.save(pessoa);
        return new ResponseEntity<>(savedPessoa, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable Long id) {
        return pessoaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Pessoa>> findAll() {
        return ResponseEntity.ok(pessoaService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        return pessoaService.findById(id).map(p -> {
            p.setNome(pessoa.getNome());
            p.setCpf(pessoa.getCpf());
            p.setEmail(pessoa.getEmail());
            p.setTelefone(pessoa.getTelefone());
            pessoaService.save(p);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (pessoaService.findById(id).isPresent()) {
            pessoaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/reportes")
    public ResponseEntity<List<Reporte>> getReportesPorPessoa(@PathVariable Long id) {
        if (!pessoaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Reporte> reportes = reporteService.findByPessoaId(id);
        return ResponseEntity.ok(reportes);
    }
}