package com.talessousa.todosimple.repositories;

import com.talessousa.todosimple.models.Reporte;
import com.talessousa.todosimple.models.projection.ReporteProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    // Métodos para buscar reportes baseados nos relacionamentos
    List<ReporteProjection> findByUsuario_Id(Long usuarioId);
    List<ReporteProjection> findByPessoaId(Long pessoaId);
    List<ReporteProjection> findByLocalizacaoId(Long localizacaoId);
    List<ReporteProjection> findByUsuarioId(Long usuarioId);
}