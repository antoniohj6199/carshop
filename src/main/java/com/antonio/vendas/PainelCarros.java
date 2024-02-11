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

public class PainelCarros extends JPanel {
    private JTextField buscaTextFieldCarro;
    private JTextField buscaTextFieldIDCarro;
    private JTable carrosTable;
    private DataManager dataManager;
    private int paginaAtualCarro = 1;
    int indice = 0;
    private int ordem = 0;
    private int pageSize = 10;

    public PainelCarros(DataManager dataManager) {
        this.dataManager = dataManager;
        this.ordem = 0; // Defina a ordem padrão conforme necessário

        setLayout(new BorderLayout());

        // Criação do campo de busca
        buscaTextFieldCarro = new JTextField(20);
        buscaTextFieldCarro.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarCarros(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarCarros(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarCarros(ordem, false);
            }
        });

        buscaTextFieldIDCarro = new JTextField(10);
        buscaTextFieldIDCarro.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarCarros(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarCarros(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarCarros(ordem, false);
            }
        });

        JLabel buscaLabel = new JLabel("Buscar Carro:");
        JLabel buscaIDLabel = new JLabel("Buscar por ID:");

        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.add(buscaLabel);
        buscaPanel.add(buscaTextFieldCarro);
        buscaPanel.add(buscaIDLabel);
        buscaPanel.add(buscaTextFieldIDCarro);

        add(buscaPanel, BorderLayout.NORTH);

        // Criação dos botões de paginação
        JButton paginaAnteriorButton = new JButton("Página Anterior");
        JButton proximaPaginaButton = new JButton("Próxima Página");

        paginaAnteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousPageCarro();
            }
        });

        proximaPaginaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPageCarro();
            }
        });

        JPanel botoesPaginacaoPanel = new JPanel();
        botoesPaginacaoPanel.add(paginaAnteriorButton);
        botoesPaginacaoPanel.add(proximaPaginaButton);

        // Criação da tabela de carros
        carrosTable = new JTable(new CarrosTableModel(new ArrayList<Carro>()));
        JScrollPane scrollPane = new JScrollPane(carrosTable);
        JTableHeader header = carrosTable.getTableHeader();
        header.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int colunaClicada = carrosTable.columnAtPoint(e.getPoint());
                ordem = colunaClicada;
                filtrarCarros(ordem, true);
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

        JButton cadastrarButton = new JButton("Cadastrar Carro");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroCarroDialog cadastroDialog = new CadastroCarroDialog(
                        (JFrame) SwingUtilities.getWindowAncestor(PainelCarros.this), dataManager, null);
                cadastroDialog.setVisible(true);
                filtrarCarros(ordem, true);
            }
        });

        JButton editarButton = new JButton("Editar Carro");
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = carrosTable.getSelectedRow();
                if (selectedRow != -1) {
                    Object idS = carrosTable.getValueAt(selectedRow, 0);
                    
                    Carro carro = new Carro();
                    carro.setId((int) idS);
                    CadastroCarroDialog cadastroDialog = new CadastroCarroDialog(
                            (JFrame) SwingUtilities.getWindowAncestor(PainelCarros.this), dataManager,
                            carro);
                    cadastroDialog.setVisible(true);
                    filtrarCarros(ordem, true);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton excluirButton = new JButton("Excluir Carro");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = carrosTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmar = JOptionPane.showConfirmDialog(null, "Você tem certeza dessa exclusão?",
                            "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmar == JOptionPane.YES_OPTION) {
                        Object idS = carrosTable.getValueAt(selectedRow, 0);
                        excluirCarro(Integer.parseInt(idS + ""));
                    }
                    filtrarCarros(ordem, true);
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
        filtrarCarros(ordem, false);
    }

    private void filtrarCarros(int ordem, boolean reverse) {
        String termoBusca = buscaTextFieldCarro.getText().toLowerCase();
        String termoBuscaID = buscaTextFieldIDCarro.getText().toLowerCase();
        if (termoBusca.isEmpty()) {
            if (termoBuscaID.isEmpty()) {
                termoBusca = "%";
                System.out.println(paginaAtualCarro);
            } else {
                termoBusca = termoBuscaID;
            }

        }
        if (termoBusca.length() > 0) {
            List<Carro> carrosFiltrados = new ArrayList<>();

            // Calcule o índice de início com base na página atual e no tamanho da página
            int startIdx = (paginaAtualCarro - 1) * pageSize;
            indice = 0;
            filtrarCarrosRecursivo(dataManager.getArvoreCarro().getRaiz(), termoBusca, carrosFiltrados,
                    startIdx, pageSize);

            ((CarrosTableModel) carrosTable.getModel()).atualizarCarros(carrosFiltrados, ordem,
                    reverse);

            int totalPages = (int) Math.ceil((double) carrosFiltrados.size() / pageSize);
            int currentPage = Math.min(paginaAtualCarro, totalPages);

        }
    }

    private void filtrarCarrosRecursivo(NoArvoreCarro no, String termoBusca, List<Carro> carrosFiltrados,
            int startIdx, int pageSize) {
        // Se o nó não é nulo
        if (no != null) {
            for (Carro carro : no.getChaves()) {
                if (termoBusca.equals("%")) {
                    if (indice >= startIdx && carrosFiltrados.size() < pageSize) {
                        if (!carrosFiltrados.contains(carro)) {
                            carrosFiltrados.add(carro);
                        }
                    }

                } else {
                    if (carro.getModelo().toLowerCase().contains(termoBusca)
                            || (carro.getId() + "").equals(termoBusca)) {
                        if (indice >= startIdx && carrosFiltrados.size() < pageSize) {
                            if (!carrosFiltrados.contains(carro)) {
                                carrosFiltrados.add(carro);
                            }
                        }

                    }
                }
                indice++;
                // System.out.println(indice+" "+startIdx+" "+carrosFiltrados.size()+"
                // "+carro.getNome()+" "+carro.getId());

            }

            for (NoArvoreCarro filho : no.getFilhos()) {
                if (carrosFiltrados.size() < pageSize) {
                    filtrarCarrosRecursivo(filho, termoBusca, carrosFiltrados, startIdx, pageSize);
                }
            }
        }
    }

    private void nextPageCarro() {
        paginaAtualCarro++;
        filtrarCarros(ordem, false);
    }

    private void previousPageCarro() {
        paginaAtualCarro = Math.max(1, paginaAtualCarro - 1);
        filtrarCarros(ordem, false);
    }

    private void excluirCarro(int idCarro) {
        dataManager.excluirCarroID(idCarro);
        System.out.println(idCarro);
    }
}
