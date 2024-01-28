package com.antonio.vendas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VendasGUI extends JFrame {

    private List<Cliente> clientes;
    private List<Vendedor> vendedores;
    private List<Carro> carros;
    private List<Venda> vendas;

    private JPanel clientePanel;
    private JPanel vendedorPanel;
    private JPanel carroPanel;
    private JPanel vendaPanel;

    public VendasGUI() {
        // Inicialize seus dados (pode ser carregado a partir do seu DataManager)
        clientes = new ArrayList<>();
        vendedores = new ArrayList<>();
        carros = new ArrayList<>();
        vendas = new ArrayList<>();

        // Configurações da janela principal
        setTitle("Sistema de Vendas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criação dos painéis
        clientePanel = criarClientePanel();
        vendedorPanel = criarVendedorPanel();
        carroPanel = criarCarroPanel();
        vendaPanel = criarVendaPanel();

        // Adiciona os painéis à janela principal
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Clientes", clientePanel);
        tabbedPane.addTab("Vendedores", vendedorPanel);
        tabbedPane.addTab("Carros", carroPanel);
        tabbedPane.addTab("Vendas", vendaPanel);

        add(tabbedPane);
    }

    private JPanel criarClientePanel() {
        JPanel panel = new JPanel();
        // Adicione componentes relacionados aos clientes aqui
        return panel;
    }

    private JPanel criarVendedorPanel() {
        JPanel panel = new JPanel();
        // Adicione componentes relacionados aos vendedores aqui
        return panel;
    }

    private JPanel criarCarroPanel() {
        JPanel panel = new JPanel();
        // Adicione componentes relacionados aos carros aqui
        return panel;
    }

    private JPanel criarVendaPanel() {
        JPanel panel = new JPanel();
        // Adicione componentes relacionados às vendas aqui
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VendasGUI().setVisible(true);
            }
        });
    }
}
