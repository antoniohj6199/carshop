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

public class PainelVendedores extends JPanel {
    private JTextField buscaTextFieldVendedor;
    private JTextField buscaTextFieldIDVendedor;
    private JTable vendedoresTable;
    private DataManager dataManager;
    private int paginaAtualVendedor = 1;
    int indice = 0;
    private int ordem = 0;
    private int pageSize = 10;

    public PainelVendedores(DataManager dataManager) {
        this.dataManager = dataManager;
        this.ordem = 0; // Defina a ordem padrão conforme necessário

        setLayout(new BorderLayout());

        // Criação do campo de busca
        buscaTextFieldVendedor = new JTextField(20);
        buscaTextFieldVendedor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarVendedores(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarVendedores(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarVendedores(ordem, false);
            }
        });

        buscaTextFieldIDVendedor = new JTextField(10);
        buscaTextFieldIDVendedor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarVendedores(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarVendedores(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarVendedores(ordem, false);
            }
        });

        JLabel buscaLabel = new JLabel("Buscar Vendedor:");
        JLabel buscaIDLabel = new JLabel("Buscar por ID:");

        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.add(buscaLabel);
        buscaPanel.add(buscaTextFieldVendedor);
        buscaPanel.add(buscaIDLabel);
        buscaPanel.add(buscaTextFieldIDVendedor);

        add(buscaPanel, BorderLayout.NORTH);

        // Criação dos botões de paginação
        JButton paginaAnteriorButton = new JButton("Página Anterior");
        JButton proximaPaginaButton = new JButton("Próxima Página");

        paginaAnteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousPageVendedor();
            }
        });

        proximaPaginaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPageVendedor();
            }
        });

        JPanel botoesPaginacaoPanel = new JPanel();
        botoesPaginacaoPanel.add(paginaAnteriorButton);
        botoesPaginacaoPanel.add(proximaPaginaButton);

        // Criação da tabela de vendedores
        vendedoresTable = new JTable(new VendedoresTableModel(new ArrayList<Vendedor>()));
        JScrollPane scrollPane = new JScrollPane(vendedoresTable);
        JTableHeader header = vendedoresTable.getTableHeader();
        header.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int colunaClicada = vendedoresTable.columnAtPoint(e.getPoint());
                ordem = colunaClicada;
                filtrarVendedores(ordem, true);
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

        JButton cadastrarButton = new JButton("Cadastrar Vendedor");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroVendedorDialog cadastroDialog = new CadastroVendedorDialog(
                        (JFrame) SwingUtilities.getWindowAncestor(PainelVendedores.this), dataManager, -1, "");
                cadastroDialog.setVisible(true);
                filtrarVendedores(ordem, true);
            }
        });

        JButton editarButton = new JButton("Editar Vendedor");
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = vendedoresTable.getSelectedRow();
                if (selectedRow != -1) {
                    Object idS = vendedoresTable.getValueAt(selectedRow, 0);
                    Object nomeS = vendedoresTable.getValueAt(selectedRow, 1);

                    CadastroVendedorDialog cadastroDialog = new CadastroVendedorDialog(
                            (JFrame) SwingUtilities.getWindowAncestor(PainelVendedores.this), dataManager,
                            Integer.parseInt((idS + "")), nomeS + "");
                    cadastroDialog.setVisible(true);
                    filtrarVendedores(ordem, true);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton excluirButton = new JButton("Excluir Vendedor");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = vendedoresTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmar = JOptionPane.showConfirmDialog(null, "Você tem certeza dessa exclusão?",
                            "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmar == JOptionPane.YES_OPTION) {
                        Object idS = vendedoresTable.getValueAt(selectedRow, 0);
                        excluirVendedor(Integer.parseInt(idS + ""));
                    }
                    filtrarVendedores(ordem, true);
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
        filtrarVendedores(ordem, false);
    }

    private void filtrarVendedores(int ordem, boolean reverse) {
        String termoBusca = buscaTextFieldVendedor.getText().toLowerCase();
        String termoBuscaID = buscaTextFieldIDVendedor.getText().toLowerCase();
        if (termoBusca.isEmpty()) {
            if (termoBuscaID.isEmpty()) {
                termoBusca = "%";
                System.out.println(paginaAtualVendedor);
            } else {
                termoBusca = termoBuscaID;
            }

        }
        if (termoBusca.length() > 0) {
            List<Vendedor> vendedoresFiltrados = new ArrayList<>();

            // Calcule o índice de início com base na página atual e no tamanho da página
            int startIdx = (paginaAtualVendedor - 1) * pageSize;
            indice = 0;
            filtrarVendedoresRecursivo(dataManager.getArvoreVendedor().getRaiz(), termoBusca, vendedoresFiltrados,
                    startIdx, pageSize);

            ((VendedoresTableModel) vendedoresTable.getModel()).atualizarVendedores(vendedoresFiltrados, ordem,
                    reverse);

            int totalPages = (int) Math.ceil((double) vendedoresFiltrados.size() / pageSize);
            int currentPage = Math.min(paginaAtualVendedor, totalPages);

        }
    }

    private void filtrarVendedoresRecursivo(NoArvoreVendedor no, String termoBusca, List<Vendedor> vendedoresFiltrados,
            int startIdx, int pageSize) {
        // Se o nó não é nulo
        if (no != null) {
            for (Vendedor vendedor : no.getChaves()) {
                if (termoBusca.equals("%")) {
                    if (indice >= startIdx && vendedoresFiltrados.size() < pageSize) {
                        if (!vendedoresFiltrados.contains(vendedor)) {
                            vendedoresFiltrados.add(vendedor);
                        }
                    }

                } else {
                    if (vendedor.getNome().toLowerCase().contains(termoBusca)
                            || (vendedor.getId() + "").equals(termoBusca)) {
                        if (indice >= startIdx && vendedoresFiltrados.size() < pageSize) {
                            if (!vendedoresFiltrados.contains(vendedor)) {
                                vendedoresFiltrados.add(vendedor);
                            }
                        }

                    }
                }
                indice++;
                // System.out.println(indice+" "+startIdx+" "+vendedoresFiltrados.size()+"
                // "+vendedor.getNome()+" "+vendedor.getId());

            }

            for (NoArvoreVendedor filho : no.getFilhos()) {
                if (vendedoresFiltrados.size() < pageSize) {
                    filtrarVendedoresRecursivo(filho, termoBusca, vendedoresFiltrados, startIdx, pageSize);
                }
            }
        }
    }

    private void nextPageVendedor() {
        paginaAtualVendedor++;
        filtrarVendedores(ordem, false);
    }

    private void previousPageVendedor() {
        paginaAtualVendedor = Math.max(1, paginaAtualVendedor - 1);
        filtrarVendedores(ordem, false);
    }

    private void excluirVendedor(int idVendedor) {
        dataManager.excluirVendedorID(idVendedor);
        System.out.println(idVendedor);
    }
}
