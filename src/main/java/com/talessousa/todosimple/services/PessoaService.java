package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Pessoa;
import com.talessousa.todosimple.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public Optional<Pessoa> findById(Long id) {
        return pessoaRepository.findById(id);
    }

    public void deleteById(Long id) {
        pessoaRepository.deleteById(id);
    }
}