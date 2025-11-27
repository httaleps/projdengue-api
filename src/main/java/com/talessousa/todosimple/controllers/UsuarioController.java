package com.talessousa.todosimple.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.talessousa.todosimple.models.Usuario;
import com.talessousa.todosimple.models.dto.UsuarioCreateDTO;
import com.talessousa.todosimple.models.dto.UsuarioUpdateDTO;
import com.talessousa.todosimple.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        Usuario obj = usuarioService.findById(id);
        return ResponseEntity.ok(obj);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        List<Usuario> list = usuarioService.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UsuarioCreateDTO dto) {
        Usuario usuario = usuarioService.fromDTO(dto);
        Usuario saved = usuarioService.create(usuario);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        dto.setId(id);
        Usuario usuario = usuarioService.fromUpdateDTO(dto);
        usuarioService.update(usuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}