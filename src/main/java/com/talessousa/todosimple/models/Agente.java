package com.talessousa.todosimple.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agente")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Agente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "A pessoa é obrigatória")
    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;

    @NotBlank(message = "A matrícula não pode estar vazia.")
    @Size(max = 20, message = "A matrícula não pode exceder 20 caracteres.")
    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @NotBlank(message = "O setor não pode estar vazio.")
    @Size(max = 50, message = "O setor não pode exceder 50 caracteres.")
    @Column(nullable = false, length = 50)
    private String setor;
}
