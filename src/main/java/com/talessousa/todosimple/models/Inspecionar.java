package com.talessousa.todosimple.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "inspecionar")
@Data
public class Inspecionar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporte_id", nullable = false)
    private Reporte reporte;

    @ManyToOne
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;

    @ManyToOne
    @JoinColumn(name = "localizacao_id", nullable = false)
    private Localizacao localizacao;

    @Column(nullable = false)
    private LocalDate dataInspecao;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(nullable = false, length = 20)
    private String resultado = "PENDENTE";

    @Column(nullable = false)
    private boolean focoConfirmado = false;
}
