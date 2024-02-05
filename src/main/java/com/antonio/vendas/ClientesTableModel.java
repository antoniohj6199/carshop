package com.antonio.vendas;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import org.w3c.dom.events.MouseEvent;

import java.util.Comparator;
import java.util.List;

public class ClientesTableModel extends AbstractTableModel {
    private List<Cliente> clientes;
    private String[] colunas = { "ID", "Nome", "Criado em", "Atualizado em" };
    private boolean ordenacaoCrescenteNome;
    private boolean ordenacaoCrescenteID;
    private boolean ordenacaoCrescenteCriacao;
    private boolean ordenacaoCrescenteAtualizacao;
    private Comparator<Cliente> comparador;

    public ClientesTableModel(List<Cliente> clientes) {
        this.clientes = clientes;
        this.ordenacaoCrescenteNome = true;
        this.ordenacaoCrescenteCriacao = true;
        this.ordenacaoCrescenteAtualizacao = true;
        this.ordenacaoCrescenteID = true;
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

    public void ordenarPorNome(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Cliente::getNome);
        } else {
            comparador = Comparator.comparing(Cliente::getNome).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorCriacao(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Cliente::getCreatedAt);
        } else {
            comparador = Comparator.comparing(Cliente::getCreatedAt).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorModificacao(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Cliente::getUpdatedAt);
        } else {
            comparador = Comparator.comparing(Cliente::getUpdatedAt).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorID(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparingInt(cliente -> cliente.getId());
        } else {
            comparador = Comparator.comparing(Cliente::getId).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }

    private void atualizarTabela() {
        // Quicksort para arrays de valores primitivos e merge sort para arrays de objetos
        // Alguns casos de teste foi possível notar que é baseado no TimSort
        // Maioria dos casos é possível notar a complexidade O(n log(n))

        clientes.sort(comparador);
        fireTableDataChanged(); // Notificar a tabela sobre as mudanças nos dados
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

    public void atualizarClientes(List<Cliente> novosClientes, int ordem, boolean reverse) {
        this.clientes = novosClientes;
        fireTableDataChanged(); 
        
        if (ordem == 0) {
            ordenarPorID(reverse);
        }else if (ordem == 1) {
            ordenarPorNome(reverse);
        }else if (ordem == 2) {
            ordenarPorCriacao(reverse);
        }else if (ordem == 3) {
            ordenarPorModificacao(reverse);
        }
    }
}
