package com.talessousa.todosimple.controllers;

import com.talessousa.todosimple.models.Reporte;
import com.talessousa.todosimple.models.Usuario;
import com.talessousa.todosimple.services.ReporteService;
import com.talessousa.todosimple.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuario")
@Validated // Ativa validações de @PathVariable e @RequestParam
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        // Serviço lança 404 automaticamente se não encontrar
        Usuario obj = usuarioService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        List<Usuario> list = usuarioService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Usuario obj) {
        Usuario newObj = usuarioService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Usuario obj) {
        obj.setId(id); // Garante consistência entre URL e corpo
        usuarioService.update(obj); // Método update do serviço cuida da lógica
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoints Relacionados ---

    @GetMapping("/{id}/reportes")
    public ResponseEntity<List<Reporte>> getReportesPorUsuario(@PathVariable Long id) {
        // Garante que o usuário existe antes de buscar seus reportes
        usuarioService.findById(id);
        List<Reporte> list = reporteService.findByUsuarioId(id);
        return ResponseEntity.ok().body(list);
    }
}