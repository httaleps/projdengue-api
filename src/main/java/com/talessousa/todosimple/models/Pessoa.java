package com.talessousa.todosimple.models;

import org.hibernate.validator.constraints.br.CPF;

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
@Table(name = "pessoa")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "O nome não pode estar vazio.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String nome;

    @Column(nullable = false, unique = true, length = 11, updatable = false)
    @NotBlank(message = "O CPF não pode estar vazio.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 caracteres.")
    @CPF(message = "O CPF informado é inválido.")
    private String cpf;

    @Column(length = 20)
    @Size(max = 11, message = "O telefone não pode exceder 11 caracteres.")
    private String telefone;

    @Column(length = 100)
    @Email(message = "O email deve ser válido.")
    @Size(max = 100, message = "O email não pode exceder 100 caracteres.")
    private String email;
}
