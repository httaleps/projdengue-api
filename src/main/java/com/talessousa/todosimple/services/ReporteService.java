package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Reporte;
import com.talessousa.todosimple.repositories.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {
    
    @Autowired
    private ReporteRepository reporteRepository;

    public Reporte save(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public List<Reporte> findAll() {
        return reporteRepository.findAll();
    }

    public Optional<Reporte> findById(Long id) {
        return reporteRepository.findById(id);
    }

    public void deleteById(Long id) {
        reporteRepository.deleteById(id);
    }
    
    // Métodos para os endpoints de relacionamento
    public List<Reporte> findByPessoaId(Long pessoaId) {
        return reporteRepository.findByPessoaId(pessoaId);
    }
    
    public List<Reporte> findByLocalizacaoId(Long localizacaoId) {
        return reporteRepository.findByLocalizacaoId(localizacaoId);
    }

    public List<Reporte> findByUsuarioId(Long usuarioId){
        return reporteRepository.findByUsuarioId(usuarioId);
    }

}