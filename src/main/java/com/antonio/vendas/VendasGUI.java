package com.antonio.vendas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;

public class VendasGUI extends JFrame {

    private List<Cliente> clientes;
    private List<Vendedor> vendedores;
    private List<Carro> carros;
    private List<Venda> vendas;

    private JPanel clientePanel;
    private JPanel vendedorPanel;
    private JPanel carroPanel;
    private JPanel vendaPanel;
    private JPanel relatorioPanel;

    private JTable clientesTable;
    private JTable vendedoresTable;
    JTextField buscaTextField;
    JTextField buscaTextFieldID;
    JTextField buscaTextFieldVendedor;
    JTextField buscaTextFieldIDVendedor;
    DataManager dataManager;

    private int pageSize = 10;
    private int paginaAtual = 1;
    private int paginaAtualVendedor = 1;

    private int ordem = 0;

    public VendasGUI() {
        // Inicialize seus dados (pode ser carregado a partir do seu DataManager)

        dataManager = new DataManager();
        vendedores = dataManager.getVendedores();
        carros = dataManager.getCarros();
        vendas = dataManager.getVendas();
        clientes = dataManager.getClientes();

        // Configurações da janela principal
        setTitle("Sistema de Vendas");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criação dos painéis
        clientePanel = criarClientePanel();
        vendedorPanel = criarVendedorPanel();
        carroPanel = criarCarroPanel();
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

    private void atualizaTableClientes() {
        // Obtém o modelo da tabela
        ClientesTableModel model = (ClientesTableModel) clientesTable.getModel();

        // Limpa os dados existentes na tabela
        model.clear();

        // Adiciona os novos dados (clientes) ao modelo
        for (Cliente cliente : clientes) {
            model.addCliente(cliente);
        }

        // Notifica a tabela que os dados foram alterados
        model.fireTableDataChanged();
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

    private JPanel criarClientePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Criação do campo de busca
        buscaTextField = new JTextField(20);
        buscaTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                paginaAtual = 1;
                filtrarClientes(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                paginaAtual = 1;
                filtrarClientes(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                paginaAtual = 1;
                filtrarClientes(ordem, false);
            }
        });

        // Criação do painel de busca
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel buscaLabel = new JLabel("Buscar Cliente:");

        JLabel buscaIDLabel = new JLabel("Buscar por ID:");
        buscaTextFieldID = new JTextField(10);
        buscaTextFieldID.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                paginaAtual = 1;
                filtrarClientes(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                paginaAtual = 1; 
                filtrarClientes(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                paginaAtual = 1;
                filtrarClientes(ordem, false);
            }
        });
        buscaPanel.add(buscaLabel);
        buscaPanel.add(buscaTextField);
        buscaPanel.add(buscaIDLabel);
        buscaPanel.add(buscaTextFieldID);

        // Adiciona o campo de busca ao painel
        panel.add(buscaPanel, BorderLayout.NORTH);

        // Criação dos botões de paginação
        JButton paginaAnteriorButton = new JButton("Página Anterior");
        JButton proximaPaginaButton = new JButton("Próxima Página");

        // Adiciona listeners aos botões de paginação
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

        // Criação de um painel para os botões de paginação
        JPanel botoesPaginacaoPanel = new JPanel();
        botoesPaginacaoPanel.add(paginaAnteriorButton);
        botoesPaginacaoPanel.add(proximaPaginaButton);

        // Criação da tabela de clientes
        clientesTable = new JTable(new ClientesTableModel(new ArrayList<Cliente>()));
        filtrarClientes(ordem, false);
        JScrollPane scrollPane = new JScrollPane(clientesTable);
        JTableHeader header = clientesTable.getTableHeader();
        header.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int colunaClicada = clientesTable.columnAtPoint(e.getPoint());
                ordem = colunaClicada;
                filtrarClientes(ordem, true);
                // ordenarPorColuna(colunaClicada);
                // System.out.println(colunaClicada);
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
        // Criação do painel principal que contém a tabela e os botões
        JPanel tabelaEControlesPanel = new JPanel(new BorderLayout());
        tabelaEControlesPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaEControlesPanel.add(botoesPaginacaoPanel, BorderLayout.SOUTH);

