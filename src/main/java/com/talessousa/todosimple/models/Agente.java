package com.talessousa.todosimple.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "agente")
@Data
public class Agente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Column(nullable = false, length = 50)
    private String setor;
}
