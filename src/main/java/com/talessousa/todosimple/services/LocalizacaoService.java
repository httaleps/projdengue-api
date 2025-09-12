package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Localizacao;
import com.talessousa.todosimple.repositories.LocalizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LocalizacaoService {
    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    public Localizacao save(Localizacao localizacao) {
        return localizacaoRepository.save(localizacao);
    }

    public List<Localizacao> findAll() {
        return localizacaoRepository.findAll();
    }

    public Optional<Localizacao> findById(Long id) {
        return localizacaoRepository.findById(id);
    }

    public void deleteById(Long id) {
        localizacaoRepository.deleteById(id);
    }
}