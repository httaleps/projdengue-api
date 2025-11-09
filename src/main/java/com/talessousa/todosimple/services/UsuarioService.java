package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Usuario;
import com.talessousa.todosimple.models.enums.ProfileEnum;
import com.talessousa.todosimple.repositories.UsuarioRepository;
import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario findById(Long id) {
        Optional<Usuario> user = this.usuarioRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuário ID " + id + " não encontrado."));
    }

    @Transactional
    public Usuario create(Usuario usuario) {
        usuario.setId(null);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setProfiles(Set.of(ProfileEnum.USER));

        if (usuario.getPessoa() != null && usuario.getPessoa().getId() != null) {
            try {
                pessoaService.findById(usuario.getPessoa().getId());
            } catch (ObjectNotFoundException e) {
                throw new DataBindingViolationException("Usuário não encontrado com ID: " + usuario.getPessoa().getId());
            }
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Usuario obj) {
        Usuario existingObj = findById(obj.getId());
        existingObj.setSenha(passwordEncoder.encode(obj.getSenha()));
        return usuarioRepository.save(existingObj);
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