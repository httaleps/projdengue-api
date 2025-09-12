package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Inspecionar;
import com.talessousa.todosimple.repositories.InspecionarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InspecionarService {
    @Autowired
    private InspecionarRepository inspecionarRepository;

    public Inspecionar save(Inspecionar inspecionar) {
        return inspecionarRepository.save(inspecionar);
    }

    public List<Inspecionar> findAll() {
        return inspecionarRepository.findAll();
    }

    public Optional<Inspecionar> findById(Long id) {
        return inspecionarRepository.findById(id);
    }

    public void deleteById(Long id) {
        inspecionarRepository.deleteById(id);
    }

    // Métodos para os endpoints de relacionamento
    public List<Inspecionar> findByReporteId(Long reporteId) {
        return inspecionarRepository.findByReporteId(reporteId);
    }

    public List<Inspecionar> findByAgenteId(Long agenteId) {
        return inspecionarRepository.findByAgenteId(agenteId);
    }
}