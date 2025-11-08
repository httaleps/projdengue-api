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

import com.talessousa.todosimple.models.enums.StatusReporte;

@Entity
@Table(name = "reporte")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "O usuário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "A pessoa reportada é obrigatória")
    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @NotNull(message = "A localização é obrigatória")
    @ManyToOne
    @JoinColumn(name = "localizacao_id", nullable = false)
    private Localizacao localizacao;

    @Column(columnDefinition = "TEXT")
    @Size(max = 5000, message = "A descrição deve ter no máximo 5000 caracteres") 
    private String descricao;

    @NotNull(message = "A data do reporte é obrigatória")
    @PastOrPresent(message = "A data do reporte não pode ser futura")
    @Column(nullable = false)
    private LocalDate dataReporte;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusReporte status = StatusReporte.PENDENTE;
}