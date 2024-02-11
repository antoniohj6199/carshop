package com.antonio.vendas;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class VendasTableModel extends AbstractTableModel {
    private List<Venda> vendas;
    private String[] colunas = {"ID", "Cliente", "Vendedor", "Carro", "Valor", "Data da Venda"};

    public VendasTableModel(List<Venda> vendas) {
        this.vendas = vendas;
    }

    @Override
    public int getRowCount() {
        return vendas.size();
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
        Venda venda = vendas.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return venda.getId();
            case 1:
                return venda.getIdCliente();
            case 2:
                return venda.getIdVendedor();
            case 3:
                return venda.getIdCarro();
            case 4:
                return venda.getValor();
            case 5:
                return venda.getCreatedAt();
            default:
                return null;
        }
    }
}

