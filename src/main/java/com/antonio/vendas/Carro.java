package com.antonio.vendas;

import java.io.Serializable;

public class Carro implements Serializable {
    private static final long serialVersionUID = 1L;

    private String modelo;
    private double valor;
    // Outros atributos relevantes para um carro, como ID, ano de fabricação, etc.

    public Carro(String modelo, double valor) {
        this.modelo = modelo;
        this.valor = valor;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    // Outros métodos conforme necessário

    @Override
    public String toString() {
        return "Carro{" +
                "modelo='" + modelo + '\'' +
                ", valor=" + valor +
                // Outros atributos
                '}';
    }
}
