package com.antonio.vendas;

public class Main {
    public static void main(String[] args) {
        // Inicialização e interação com o usuário
        // ...

        // Criação de objetos para manipulação de dados
        DataManager dataManager = new DataManager();

        // Operações
        dataManager.addVendedor(new Vendedor("João"));
       // dataManager.addCarro(new Carro("Modelo1", 25000));
        // ...

        // Consultas
        dataManager.listVendedores();
        //dataManager.listCarros();
        // ...

        // Filtros
        //dataManager.filterMaisVendidos();
        //dataManager.filterPorValor(30000);
        // ...
    }
}