package com.talessousa.todosimple.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "localizacao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "O logradouro não pode estar vazio.")
    @Size(max = 100, message = "O logradouro deve ter no máximo 100 caracteres.")
    private String logradouro;

    @Column(length = 10)
    @Size(max = 10, message = "O número deve ter no máximo 10 caracteres.")
    private String numero;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "O bairro não pode estar vazio.")
    @Size(max = 50, message = "O bairro deve ter no máximo 50 caracteres.")
    private String bairro;

    @Column(nullable = false, length = 80)
    @NotBlank(message = "A cidade não pode estar vazia.")
    @Size(max = 80, message = "A cidade deve ter no máximo 80 caracteres.")
    private String cidade;

    @Column(nullable = false, length = 2)
    @NotBlank(message = "A UF não pode estar vazia.")
    @Size(min = 2, max = 2, message = "A UF deve ter 2 caracteres.")
    private String uf;

    @Column(length = 9)
    @Size(max = 9, message = "O CEP deve ter no máximo 9 caracteres.")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato 99999-999")
    private String cep;

    @Column(precision = 10, scale = 8)
    @DecimalMin(value = "-90.0", message = "Latitude mínima inválida")
    @DecimalMax(value = "90.0", message = "Latitude máxima inválida")
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    @DecimalMin(value = "-180.0", message = "Longitude mínima inválida")
    @DecimalMax(value = "180.0", message = "Longitude máxima inválida")
    private BigDecimal longitude;
}
