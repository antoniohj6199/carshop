package com.antonio.vendas;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String idCliente;
    private String idVendedor;
    private String idCarro;
    private double valor;
    private Instant createdAt;

    public Venda(String id, String idCliente, String idVendedor, String idCarro, double valor, Instant createdAt) {
        this.id = id;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.idCarro = idCarro;
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

    public String getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(String idCarro) {
        this.idCarro = idCarro;
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
                ", idCliente='" + idCliente + '\'' +
                ", idVendedor='" + idVendedor + '\'' +
                ", idCarro='" + idCarro + '\'' +
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

