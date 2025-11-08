package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Inspecionar;
import com.talessousa.todosimple.repositories.InspecionarRepository;
import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InspecionarService {

    @Autowired
    private InspecionarRepository inspecionarRepository;

    public Inspecionar findById(Long id) {
        Optional<Inspecionar> obj = inspecionarRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Inspecionar ID " + id + " não encontrada."));
    }

    public List<Inspecionar> findAll() {
        return inspecionarRepository.findAll();
    }

    @Transactional
    public Inspecionar create(Inspecionar obj) {
        obj.setId(null); // Garante que é uma criação
        return inspecionarRepository.save(obj);
    }

    @Transactional
    public Inspecionar update(Inspecionar obj) {
        Inspecionar newObj = findById(obj.getId()); // Verifica se existe
        // Atualiza os dados permitidos
        newObj.setDataInspecao(obj.getDataInspecao());
        newObj.setObservacao(obj.getObservacao());
        newObj.setStatus(obj.getStatus());
        newObj.setFocoConfirmado(obj.isFocoConfirmado());
        // Geralmente não se altera Reporte/Agente/Localização de uma inspeção já feita,
        // mas se for necessário, adicione aqui.
        return inspecionarRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id); // Verifica se existe
        try {
            this.inspecionarRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    // Métodos para os endpoints de relacionamento
    public List<Inspecionar> findByReporteId(Long reporteId) {
        return inspecionarRepository.findByReporteId(reporteId);
    }

    public List<Inspecionar> findByAgenteId(Long agenteId) {
        return inspecionarRepository.findByAgenteId(agenteId);
    }
}