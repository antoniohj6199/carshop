package com.antonio.vendas;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nomeCliente;
    private String nomeVendedor;
    private String modeloCarro;
    private double valor;
    private Instant createdAt;

    public Venda(String id, String nomeCliente, String nomeVendedor, String modeloCarro, double valor, Instant createdAt) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.nomeVendedor = nomeVendedor;
        this.modeloCarro = modeloCarro;
        this.valor = valor;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return nomeCliente;
    }

    public void setIdCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getIdVendedor() {
        return nomeVendedor;
    }

    public void setIdVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }

    public String getIdCarro() {
        return modeloCarro;
    }

    public void setIdCarro(String modeloCarro) {
        this.modeloCarro = modeloCarro;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // Outros métodos conforme necessário

    @Override
    public String toString() {
        return "Venda{" +
                "id='" + id + '\'' +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", nomeVendedor='" + nomeVendedor + '\'' +
                ", modeloCarro='" + modeloCarro + '\'' +
                ", valor=" + valor +
                ", createdAt=" + createdAt +
                // Outros atributos
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Venda venda = (Venda) o;
        return Objects.equals(getId(), venda.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

