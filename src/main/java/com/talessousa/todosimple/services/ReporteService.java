package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Reporte;
import com.talessousa.todosimple.repositories.ReporteRepository;
import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PessoaService pessoaService;      // Injetar PessoaService

    @Autowired
    private LocalizacaoService localizacaoService; // Injetar LocalizacaoService

    public Reporte findById(Long id) {
        Optional<Reporte> obj = reporteRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Reporte ID " + id + " não encontrada."));
    }

    @Transactional
    public Reporte create(Reporte obj) {
        obj.setId(null);

        // 1. Valida Usuário
        if (obj.getUsuario() != null && obj.getUsuario().getId() != null) {
             try {
                 usuarioService.findById(obj.getUsuario().getId());
             } catch (ObjectNotFoundException e) {
                 throw new DataBindingViolationException("Falha ao criar reporte: Usuário inexistente! Id: " + obj.getUsuario().getId());
             }
        }

        // 2. Valida Pessoa (Novo)
        if (obj.getPessoa() != null && obj.getPessoa().getId() != null) {
            try {
                pessoaService.findById(obj.getPessoa().getId());
            } catch (ObjectNotFoundException e) {
                throw new DataBindingViolationException("Falha ao criar reporte: Pessoa inexistente! Id: " + obj.getPessoa().getId());
            }
        }

        // 3. Valida Localização (Novo)
        if (obj.getLocalizacao() != null && obj.getLocalizacao().getId() != null) {
            try {
                localizacaoService.findById(obj.getLocalizacao().getId());
            } catch (ObjectNotFoundException e) {
                throw new DataBindingViolationException("Falha ao criar reporte: Localização inexistente! Id: " + obj.getLocalizacao().getId());
            }
        }

        return reporteRepository.save(obj);
    }

    @Transactional
    public Reporte update(Reporte obj) {
        Reporte newObj = findById(obj.getId());
        newObj.setDescricao(obj.getDescricao());
        newObj.setStatus(obj.getStatus());
        // Se permitir alterar Usuario/Pessoa/Localizacao no update, precisará validar aqui também.
        return reporteRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            reporteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas (Inspecção)!");
        }
    }

    public List<Reporte> findAll() {
        return reporteRepository.findAll();
    }

    public List<Reporte> findByPessoaId(Long pessoaId) {
        pessoaService.findById(pessoaId); // Valida se pessoa existe antes de buscar
        return reporteRepository.findByPessoaId(pessoaId);
    }

    public List<Reporte> findByLocalizacaoId(Long localizacaoId) {
        localizacaoService.findById(localizacaoId); // Valida se localizacao existe antes de buscar
        return reporteRepository.findByLocalizacaoId(localizacaoId);
    }

    public List<Reporte> findByUsuarioId(Long usuarioId){
        usuarioService.findById(usuarioId); // Valida se usuario existe antes de buscar
        return reporteRepository.findByUsuarioId(usuarioId);
    }
}