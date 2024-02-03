package com.antonio.vendas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.List;

public class CadastroVendaDialog extends JDialog {

    private JComboBox<String> clientesComboBox;
    private JComboBox<String> vendedoresComboBox;
    private JComboBox<String> carrosComboBox;
    private JTextField valorTextField;

    public CadastroVendaDialog(JFrame parent, DataManager dataManager) {
        super(parent, "Cadastro de Venda", true);

        // Obter listas de clientes, vendedores e carros do DataManager
        List<Cliente> clientes = dataManager.getClientes();
        List<Vendedor> vendedores = dataManager.getVendedores();
        List<Carro> carros = dataManager.getCarros();

        // Preencher arrays de nomes para os ComboBoxes
        String[] nomesClientes = clientes.stream().map(Cliente::getNome).toArray(String[]::new);
        String[] nomesVendedores = vendedores.stream().map(Vendedor::getNome).toArray(String[]::new);
        String[] modelosCarros = carros.stream().map(Carro::getModelo).toArray(String[]::new);

        JLabel clienteLabel = new JLabel("Cliente:");
        JLabel vendedorLabel = new JLabel("Vendedor:");
        JLabel carroLabel = new JLabel("Carro:");
        JLabel valorLabel = new JLabel("Valor:");

        clientesComboBox = new JComboBox<>(nomesClientes);
        vendedoresComboBox = new JComboBox<>(nomesVendedores);
        carrosComboBox = new JComboBox<>(modelosCarros);
        valorTextField = new JTextField(20);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(clienteLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(clientesComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(vendedorLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(vendedoresComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(carroLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(carrosComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(valorLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        valorTextField.setMinimumSize(new Dimension(200, valorTextField.getPreferredSize().height));
        formPanel.add(valorTextField, gbc);

        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obter os valores selecionados nos ComboBoxes e no TextField
                String nomeCliente = (String) clientesComboBox.getSelectedItem();
                String nomeVendedor = (String) vendedoresComboBox.getSelectedItem();
                String modeloCarro = (String) carrosComboBox.getSelectedItem();
                double valor = Double.parseDouble(valorTextField.getText());

                // Obter os objetos Cliente, Vendedor e Carro correspondentes aos nomes selecionados
                Cliente cliente = dataManager.getClienteByNome(nomeCliente);
                Vendedor vendedor = dataManager.getVendedorByNome(nomeVendedor);
                Carro carro = dataManager.getCarroByModelo(modeloCarro);

                // Criar um objeto Venda com os valores
                Venda novaVenda = new Venda("-1", cliente.getNome(), vendedor.getNome(), carro.getModelo(), valor, Instant.now());

                // Adicionar a nova venda ao DataManager
                dataManager.addVenda(novaVenda);

                // Fechar o diálogo
                dispose();
            }
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fechar o diálogo sem fazer nada
                dispose();
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(formPanel, BorderLayout.CENTER);
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);

        setSize(400, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
}
