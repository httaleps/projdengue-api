package com.talessousa.todosimple.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioCreateDTO {

    @NotBlank
    @Size(min = 3, max = 60)
    private String usuario;

    @NotBlank
    @Size(min = 6, max = 24)
    private String senha;

    @NotNull(message = "O ID da pessoa é obrigatório.")
    private Long pessoaId;
}
