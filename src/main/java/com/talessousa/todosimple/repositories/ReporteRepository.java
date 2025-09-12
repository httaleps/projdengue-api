package com.talessousa.todosimple.repositories;

import com.talessousa.todosimple.models.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    // Métodos para buscar reportes baseados nos relacionamentos
    List<Reporte> findByPessoaId(Long pessoaId);
    List<Reporte> findByLocalizacaoId(Long localizacaoId);
    List<Reporte> findByUsuarioId(Long UsuarioId);
    List<Reporte> findByInspecionarId(Long InspecionarId);
    List<Reporte> findByAgenteId(Long AgenteId);
}