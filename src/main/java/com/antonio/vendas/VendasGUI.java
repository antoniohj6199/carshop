package com.antonio.vendas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class VendasGUI extends JFrame {

    private List<Venda> vendas;
    private JPanel clientePanel;
    private JPanel vendedorPanel;
    private JPanel carroPanel;
    private JPanel vendaPanel;
    private JPanel relatorioPanel;

    JTextField buscaTextField;
    JTextField buscaTextFieldID;
    JTextField buscaTextFieldVendedor;
    JTextField buscaTextFieldIDVendedor;
    DataManager dataManager;

    int indice = 0;

    public VendasGUI() {

        dataManager = new DataManager();
        vendas = dataManager.getVendas();

        // Configurações da janela principal
        setTitle("Sistema de Vendas");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criação dos painéis
        clientePanel = new PainelClientes(dataManager);
        vendedorPanel = new PainelVendedores(dataManager);
        carroPanel = new PainelCarros(dataManager);
        vendaPanel = criarVendaPanel();
        relatorioPanel = criarRelatorioPanel();
        
        // Adiciona os painéis à janela principal
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Clientes", clientePanel);
        tabbedPane.addTab("Vendedores", vendedorPanel);
        tabbedPane.addTab("Carros", carroPanel);
        tabbedPane.addTab("Vendas", vendaPanel);
        tabbedPane.addTab("Vendas", vendaPanel);
        tabbedPane.addTab("Analytics", relatorioPanel);

        add(tabbedPane);
    }    

    private JPanel criarRelatorioPanel() {
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        JLabel tituloLabel = new JLabel("Relatório de Vendas");
        panel.add(tituloLabel);

        int quantidadeTotalVendas = vendas.size();
        JLabel quantidadeVendasLabel = new JLabel("Quantidade Total de Vendas: " + quantidadeTotalVendas);
        panel.add(quantidadeVendasLabel);

        double valorTotalVendas = calcularTotalVendas();
        JLabel totalVendasLabel = new JLabel("Valor Total de Vendas: " + valorTotalVendas);
        panel.add(totalVendasLabel);

        double mediaValor = calcularMediaValor();
        JLabel mediaValorLabel = new JLabel("Média de Valor das Vendas: " + mediaValor);
        panel.add(mediaValorLabel);

        double desvioPadrao = calcularDesvioPadrao();
        String desvioPadraoFormatado = String.format("%.3f", desvioPadrao);
        JLabel desvioPadraoLabel = new JLabel("Desvio Padrão de Valor das Vendas: " + desvioPadraoFormatado);
        panel.add(desvioPadraoLabel);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quantidadeVendasLabel.setText("Quantidade Total de Vendas: " + vendas.size());
                totalVendasLabel.setText("Valor Total de Vendas: " + calcularTotalVendas());
                mediaValorLabel.setText("Média de Valor das Vendas: " + calcularMediaValor());
                desvioPadraoLabel
                        .setText("Desvio Padrão de Valor das Vendas: " + String.format("%.3f", calcularDesvioPadrao()));
            }
        });
        panel.add(refreshButton);

        return panel;
    }

    private double calcularTotalVendas() {
        if (vendas.isEmpty()) {
            return 0.0;
        }

        double somaValores = 0.0;
        for (Venda venda : vendas) {
            somaValores += venda.getValor();
        }

        return somaValores;
    }

    private double calcularMediaValor() {
        if (vendas.isEmpty()) {
            return 0.0;
        }

        double somaValores = 0.0;
        for (Venda venda : vendas) {
            somaValores += venda.getValor();
        }

        return somaValores / vendas.size();
    }

    private double calcularDesvioPadrao() {
        double mediaValor = calcularMediaValor();

        double somaQuadradosDiferencas = 0;
        for (Venda venda : vendas) {
            double diferenca = venda.getValor() - mediaValor;
            somaQuadradosDiferencas += diferenca * diferenca;
        }

        double mediaQuadradosDiferencas = somaQuadradosDiferencas / vendas.size();

        return Math.sqrt(mediaQuadradosDiferencas);
    }      
   
    
    private JPanel criarVendaPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Criação da tabela de vendas
        JTable vendasTable = new JTable(new VendasTableModel(vendas));
        JScrollPane scrollPane = new JScrollPane(vendasTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Criação dos botões
        JButton cadastrarButton = new JButton("Cadastrar Venda");
        JButton editarButton = new JButton("Editar Venda");
        JButton excluirButton = new JButton("Excluir Venda");

        // Adição dos botões ao painel
        JPanel botoesPanel = new JPanel();
        botoesPanel.add(cadastrarButton);
        // botoesPanel.add(editarButton);
        // botoesPanel.add(excluirButton);
        panel.add(botoesPanel, BorderLayout.SOUTH);

        // Adiciona listeners aos botões
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroVendaDialog cadastroDialog = new CadastroVendaDialog(VendasGUI.this, dataManager);
                cadastroDialog.setVisible(true);
                vendasTable.setModel(new VendasTableModel(vendas));
                relatorioPanel = criarRelatorioPanel();
                repaint();
                validate();
            }
        });

        return panel;
    }

}
