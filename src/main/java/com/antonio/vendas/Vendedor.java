package com.antonio.vendas;

import java.io.Serializable;

public class Vendedor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    // Outros atributos relevantes para um vendedor, como ID, desempenho de vendas, etc.

    public Vendedor(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Outros métodos conforme necessário

    @Override
    public String toString() {
        return "Vendedor{" +
                "nome='" + nome + '\'' +
                // Outros atributos
                '}';
    }
}
