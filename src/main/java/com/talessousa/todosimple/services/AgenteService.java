package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Agente;
import com.talessousa.todosimple.repositories.AgenteRepository;
import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AgenteService {

    @Autowired
    private AgenteRepository agenteRepository;

    public Agente findById(Long id) {
        Optional<Agente> obj = agenteRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Agente ID " + id + " não encontrada."));
    }

    public List<Agente> findAll() {
        return agenteRepository.findAll();
    }

    @Transactional
    public Agente create(Agente obj) {
        obj.setId(null); // Garante que é uma criação
        return agenteRepository.save(obj);
    }

    @Transactional
    public Agente update(Agente obj) {
        Agente newObj = findById(obj.getId()); // Verifica se existe
        // Atualiza campos permitidos
        newObj.setMatricula(obj.getMatricula());
        newObj.setSetor(obj.getSetor());
        return agenteRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id); // Verifica se existe antes de tentar deletar
        try {
            this.agenteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas (Inspeções)!");
        }
    }
}