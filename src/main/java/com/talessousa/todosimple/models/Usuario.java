package com.talessousa.todosimple.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "O campo email não pode estar vazio.")
    @Email(message = "O campo email deve conter um endereço de email válido.")
    @Size(max = 100, message = "O campo email não pode exceder 100 caracteres.")
    private String email;

    @Column(length = 20)
    @Size(max = 11, message = "O campo telefone não pode exceder 20 caracteres.")
    private String telefone;
}
