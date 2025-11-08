package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Usuario;
import com.talessousa.todosimple.repositories.UsuarioRepository;
import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaService pessoaService; // <--- 1. Injeção do PessoaService

    public Usuario findById(Long id) {
        Optional<Usuario> user = this.usuarioRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuário ID " + id + " não encontrada."));
    }

    @Transactional
    public Usuario create(Usuario usuario) { // Renomeie para 'create' se preferir padronizar
        usuario.setId(null);

        if (usuario.getPessoa() != null && usuario.getPessoa().getId() != null) {
            try {
                pessoaService.findById(usuario.getPessoa().getId());
            } catch (ObjectNotFoundException e) {
                // Lança a exceção 409 com a mensagem EXATA que você pediu
                throw new DataBindingViolationException("Usuário não encontrado com ID: " + usuario.getPessoa().getId());
            }
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Usuario obj) {
        Usuario newObj = findById(obj.getId());
        newObj.setEmail(obj.getEmail());
        newObj.setTelefone(obj.getTelefone());
        return usuarioRepository.save(newObj);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.usuarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }
}