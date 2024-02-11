package com.antonio.vendas;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class PainelClientes extends JPanel {
    private JTextField buscaTextField;
    private JTextField buscaTextFieldID;
    private JTable clientesTable;
    private DataManager dataManager;
    private int ordem;
    private int indice = 0;
    private int pageSize = 10;
    private int paginaAtual = 1;

    public PainelClientes(DataManager dataManager) {
        this.dataManager = dataManager;
        this.ordem = 0; // Defina a ordem padrão conforme necessário

        setLayout(new BorderLayout());

        // Criação do campo de busca
        buscaTextField = new JTextField(20);
        buscaTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarClientes(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarClientes(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarClientes(ordem, false);
            }
        });

        buscaTextFieldID = new JTextField(10);
        buscaTextFieldID.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarClientes(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarClientes(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarClientes(ordem, false);
            }
        });

        JLabel buscaLabel = new JLabel("Buscar Cliente:");
        JLabel buscaIDLabel = new JLabel("Buscar por ID:");

        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.add(buscaLabel);
        buscaPanel.add(buscaTextField);
        buscaPanel.add(buscaIDLabel);
        buscaPanel.add(buscaTextFieldID);

        add(buscaPanel, BorderLayout.NORTH);

        // Criação dos botões de paginação
        JButton paginaAnteriorButton = new JButton("Página Anterior");
        JButton proximaPaginaButton = new JButton("Próxima Página");

        paginaAnteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousPage();
            }
        });

        proximaPaginaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPage();
            }
        });

        JPanel botoesPaginacaoPanel = new JPanel();
        botoesPaginacaoPanel.add(paginaAnteriorButton);
        botoesPaginacaoPanel.add(proximaPaginaButton);

        // Criação da tabela de clientes
        clientesTable = new JTable(new ClientesTableModel(new ArrayList<Cliente>()));
        JScrollPane scrollPane = new JScrollPane(clientesTable);
        JTableHeader header = clientesTable.getTableHeader();
        header.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int colunaClicada = clientesTable.columnAtPoint(e.getPoint());
                ordem = colunaClicada;
                filtrarClientes(ordem, true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        JPanel tabelaEControlesPanel = new JPanel(new BorderLayout());
        tabelaEControlesPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaEControlesPanel.add(botoesPaginacaoPanel, BorderLayout.SOUTH);

        add(tabelaEControlesPanel, BorderLayout.CENTER);

        JButton cadastrarButton = new JButton("Cadastrar Cliente");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroClienteDialog cadastroDialog = new CadastroClienteDialog(
                        (JFrame) SwingUtilities.getWindowAncestor(PainelClientes.this), dataManager, -1, "");
                cadastroDialog.setVisible(true);
                filtrarClientes(ordem, true);
            }
        });

        JButton editarButton = new JButton("Editar Cliente");
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = clientesTable.getSelectedRow();
                if (selectedRow != -1) {
                    Object idS = clientesTable.getValueAt(selectedRow, 0);
                    Object nomeS = clientesTable.getValueAt(selectedRow, 1);

                    CadastroClienteDialog cadastroDialog = new CadastroClienteDialog(
                            (JFrame) SwingUtilities.getWindowAncestor(PainelClientes.this), dataManager,
                            Integer.parseInt((idS + "")), nomeS + "");
                    cadastroDialog.setVisible(true);
                    filtrarClientes(ordem, true);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton excluirButton = new JButton("Excluir Cliente");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = clientesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmar = JOptionPane.showConfirmDialog(null, "Você tem certeza dessa exclusão?",
                            "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmar == JOptionPane.YES_OPTION) {
                        Object idS = clientesTable.getValueAt(selectedRow, 0);
                        excluirCliente(Integer.parseInt(idS + ""));
                    }
                    filtrarClientes(ordem, true);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para excluir.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel botoesPanel = new JPanel();
        botoesPanel.add(cadastrarButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        add(botoesPanel, BorderLayout.SOUTH);
        filtrarClientes(ordem, false);
    }

    private void filtrarClientes(int ordem, boolean reverse) {
        String termoBusca = buscaTextField.getText().toLowerCase();
        String termoBuscaID = buscaTextFieldID.getText().toLowerCase();
        if (termoBusca.isEmpty()) {
            if (termoBuscaID.isEmpty()) {
                termoBusca = "%";
            } else {
                termoBusca = termoBuscaID;
            }

        }
        if (termoBusca.length() > 0) {
            List<Cliente> clientesFiltrados = new ArrayList<>();

            // Calcule o índice de início com base na página atual e no tamanho da página
            int startIdx = (paginaAtual - 1) * pageSize;

            // Chama o método na árvore para obter os clientes filtrados com paginação
            indice = 0;
            filtrarClientesRecursivo(dataManager.getArvoreCliente().getRaiz(), termoBusca, clientesFiltrados, startIdx,
                    pageSize);

            // Atualiza o modelo da tabela com os clientes filtrados
            ((ClientesTableModel) clientesTable.getModel()).atualizarClientes(clientesFiltrados, ordem, reverse);

            // Exiba as informações de paginação (por exemplo, página atual e número total
            // de páginas)
            int totalPages = (int) Math.ceil((double) clientesFiltrados.size() / pageSize);
            int currentPage = Math.min(paginaAtual, totalPages);
            // Agora você pode exibir currentPage e totalPages na sua interface do usuário
        }
    }

    private void filtrarClientesRecursivo(NoArvoreCliente no, String termoBusca, List<Cliente> clientesFiltrados,
            int startIdx, int pageSize) {
        if (no != null) {
            for (Cliente cliente : no.getChaves()) {
                if (termoBusca.equals("%")) {
                    if (indice >= startIdx && clientesFiltrados.size() < pageSize) {
                        if (!clientesFiltrados.contains(cliente)) {
                            clientesFiltrados.add(cliente);
                        }
                    }
                } else {
                    if (cliente.getNome().toLowerCase().contains(termoBusca)
                            || (cliente.getId() + "").equals(termoBusca)) {
                        if (indice >= startIdx && clientesFiltrados.size() < pageSize) {
                            if (!clientesFiltrados.contains(cliente)) {
                                clientesFiltrados.add(cliente);
                            }
                        }

                    }
                }
                indice++;
                // System.out.println(indice+" "+startIdx+" "+clientesFiltrados.size()+"
                // "+cliente.getNome()+" "+cliente.getId());
            }

            for (NoArvoreCliente filho : no.getFilhos()) {
                if (clientesFiltrados.size() < pageSize) {
                    filtrarClientesRecursivo(filho, termoBusca, clientesFiltrados, startIdx, pageSize);
                }
            }
        }
    }

    private void previousPage() {
        paginaAtual = Math.max(1, paginaAtual - 1);
        filtrarClientes(ordem, false);
    }

    private void nextPage() {
        paginaAtual++;
        filtrarClientes(ordem, false);
    }

    private void excluirCliente(int idCliente) {
        dataManager.excluirClienteID(idCliente);
        System.out.println(idCliente);
    }
}
