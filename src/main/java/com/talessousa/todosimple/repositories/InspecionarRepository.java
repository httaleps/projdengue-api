package com.talessousa.todosimple.repositories;

import com.talessousa.todosimple.models.Inspecionar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InspecionarRepository extends JpaRepository<Inspecionar, Long> {
    // Métodos para buscar inspeções baseadas nos relacionamentos
    List<Inspecionar> findByReporteId(Long reporteId);
    List<Inspecionar> findByAgenteId(Long agenteId);
}