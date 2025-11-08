package com.talessousa.todosimple.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.talessousa.todosimple.models.enums.StatusInspecao;

@Entity
@Table(name = "inspecionar")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Inspecionar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "O reporte é obrigatório")
    @ManyToOne
    @JoinColumn(name = "reporte_id", nullable = false)
    private Reporte reporte;

    @NotNull(message = "O agente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;

    @NotNull(message = "A localização é obrigatória")
    @ManyToOne
    @JoinColumn(name = "localizacao_id", nullable = false)
    private Localizacao localizacao;

    @NotNull(message = "A data da inspeção é obrigatória")
    @PastOrPresent(message = "A data da inspeção não pode ser futura")
    @Column(nullable = false)
    private LocalDate dataInspecao;

    @Column(columnDefinition = "TEXT")
    @Size(max = 5000, message = "A observação deve ter no máximo 5000 caracteres")
    private String observacao;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusInspecao status = StatusInspecao.PENDENTE;

    @Column(nullable = false)
    private boolean focoConfirmado = false;
}
