package com.antonio.vendas;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class Carro implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String idCliente;
    private String idVendedor;
    private String modelo;
    private String cor;
    private double preco;
    private boolean disponivel;
    private Instant createdAt;
    private Instant updatedAt;

    public Carro(String id, String idCliente, String idVendedor, String modelo, String cor, double preco, boolean disponivel, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.modelo = modelo;
        this.cor = cor;
        this.preco = preco;
        this.disponivel = disponivel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Outros métodos conforme necessário

    @Override
    public String toString() {
        return "Carro{" +
                "id='" + id + '\'' +
                ", idCliente='" + idCliente + '\'' +
                ", idVendedor='" + idVendedor + '\'' +
                ", modelo='" + modelo + '\'' +
                ", cor='" + cor + '\'' +
                ", preco=" + preco +
                ", disponivel=" + disponivel +
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
        Carro carro = (Carro) o;
        return Objects.equals(getId(), carro.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}