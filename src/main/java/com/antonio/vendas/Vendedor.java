package com.antonio.vendas;

import java.io.Serializable;
import java.util.Objects;

public class Vendedor implements Serializable, Comparable<Vendedor> {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String createdAt;
    private String updatedAt;

    public Vendedor(){
        
    }
    public Vendedor(int id, String nome, String createdAt, String updatedAt) {
        this.id = id;
        this.nome = nome;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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


    @Override
    public String toString() {
        return "Vendedor{" +
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
        Vendedor vendedor = (Vendedor) o;
        return Objects.equals(getId(), vendedor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    
    @Override
    public int compareTo(Vendedor outroVendedor) {
        return Integer.compare(this.id, outroVendedor.id);
    }
}
