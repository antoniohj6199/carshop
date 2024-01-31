package com.antonio.vendas;

import java.io.Serializable;
import java.util.Objects;

public class Cliente implements Serializable, Comparable<Cliente> {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private String createdAt;
    private String updatedAt;

    public Cliente() {

    }

    public Cliente(String id, String nome, String createdAt, String updatedAt) {
        this.id = id;
        this.nome = nome;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Outros métodos conforme necessário

    @Override
    public String toString() {
        return "Cliente{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                // Outros atributos
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(getId(), cliente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public int compareTo(Cliente outroCliente) {
        // Implemente a lógica de comparação, geralmente baseada no ID ou em outro critério relevante
        return this.id.compareTo(outroCliente.id);
    }
}
