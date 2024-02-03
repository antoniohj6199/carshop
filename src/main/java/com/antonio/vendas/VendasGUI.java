package com.antonio.vendas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
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

    private JTable clientesTable;
    private JTable vendedoresTable;
    JTextField buscaTextField;
    JTextField buscaTextFieldVendedor;
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
        // Adiciona os painéis à janela principal
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Clientes", clientePanel);
        tabbedPane.addTab("Vendedores", vendedorPanel);
        tabbedPane.addTab("Carros", carroPanel);
        tabbedPane.addTab("Vendas", vendaPanel);

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

    private JPanel criarClientePanel() {
        JPanel panel = new JPanel(new BorderLayout());

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

        // Criação do painel de busca
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel buscaLabel = new JLabel("Buscar Cliente:");

        buscaPanel.add(buscaLabel);
        buscaPanel.add(buscaTextField);

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
                CadastroClienteDialog cadastroDialog = new CadastroClienteDialog(VendasGUI.this, dataManager, -1,"");
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
                    
                    CadastroClienteDialog cadastroDialog = new CadastroClienteDialog(VendasGUI.this, dataManager, Integer.parseInt((idS+"")), nomeS+"");
                    cadastroDialog.setVisible(true);
                    filtrarClientes(ordem, true);
                }else{
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton excluirButton = new JButton("Excluir Cliente");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = clientesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmar = JOptionPane.showConfirmDialog(null, "Você tem certeza dessa exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmar == JOptionPane.YES_OPTION) {     
                        Object idS = clientesTable.getValueAt(selectedRow, 0);                   
                        excluirCliente(Integer.parseInt(idS+""));
                    }
                    filtrarClientes(ordem, true);
                }else{
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
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

        if (termoBusca.length() > 1) {
            List<Cliente> clientesFiltrados = new ArrayList<>();

            // Calcule o índice de início com base na página atual e no tamanho da página
            int startIdx = (paginaAtual - 1) * pageSize;

            // Chama o método na árvore para obter os clientes filtrados com paginação
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

    private void filtrarVendedores(int ordem, boolean reverse) {
        String termoBusca = buscaTextFieldVendedor.getText().toLowerCase();

        if (termoBusca.length() > 1) {
            List<Vendedor> vendedoresFiltrados = new ArrayList<>();

            // Calcule o índice de início com base na página atual e no tamanho da página
            int startIdx = (paginaAtual - 1) * pageSize;

            // Chama o método na árvore para obter os clientes filtrados com paginação
            filtrarVendedoresRecursivo(dataManager.getArvoreVendedor().getRaiz(), termoBusca, vendedoresFiltrados,
                    startIdx, pageSize);

            // Atualiza o modelo da tabela com os clientes filtrados
            ((VendedoresTableModel) vendedoresTable.getModel()).atualizarVendedores(vendedoresFiltrados, ordem,
                    reverse);

            // Exiba as informações de paginação (por exemplo, página atual e número total
            // de páginas)
            int totalPages = (int) Math.ceil((double) vendedoresFiltrados.size() / pageSize);
            int currentPage = Math.min(paginaAtual, totalPages);
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
                // Verifica se o nome do cliente contém o termo de busca
                if (cliente.getNome().toLowerCase().contains(termoBusca)) {
                    // Adiciona o cliente aos resultados filtrados se atender ao critério de busca
                    if (indice >= startIdx && clientesFiltrados.size() < pageSize) {
                        if(!clientesFiltrados.contains(cliente)){
                            clientesFiltrados.add(cliente);
                        }                        
                    }
                    indice++;
                }
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

            // Percorre cada chave no nó atual
            for (Vendedor vendedor : no.getChaves()) {
                // Verifica se o nome do cliente contém o termo de busca
                if (vendedor.getNome().toLowerCase().contains(termoBusca)) {
                    // Adiciona o cliente aos resultados filtrados se atender ao critério de busca
                    if (indice >= startIdx && vendedoresFiltrados.size() < pageSize) {
                        if(!vendedoresFiltrados.contains(vendedor)){
                            vendedoresFiltrados.add(vendedor);
                        }                        
                    }
                    indice++;
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

        // Criação do painel de busca
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel buscaLabel = new JLabel("Buscar Vendedor:");

        buscaPanel.add(buscaLabel);
        buscaPanel.add(buscaTextFieldVendedor);

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
                CadastroVendedorDialog cadastroDialog = new CadastroVendedorDialog(VendasGUI.this, dataManager, -1,"");
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
                    
                    CadastroVendedorDialog cadastroDialog = new CadastroVendedorDialog(VendasGUI.this, dataManager, Integer.parseInt((idS+"")), nomeS+"");
                    cadastroDialog.setVisible(true);
                    filtrarVendedores(ordem, true);
                }else{
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton excluirButton = new JButton("Excluir Vendedor");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = vendedoresTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmar = JOptionPane.showConfirmDialog(null, "Você tem certeza dessa exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmar == JOptionPane.YES_OPTION) {     
                        Object idS = vendedoresTable.getValueAt(selectedRow, 0);                   
                        excluirVendedor(Integer.parseInt(idS+""));
                    }
                    filtrarVendedores(ordem, true);
                }else{
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
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
                        if(carro.getId() == idS){
                            CadastroCarroDialog cadastroDialog = new CadastroCarroDialog(VendasGUI.this, dataManager, carro);
                            cadastroDialog.setVisible(true);
                            carrosTable.setModel(new CarrosTableModel(carros));
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = carrosTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmar = JOptionPane.showConfirmDialog(null, "Você tem certeza dessa exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmar == JOptionPane.YES_OPTION) {     
                        Object idS = carrosTable.getValueAt(selectedRow, 0);                   
                        excluirCarro(idS+"");
                    }
                    carrosTable.setModel(new CarrosTableModel(carros));
                }else{
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
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
        //botoesPanel.add(excluirButton);
        panel.add(botoesPanel, BorderLayout.SOUTH);

        // Adiciona listeners aos botões
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroVendaDialog cadastroDialog = new CadastroVendaDialog(VendasGUI.this, dataManager);
                cadastroDialog.setVisible(true);
                vendasTable.setModel(new VendasTableModel(vendas));
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

    private void cadastrarVenda() {
        // Lógica para cadastrar uma nova venda
        // Exemplo: abrir uma nova janela de cadastro de venda
        // e, ao finalizar, adicionar a venda à lista e atualizar a tabela
    }

    private void editarVenda() {
        // Lógica para editar uma venda selecionada na tabela
        // Exemplo: abrir uma nova janela de edição de venda
        // e, ao finalizar, atualizar a venda na lista e na tabela
    }

    private void excluirVenda() {
        // Lógica para excluir uma venda selecionada na tabela
        // Exemplo: remover a venda da lista e atualizar a tabela
    }
}
