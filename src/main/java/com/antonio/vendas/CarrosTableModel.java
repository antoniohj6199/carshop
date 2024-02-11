package com.antonio.vendas;

import javax.swing.table.AbstractTableModel;

import java.util.Comparator;
import java.util.List;

public class CarrosTableModel extends AbstractTableModel {
    private List<Carro> carros;
    private String[] colunas = {"ID", "ID Cliente", "ID Vendedor", "Modelo", "Cor", "Preço", "Disponível", "Criado em", "Atualizado em"};
    private boolean ordenacaoCrescenteNome;
    private Comparator<Carro> comparador;
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
    public void atualizarCarros(List<Carro> novosCarros, int ordem, boolean reverse) {
        this.carros = novosCarros;
        fireTableDataChanged(); 
        
        if (ordem == 0) {
            ordenarPorID(reverse);
        }else if (ordem == 1) {
            ordenarPorModelo(reverse);
        }
        else if (ordem == 2) {
            ordenarPorModelo(reverse);
        }else if (ordem == 3) {
            ordenarPorModelo(reverse);
        }
    }
    public void ordenarPorID(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(carro -> carro.getId());
        } else {
            comparador = Comparator.comparing(Carro::getId).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorModelo(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Carro::getModelo);
        } else {
            comparador = Comparator.comparing(Carro::getModelo).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    private void atualizarTabela() {
        carros.sort(comparador);
        fireTableDataChanged(); 
    }
}
