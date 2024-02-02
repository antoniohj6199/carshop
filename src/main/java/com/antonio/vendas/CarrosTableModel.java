package com.antonio.vendas;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CarrosTableModel extends AbstractTableModel {
    private List<Carro> carros;
    private String[] colunas = {"ID", "ID Cliente", "ID Vendedor", "Modelo", "Cor", "Preço", "Disponível", "Criado em", "Atualizado em"};

    public CarrosTableModel(List<Carro> carros) {
        this.carros = carros;
    }

    @Override
    public int getRowCount() {
        return carros.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Carro carro = carros.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return carro.getId();
            case 1:
                return carro.getIdCliente();
            case 2:
                return carro.getIdVendedor();
            case 3:
                return carro.getModelo();
            case 4:
                return carro.getCor();
            case 5:
                return carro.getPreco();
            case 6:
                return carro.isDisponivel() ? "Sim" : "Não";
            case 7:
                return carro.getCreatedAt();
            case 8:
                return carro.getUpdatedAt();
            // Adicione mais cases conforme necessário para outras colunas
            default:
                return null;
        }
    }
}
