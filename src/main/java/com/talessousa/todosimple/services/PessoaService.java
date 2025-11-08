package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Pessoa;
import com.talessousa.todosimple.repositories.PessoaRepository;
import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa findById(Long id) {
        Optional<Pessoa> obj = pessoaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Pessoa ID " + id + " não encontrada."));
    }

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    @Transactional
    public Pessoa create(Pessoa obj) {
        obj.setId(null); // Garante que é uma criação, não atualização
        return pessoaRepository.save(obj);
    }

    @Transactional
    public Pessoa update(Pessoa obj) {
        // Verifica se existe antes de tentar atualizar
        Pessoa newObj = findById(obj.getId());
        // Opcional: Atualizar apenas campos permitidos para não sobrescrever tudo acidentalmente
        newObj.setNome(obj.getNome());
        newObj.setTelefone(obj.getTelefone());
        newObj.setEmail(obj.getEmail());
        // CPF geralmente não se atualiza, então não incluí aqui.
        return pessoaRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id); // Garante que existe antes de tentar deletar
        try {
            pessoaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // Lança erro 409 se a pessoa estiver vinculada a um Usuario, Agente ou Reporte
            throw new DataBindingViolationException("Não é possível excluir uma Pessoa que possui vínculos ativos!");
        }
    }
}