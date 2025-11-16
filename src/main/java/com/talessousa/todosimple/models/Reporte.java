package com.talessousa.todosimple.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.talessousa.todosimple.models.enums.StatusReporte;

@Entity
@Table(name = Reporte.TABLE_NAME)
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reporte {
    public static final String TABLE_NAME = "reporte";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull(message = "O usuário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false)
    private Usuario usuario;

    @NotNull(message = "A pessoa reportada é obrigatória")
    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false, updatable = false)
    private Pessoa pessoa;

    @NotNull(message = "A localização é obrigatória")
    @ManyToOne
    @JoinColumn(name = "localizacao_id", nullable = false)
    private Localizacao localizacao;

    @Column(name = "descrição", length = 255, nullable = false)
    @NotBlank
    @Size(min = 1, max = 5000) 
    private String descricao;

    @NotNull(message = "A data do reporte é obrigatória")
    @PastOrPresent(message = "A data do reporte não pode ser futura")
    @Column(nullable = false)
    private LocalDate dataReporte;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusReporte status = StatusReporte.PENDENTE;
}