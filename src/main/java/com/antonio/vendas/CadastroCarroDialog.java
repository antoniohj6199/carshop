package com.antonio.vendas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CadastroCarroDialog extends JDialog {

    private JTextField idTextField;
    private JTextField idClienteTextField;
    private JTextField idVendedorTextField;
    private JTextField modeloTextField;
    private JTextField corTextField;
    private JTextField precoTextField;
    private JCheckBox disponivelCheckBox;
    private JFrame parent;
    private DataManager dataManager;
    private Carro carro;

    public CadastroCarroDialog(JFrame parent, DataManager dataManager, Carro carro) {
        super(parent, "Cadastro de Carro", true);
        this.parent = parent;
        this.dataManager = dataManager;
        this.carro = carro;

        JLabel idLabel = new JLabel("ID:");
        JLabel idClienteLabel = new JLabel("ID Cliente:");
        JLabel idVendedorLabel = new JLabel("ID Vendedor:");
        JLabel modeloLabel = new JLabel("Modelo:");
        JLabel corLabel = new JLabel("Cor:");
        JLabel precoLabel = new JLabel("Preço:");
        JLabel disponivelLabel = new JLabel("Disponível:");

        idTextField = new JTextField(20);
        idClienteTextField = new JTextField(20);
        idVendedorTextField = new JTextField(20);
        modeloTextField = new JTextField(20);
        corTextField = new JTextField(20);
        precoTextField = new JTextField(20);
        disponivelCheckBox = new JCheckBox();
        idTextField.setText("-1");
        if(carro != null){
            idTextField.setText(carro.getId());
            idClienteTextField.setText(carro.getIdCliente());
            idVendedorTextField.setText(carro.getIdVendedor());
            modeloTextField.setText(carro.getModelo());
            corTextField.setText(carro.getCor());
            precoTextField.setText(carro.getPreco()+"");
            disponivelCheckBox.setSelected(carro.isDisponivel());
        }

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(idClienteLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(idClienteTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(idVendedorLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(idVendedorTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(modeloLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(modeloTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(corLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(corTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(precoLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(precoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(disponivelLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(disponivelCheckBox, gbc);

        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenha os valores dos campos
                String id = idTextField.getText();
                String idCliente = idClienteTextField.getText();
                String idVendedor = idVendedorTextField.getText();
                String modelo = modeloTextField.getText();
                String cor = corTextField.getText();
                double preco = Double.parseDouble(precoTextField.getText());
                boolean disponivel = disponivelCheckBox.isSelected();

                // Crie um objeto Carro com os valores
                Carro novoCarro = new Carro(id, idCliente, idVendedor, modelo, cor, preco, disponivel,
                        obterDataAtualFormatada(), obterDataAtualFormatada());

                // Adicione o novo carro ao DataManager
                // Suponho que você tenha um método addCarro em seu DataManager
                dataManager.addCarro(novoCarro);

                // Feche o diálogo
                dispose();
            }
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Feche o diálogo sem fazer nada
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

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    public String obterDataAtualFormatada() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return agora.format(formatter);
    }
}
