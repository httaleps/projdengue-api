package com.talessousa.todosimple.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.talessousa.todosimple.models.enums.ProfileEnum;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    public interface CreateUsuario {}
    public interface UpdateUsuario {}

    public static final String TABLE_NAME = "usuario";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;

    @Column(name = "usuario", length = 100, nullable = false, unique = true)
    @NotNull(groups = CreateUsuario.class)
    @NotEmpty(groups = CreateUsuario.class)
    @Size(groups = CreateUsuario.class, min = 2, max = 100)
    private String usuario;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "O campo email não pode estar vazio.")
    @Email(message = "O campo email deve conter um endereço de email válido.")
    @Size(max = 100, message = "O campo email não pode exceder 100 caracteres.")
    private String email;

    @Column(length = 20)
    @Size(max = 11, message = "O campo telefone não pode exceder 20 caracteres.")
    private String telefone;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "senha", length = 60, nullable = false)
    @NotNull(groups = CreateUsuario.class)
    @NotEmpty(groups = CreateUsuario.class)
    @Size(groups = CreateUsuario.class, min = 8, max = 60)
    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_profile")
    @Column(name = "profile", nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    private Set<ProfileEnum> profiles = new HashSet<>();
}
