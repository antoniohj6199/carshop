package com.antonio.vendas;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ClientesTableModel extends AbstractTableModel {
    private List<Cliente> clientes;
    private String[] colunas = {"ID", "Nome", "Criado em", "Atualizado em"};

    public ClientesTableModel(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public int getRowCount() {
        return clientes.size();
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
        Cliente cliente = clientes.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return cliente.getId();
            case 1:
                return cliente.getNome();
            case 2:
                return cliente.getCreatedAt();
            case 3:
                return cliente.getUpdatedAt();            
            default:
                return null;
        }
    }
    public void addCliente(Cliente cliente) {
        clientes.add(cliente);
        int rowIndex = clientes.size() - 1;
        fireTableRowsInserted(rowIndex, rowIndex);
    }
    public void clear() {
        int rowCount = getRowCount();
        clientes.clear();
        fireTableRowsDeleted(0, rowCount - 1);
    }
    public void atualizarClientes(List<Cliente> novosClientes) {
        this.clientes = novosClientes;
        fireTableDataChanged(); // Notifica a tabela sobre a mudança nos dados
    }
}
