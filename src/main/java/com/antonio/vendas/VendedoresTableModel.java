package com.antonio.vendas;

import javax.swing.table.AbstractTableModel;

import java.util.Comparator;
import java.util.List;

public class VendedoresTableModel extends AbstractTableModel {
    private List<Vendedor> vendedores;
    private String[] colunas = {"ID", "Nome", "Criado em", "Atualizado em"};
    private boolean ordenacaoCrescenteNome;
    private Comparator<Vendedor> comparador;

    public VendedoresTableModel(List<Vendedor> vendedores) {
        this.vendedores = vendedores;
    }

    @Override
    public int getRowCount() {
        return vendedores.size();
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
        Vendedor vendedor = vendedores.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return vendedor.getId();
            case 1:
                return vendedor.getNome();
            case 2:
                return vendedor.getCreatedAt();
            case 3:
                return vendedor.getUpdatedAt();
            // Adicione mais cases conforme necessário para outras colunas
            default:
                return null;
        }
    }
    public void addVendedor(Vendedor vendedor) {
        vendedores.add(vendedor);
        int rowIndex = vendedores.size() - 1;
        fireTableRowsInserted(rowIndex, rowIndex);
    }
    public void clear() {
        int rowCount = getRowCount();
        vendedores.clear();
        fireTableRowsDeleted(0, rowCount - 1);
    }
    public void ordenarPorNome(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Vendedor::getNome);
        } else {
            comparador = Comparator.comparing(Vendedor::getNome).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorCriacao(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Vendedor::getCreatedAt);
        } else {
            comparador = Comparator.comparing(Vendedor::getCreatedAt).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorModificacao(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Vendedor::getUpdatedAt);
        } else {
            comparador = Comparator.comparing(Vendedor::getUpdatedAt).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorID(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparingInt(vendedor -> vendedor.getId());
        } else {
            comparador = Comparator.comparing(Vendedor::getId).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    private void atualizarTabela() {
        vendedores.sort(comparador);
        fireTableDataChanged(); // Notificar a tabela sobre as mudanças nos dados
    }
    public void atualizarVendedores(List<Vendedor> novosVendedores, int ordem, boolean reverse) {
        this.vendedores = novosVendedores;
        fireTableDataChanged(); // Notifica a tabela sobre a mudança nos dados
        
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
