package com.talessousa.todosimple.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talessousa.todosimple.models.Reporte;
import com.talessousa.todosimple.models.Usuario;
import com.talessousa.todosimple.models.enums.ProfileEnum;
import com.talessousa.todosimple.models.projection.ReporteProjection;
import com.talessousa.todosimple.repositories.ReporteRepository;
import com.talessousa.todosimple.security.UserSpringSecurity;
import com.talessousa.todosimple.services.exceptions.AuthorizationException;
import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;

import org.springframework.transaction.annotation.Transactional;


@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private LocalizacaoService localizacaoService;

    public Reporte findById(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Reporte ID " + id + " não encontrado. Tipo: " + Reporte.class.getName()));

        UserSpringSecurity authenticatedUsuario = UsuarioService.authenticated();
        if (authenticatedUsuario == null || 
            (!authenticatedUsuario.hasRole(ProfileEnum.ADMIN) &&
            !isOwner(authenticatedUsuario, reporte))) {
            throw new AuthorizationException("Acesso negado.");
        }

        return reporte;
    }

    public List<ReporteProjection> findAllByUsuario(){
        UserSpringSecurity usuario = UsuarioService.authenticated();
        if (usuario == null) {
            throw new AuthorizationException("Acesso negado.");
        }
        return reporteRepository.findByUsuario_Id(usuario.getId());
    } 
    
    @Transactional
    public Reporte create(Reporte obj) {
        UserSpringSecurity usuario = UsuarioService.authenticated();
        if (usuario == null) {
            throw new AuthorizationException("Acesso negado.");
        }
        Usuario owner = usuarioService.findById(usuario.getId());
        obj.setUsuario(owner);
        obj.setId(null);

        if (obj.getPessoa() != null && obj.getPessoa().getId() != null) {
            try {
                obj.setPessoa(pessoaService.findById(obj.getPessoa().getId()));
            } catch (ObjectNotFoundException e) {
                throw new DataBindingViolationException("Falha ao criar reporte: Pessoa inexistente! Id: " + obj.getPessoa().getId());
            }
        }

        if (obj.getLocalizacao() != null && obj.getLocalizacao().getId() != null) {
            try {
                obj.setLocalizacao(localizacaoService.findById(obj.getLocalizacao().getId()));
            } catch (ObjectNotFoundException e) {
                throw new DataBindingViolationException("Falha ao criar reporte: Localização inexistente! Id: " + obj.getLocalizacao().getId());
            }
        }

        return reporteRepository.save(obj);
    }

    @Transactional
    public Reporte update(Reporte obj) {
        Reporte existing = findById(obj.getId());
        existing.setDescricao(obj.getDescricao());
        return reporteRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        try {
            reporteRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas (Inspecção)!");
        }
    }

    public List<ReporteProjection> findByPessoaId(Long pessoaId) {
        pessoaService.findById(pessoaId);
        return reporteRepository.findByPessoaId(pessoaId);
    }

    public List<ReporteProjection> findByLocalizacaoId(Long localizacaoId) {
        localizacaoService.findById(localizacaoId);
        return reporteRepository.findByLocalizacaoId(localizacaoId);
    }

    public List<ReporteProjection> findByUsuarioId(Long usuarioId) {
        usuarioService.findById(usuarioId); // Garante que o usuário existe antes de buscar
        return reporteRepository.findByUsuarioId(usuarioId);
    }

    private boolean isOwner(UserSpringSecurity usuario, Reporte reporte) {
        return reporte.getUsuario().getId().equals(usuario.getId());
    }
    
}