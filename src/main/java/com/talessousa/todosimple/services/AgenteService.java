package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Agente;
import com.talessousa.todosimple.repositories.AgenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AgenteService {
    @Autowired
    private AgenteRepository agenteRepository;

    public Agente save(Agente agente) {
        return agenteRepository.save(agente);
    }

    public List<Agente> findAll() {
        return agenteRepository.findAll();
    }

    public Optional<Agente> findById(Long id) {
        return agenteRepository.findById(id);
    }

    public void deleteById(Long id) {
        agenteRepository.deleteById(id);
    }
}