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
            ordenarPorIDCliente(reverse);
        }
        else if (ordem == 2) {
            ordenarPorIDVendedor(reverse);
        }else if (ordem == 3) {
            ordenarPorModelo(reverse);
        }
        else if (ordem == 3) {
            ordenarPorCor(reverse);
        }
        else if (ordem == 3) {
            ordenarPorPreco(reverse);
        }
        else if (ordem == 3) {
            ordenarPorDisponivel(reverse);
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
    public void ordenarPorIDCliente(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(carro -> carro.getIdCliente());
        } else {
            comparador = Comparator.comparing(Carro::getIdCliente).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorIDVendedor(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(carro -> carro.getIdVendedor());
        } else {
            comparador = Comparator.comparing(Carro::getIdVendedor).reversed();
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
    public void ordenarPorDisponivel(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Carro::isDisponivel);
        } else {
            comparador = Comparator.comparing(Carro::isDisponivel).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorPreco(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Carro::getPreco);
        } else {
            comparador = Comparator.comparing(Carro::getPreco).reversed();
        }
        if (reverte){
            ordenacaoCrescenteNome = !ordenacaoCrescenteNome;
        }            
        atualizarTabela();
    }
    public void ordenarPorCor(boolean reverte) {
        if (ordenacaoCrescenteNome) {
            comparador = Comparator.comparing(Carro::getCor);
        } else {
            comparador = Comparator.comparing(Carro::getCor).reversed();
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
