package com.talessousa.todosimple.services;

import java.util.List;
import java.util.Set;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talessousa.todosimple.models.Pessoa;
import com.talessousa.todosimple.models.Usuario;
import com.talessousa.todosimple.models.dto.UsuarioCreateDTO;
import com.talessousa.todosimple.models.dto.UsuarioUpdateDTO;
import com.talessousa.todosimple.models.enums.ProfileEnum;
import com.talessousa.todosimple.repositories.UsuarioRepository;
import com.talessousa.todosimple.security.UserSpringSecurity;
import com.talessousa.todosimple.services.exceptions.AuthorizationException;
import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario findById(Long id) { 
        UserSpringSecurity usuario = authenticated();
        if (usuario == null || (!usuario.hasRole(ProfileEnum.ADMIN) && !id.equals(usuario.getId()))) {
            throw new AuthorizationException("Acesso negado.");
        }
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Usuário ID " + id + " não encontrado."));
    }

    @Transactional
    public Usuario create(Usuario obj) {
        obj.setId(null);
        obj.setSenha(passwordEncoder.encode(obj.getSenha()));
        obj.setProfiles(Set.of(ProfileEnum.USER));

        if (obj.getPessoa() != null && obj.getPessoa().getId() != null) {
            try {
                var pessoa = pessoaService.findById(obj.getPessoa().getId());
                obj.setPessoa(pessoa); 
            } catch (ObjectNotFoundException e) {
                throw new DataBindingViolationException("Pessoa não encontrada com ID: " + obj.getPessoa().getId());
            }
        } else {
            throw new DataBindingViolationException("É necessário informar uma Pessoa para criar um Usuário.");
        }

        return usuarioRepository.save(obj);
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
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public static UserSpringSecurity authenticated() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserSpringSecurity) {
            return (UserSpringSecurity) auth.getPrincipal();
        }
        return null;
    }

    public Usuario fromDTO(UsuarioCreateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsuario(dto.getUsuario());
        usuario.setSenha(dto.getSenha());

        if (dto.getPessoaId() != null) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(dto.getPessoaId());
            usuario.setPessoa(pessoa);
        }

        return usuario;
    }

    public Usuario fromUpdateDTO(UsuarioUpdateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setSenha(dto.getSenha());
        return usuario;
    }
}