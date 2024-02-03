package com.antonio.vendas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CadastroVendedorDialog extends JDialog {

    private JTextField nomeTextField;
    private JFrame parent;
    private DataManager dataManager;
    private int idVendedor;
    private String nomeVendedor;

    public CadastroVendedorDialog(JFrame parent, DataManager dataManager, int idVendedor, String nomeVendedor) {
        super(parent, "Cadastro de Vendedor", true);
        this.parent = parent;
        this.dataManager = dataManager;
        this.idVendedor = idVendedor;
        this.nomeVendedor = nomeVendedor;
        
        JLabel nomeLabel = new JLabel("Nome:");
        nomeTextField = new JTextField(20);
        nomeTextField.setText(nomeVendedor);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nomeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(nomeTextField, gbc);
        
        JButton confirmButton = new JButton("Confirmar");
        
        
        JButton cancelButton = new JButton("Cancelar");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {       
                String novoNome = nomeTextField.getText();    
                dataManager.addVendedor(new Vendedor(idVendedor, novoNome, obterDataAtualFormatada(), obterDataAtualFormatada()));                     
                dispose(); 
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
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
    public String obterDataAtualFormatada() {
        // Obtém a data e hora atuais
        LocalDateTime agora = LocalDateTime.now();

        // Define o formato desejado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        // Formata a data e hora conforme o formato definido
        return agora.format(formatter);
    }
}
