package com.talessousa.todosimple.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioUpdateDTO {

    private long id;

    @NotBlank
    @Size(min = 3, max = 24)
    private String senha;
    
}
