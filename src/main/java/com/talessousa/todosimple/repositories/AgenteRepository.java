package com.talessousa.todosimple.repositories;

import com.talessousa.todosimple.models.Agente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenteRepository extends JpaRepository<Agente, Long> {
}