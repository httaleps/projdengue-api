package com.talessousa.todosimple.services;

import com.talessousa.todosimple.models.Localizacao;
import com.talessousa.todosimple.repositories.LocalizacaoRepository;
import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocalizacaoService {

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    public Localizacao findById(Long id) {
        Optional<Localizacao> obj = localizacaoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Localização ID " + id + " não encontrada."));
    }

    public List<Localizacao> findAll() {
        return localizacaoRepository.findAll();
    }

    @Transactional
    public Localizacao create(Localizacao obj) {
        obj.setId(null); // Garante que é uma criação
        return localizacaoRepository.save(obj);
    }

    @Transactional
    public Localizacao update(Localizacao obj) {
        Localizacao newObj = findById(obj.getId()); // Verifica se existe
        // Atualiza os dados permitidos
        newObj.setLogradouro(obj.getLogradouro());
        newObj.setNumero(obj.getNumero());
        newObj.setBairro(obj.getBairro());
        newObj.setCidade(obj.getCidade());
        newObj.setUf(obj.getUf());
        newObj.setCep(obj.getCep());
        newObj.setLatitude(obj.getLatitude());
        newObj.setLongitude(obj.getLongitude());
        return localizacaoRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id); // Verifica se existe antes de tentar deletar
        try {
            this.localizacaoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas (Reportes/Inspeções)!");
        }
    }
}