        // Adiciona o painel principal ao centro do layout
        panel.add(tabelaEControlesPanel, BorderLayout.CENTER);

        // Criação dos botões
        JButton cadastrarButton = new JButton("Cadastrar Cliente");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroClienteDialog cadastroDialog = new CadastroClienteDialog(VendasGUI.this, dataManager, -1, "");
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

                    CadastroClienteDialog cadastroDialog = new CadastroClienteDialog(VendasGUI.this, dataManager,
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
        // Adição dos botões ao painel
        JPanel botoesPanel = new JPanel();
        botoesPanel.add(cadastrarButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        panel.add(botoesPanel, BorderLayout.SOUTH);

        return panel;
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

            int indice = 0;

            for (Cliente cliente : clientes) {
                if (termoBusca.equals("%")) {
                    if (indice >= startIdx && clientesFiltrados.size() < pageSize) {
                        if (!clientesFiltrados.contains(cliente)) {
                            clientesFiltrados.add(cliente);
                        }
                    }
                    indice++;
                } else {
                    if (cliente.getNome().toLowerCase().contains(termoBusca)
                            || (String.valueOf(cliente.getId())).equals(termoBusca)) {
                        if (indice >= startIdx && clientesFiltrados.size() < pageSize) {
                            if (!clientesFiltrados.contains(cliente)) {
                                clientesFiltrados.add(cliente);
                            }
                        }
                        indice++;
                    }
                }
            }

            // Atualiza o modelo da tabela com os clientes filtrados
            ((ClientesTableModel) clientesTable.getModel()).atualizarClientes(clientesFiltrados, ordem, reverse);

            // Exiba as informações de paginação (por exemplo, página atual e número total
            // de páginas)
            int totalPages = (int) Math.ceil((double) clientesFiltrados.size() / pageSize);
            int currentPage = Math.min(paginaAtual, totalPages);
            // Agora você pode exibir currentPage e totalPages na sua interface do usuário
        }
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

            int indice = 0;

    for (Vendedor vendedor : vendedores) {
        if (termoBusca.equals("%")) {
            if (indice >= startIdx && vendedoresFiltrados.size() < pageSize) {
                if (!vendedoresFiltrados.contains(vendedor)) {
                    vendedoresFiltrados.add(vendedor);
                }
            }
            indice++;
        } else {
            if (vendedor.getNome().toLowerCase().contains(termoBusca)
                    || (String.valueOf(vendedor.getId())).equals(termoBusca)) {
                if (indice >= startIdx && vendedoresFiltrados.size() < pageSize) {
                    if (!vendedoresFiltrados.contains(vendedor)) {
                        vendedoresFiltrados.add(vendedor);
                    }
                }
                indice++;
            }
        }
    }

            // Atualiza o modelo da tabela com os clientes filtrados
            ((VendedoresTableModel) vendedoresTable.getModel()).atualizarVendedores(vendedoresFiltrados, ordem,
                    reverse);

            // Exiba as informações de paginação (por exemplo, página atual e número total
            // de páginas)
            int totalPages = (int) Math.ceil((double) vendedoresFiltrados.size() / pageSize);
            int currentPage = Math.min(paginaAtualVendedor, totalPages);
            // Agora você pode exibir currentPage e totalPages na sua interface do usuário
        }
    }

    private void nextPage() {
        paginaAtual++;
        filtrarClientes(ordem, false);
    }

    private void nextPageVendedor() {
        paginaAtualVendedor++;
        filtrarVendedores(ordem, false);
    }

    private void previousPage() {
        paginaAtual = Math.max(1, paginaAtual - 1);
        filtrarClientes(ordem, false);
    }

    private void previousPageVendedor() {
        paginaAtualVendedor = Math.max(1, paginaAtualVendedor - 1);
        filtrarVendedores(ordem, false);
    }

    // Método recursivo para percorrer a árvore e filtrar clientes
    private void filtrarClientesRecursivo(NoArvoreCliente no, String termoBusca, List<Cliente> clientesFiltrados,
            int startIdx, int pageSize) {
        // Se o nó não é nulo
        if (no != null) {
            int indice = 0;

            // Percorre cada chave no nó atual
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
                        // Adiciona o cliente aos resultados filtrados se atender ao critério de busca
                        if (indice >= startIdx && clientesFiltrados.size() < pageSize) {
                            if (!clientesFiltrados.contains(cliente)) {
                                clientesFiltrados.add(cliente);
                            }
                        }
                        
                    }
                }
                indice++;
            }

            // Chama o método recursivamente para os filhos do nó
            for (NoArvoreCliente filho : no.getFilhos()) {
                // Chama recursivamente apenas se ainda não atingiu o limite de resultados por
                // página
                if (clientesFiltrados.size() < pageSize) {
                    filtrarClientesRecursivo(filho, termoBusca, clientesFiltrados, startIdx - indice, pageSize);
                }
            }
        }
    }

    private void filtrarVendedoresRecursivo(NoArvoreVendedor no, String termoBusca, List<Vendedor> vendedoresFiltrados,
            int startIdx, int pageSize) {
        // Se o nó não é nulo
        if (no != null) {
            int indice = 0;
           // startIdx = (paginaAtualVendedor - 1) * pageSize;
            // Percorre cada chave no nó atual
            for (Vendedor vendedor : no.getChaves()) {
                if (termoBusca.equals("%")) {                    
                    if (indice >= startIdx && vendedoresFiltrados.size() < pageSize) {
                        if (!vendedoresFiltrados.contains(vendedor)) {
                            vendedoresFiltrados.add(vendedor);
                            indice++;
                        }
                        System.out.println("opas"+startIdx);
                    }else{
                        indice++;
                        System.out.println("opa"+indice+" "+startIdx+" "+pageSize);
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
                
            }

            // Chama o método recursivamente para os filhos do nó
            for (NoArvoreVendedor filho : no.getFilhos()) {
                // Chama recursivamente apenas se ainda não atingiu o limite de resultados por
                // página
                if (vendedoresFiltrados.size() < pageSize) {
                    filtrarVendedoresRecursivo(filho, termoBusca, vendedoresFiltrados, startIdx - indice, pageSize);
                }
            }
        }
    }

    private JPanel criarVendedorPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Criação do campo de busca
        buscaTextFieldVendedor = new JTextField(20);
        buscaTextFieldVendedor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                paginaAtualVendedor = 1;
                filtrarVendedores(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                paginaAtualVendedor = 1;
                filtrarVendedores(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                paginaAtualVendedor = 1;
                filtrarVendedores(ordem, false);
            }
        });

        // Criação do painel de busca
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel buscaLabel = new JLabel("Buscar Vendedor:");

        JLabel buscaIDLabel = new JLabel("Buscar por ID:");
        buscaTextFieldIDVendedor = new JTextField(10);
        buscaTextFieldIDVendedor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                paginaAtualVendedor = 1;
                filtrarVendedores(ordem, false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                paginaAtualVendedor = 1;
                filtrarVendedores(ordem, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                paginaAtualVendedor = 1;
                filtrarVendedores(ordem, false);
            }
        });

        buscaPanel.add(buscaLabel);
        buscaPanel.add(buscaTextFieldVendedor);

        buscaPanel.add(buscaIDLabel);
        buscaPanel.add(buscaTextFieldIDVendedor);

        // Adiciona o campo de busca ao painel
        panel.add(buscaPanel, BorderLayout.NORTH);

        // Criação dos botões de paginação
        JButton paginaAnteriorButton = new JButton("Página Anterior");
        JButton proximaPaginaButton = new JButton("Próxima Página");

        // Adiciona listeners aos botões de paginação
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

        // Criação de um painel para os botões de paginação
        JPanel botoesPaginacaoPanel = new JPanel();
        botoesPaginacaoPanel.add(paginaAnteriorButton);
        botoesPaginacaoPanel.add(proximaPaginaButton);

        // Criação da tabela de clientes
        vendedoresTable = new JTable(new VendedoresTableModel(new ArrayList<Vendedor>()));
        filtrarVendedores(ordem, false);
        JScrollPane scrollPane = new JScrollPane(vendedoresTable);
        JTableHeader header = vendedoresTable.getTableHeader();
        header.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int colunaClicada = vendedoresTable.columnAtPoint(e.getPoint());
                ordem = colunaClicada;
                filtrarVendedores(ordem, true);
                // ordenarPorColuna(colunaClicada);
                // System.out.println(colunaClicada);
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
        // Criação do painel principal que contém a tabela e os botões
        JPanel tabelaEControlesPanel = new JPanel(new BorderLayout());
        tabelaEControlesPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaEControlesPanel.add(botoesPaginacaoPanel, BorderLayout.SOUTH);

        // Adiciona o painel principal ao centro do layout
        panel.add(tabelaEControlesPanel, BorderLayout.CENTER);

        // Criação dos botões
        JButton cadastrarButton = new JButton("Cadastrar Vendedor");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroVendedorDialog cadastroDialog = new CadastroVendedorDialog(VendasGUI.this, dataManager, -1, "");
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

                    CadastroVendedorDialog cadastroDialog = new CadastroVendedorDialog(VendasGUI.this, dataManager,
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
        // Adição dos botões ao painel
        JPanel botoesPanel = new JPanel();
        botoesPanel.add(cadastrarButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        panel.add(botoesPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel criarCarroPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Criação da tabela de carros
        JTable carrosTable = new JTable(new CarrosTableModel(carros));
        JScrollPane scrollPane = new JScrollPane(carrosTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Criação dos botões
        JButton cadastrarButton = new JButton("Cadastrar Carro");
        JButton editarButton = new JButton("Editar Carro");
        JButton excluirButton = new JButton("Excluir Carro");

        // Adição dos botões ao painel
        JPanel botoesPanel = new JPanel();
        botoesPanel.add(cadastrarButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        panel.add(botoesPanel, BorderLayout.SOUTH);

        // Adiciona listeners aos botões
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroCarroDialog cadastroDialog = new CadastroCarroDialog(VendasGUI.this, dataManager, null);
                cadastroDialog.setVisible(true);
                carrosTable.setModel(new CarrosTableModel(carros));
            }
        });

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = carrosTable.getSelectedRow();
                if (selectedRow != -1) {
                    Object idS = carrosTable.getValueAt(selectedRow, 0);
                    for (Carro carro : carros) {
                        if (carro.getId() == idS) {
                            CadastroCarroDialog cadastroDialog = new CadastroCarroDialog(VendasGUI.this, dataManager,
                                    carro);
                            cadastroDialog.setVisible(true);
                            carrosTable.setModel(new CarrosTableModel(carros));
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = carrosTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmar = JOptionPane.showConfirmDialog(null, "Você tem certeza dessa exclusão?",
                            "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmar == JOptionPane.YES_OPTION) {
                        Object idS = carrosTable.getValueAt(selectedRow, 0);
                        excluirCarro(idS + "");
                    }
                    carrosTable.setModel(new CarrosTableModel(carros));
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para excluir.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
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

    private void excluirCliente(int idCliente) {
        dataManager.excluirClienteID(idCliente);
        System.out.println(idCliente);
    }

    private void excluirVendedor(int idVendedor) {
        dataManager.excluirVendedorID(idVendedor);
        System.out.println(idVendedor);
    }

    private void excluirCarro(String idCarro) {
        dataManager.excluirCarroID(idCarro);
        System.out.println(idCarro);
    }
}
