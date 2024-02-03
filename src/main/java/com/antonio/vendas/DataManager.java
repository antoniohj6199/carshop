package com.antonio.vendas;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataManager {
    private List<Vendedor> vendedores;
    private List<Carro> carros;
    private List<Cliente> clientes;
    private List<Venda> vendas;

    private static final String FILE_PATH_VENDEDORES = "banco\\vendedores.dat";
    private static final String FILE_PATH_CARROS = "banco\\carros.dat";
    private static final String FILE_PATH_CLIENTES = "banco\\clientes.dat";
    private static final String FILE_PATH_VENDAS = "banco\\vendas.dat";

    private ArvoreCliente arvoreClientes;
    private ArvoreCarro arvoreCarros;
    private ArvoreVendedor arvoreVendedores;

    public DataManager() {
        this.vendedores = new ArrayList<>();
        this.carros = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.vendas = new ArrayList<>();

        this.arvoreClientes = new ArvoreCliente(3);
        this.arvoreCarros = new ArvoreCarro(3);
        this.arvoreVendedores = new ArvoreVendedor(3);

        loadExistingData();
    }

    private void loadExistingData() {
        loadVendedores();
        loadCarros();
        loadClientes();
        loadVendas();

        // Leitura carga inicial
        loadClientesFromJson("data\\clientes.json");
        loadCarrosFromJson("data\\carros.json");
        loadVendedoresFromJson("data\\vendedores.json");

        // listClientes();
        // listCarros();
        // listVendedores();
    }

    @SuppressWarnings("unchecked")
    private void loadClientes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH_CLIENTES))) {
            clientes = (List<Cliente>) ois.readObject();
            for (Cliente cliente : clientes) {
                arvoreClientes.inserir(cliente);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de clientes não encontrado. Criando um novo.");
            saveClientes();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadVendas() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH_VENDAS))) {
            vendas = (List<Venda>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de vendas não encontrado. Criando um novo.");
            saveVendas();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadClientesFromJson(String jsonFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File(jsonFilePath);

            if (jsonFile.exists()) {
                // Ler clientes do JSON
                List<Cliente> clientesFromJson = objectMapper.readValue(jsonFile,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Cliente.class));

                // Adicionar clientes do JSON à lista se ainda não existirem
                for (Cliente cliente : clientesFromJson) {
                    if (!clientes.contains(cliente)) {
                        clientes.add(cliente);
                        arvoreClientes.inserir(cliente);
                    }
                }

                // Salvar a lista atualizada de clientes
                saveClientes();
            } else {
                System.out.println("Arquivo JSON não encontrado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCarrosFromJson(String jsonFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File(jsonFilePath);

            if (jsonFile.exists()) {
                // Ler carros do JSON
                List<Carro> carrosFromJson = objectMapper.readValue(jsonFile,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Carro.class));

                // Adicionar carros do JSON à lista se ainda não existirem
                for (Carro carro : carrosFromJson) {
                    if (!carros.contains(carro)) {
                        carros.add(carro);
                        arvoreCarros.inserir(carro);
                    }
                }

                // Salvar a lista atualizada de carros
                saveCarros();
            } else {
                System.out.println("Arquivo JSON não encontrado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadVendedoresFromJson(String jsonFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File(jsonFilePath);

            if (jsonFile.exists()) {
                // Ler vendedores do JSON
                List<Vendedor> vendedoresFromJson = objectMapper.readValue(jsonFile,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Vendedor.class));

                // Adicionar vendedores do JSON à lista se ainda não existirem
                for (Vendedor vendedor : vendedoresFromJson) {
                    if (!vendedores.contains(vendedor)) {
                        vendedores.add(vendedor);
                        arvoreVendedores.inserir(vendedor);
                    }
                }

                // Salvar a lista atualizada de vendedores
                saveVendedores();
            } else {
                System.out.println("Arquivo JSON não encontrado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public ArvoreCliente getArvoreCliente() {
        return this.arvoreClientes;
    }

    public ArvoreVendedor getArvoreVendedor() {
        return this.arvoreVendedores;
    }

    public List<Carro> getCarros() {
        return carros;
    }
    public List<Venda> getVendas() {
        return vendas;
    }

    public List<Vendedor> getVendedores() {
        return vendedores;
    }
    public Cliente getClienteByNome(String nome) {
        for (Cliente cliente : clientes) {
            if (cliente.getNome().equalsIgnoreCase(nome)) {
                return cliente;
            }
        }
        return null;
    }
    public Vendedor getVendedorByNome(String nome) {
        for (Vendedor vendedor : vendedores) {
            if (vendedor.getNome().equalsIgnoreCase(nome)) {
                return vendedor;
            }
        }
        return null; 
    }
    public Carro getCarroByModelo(String modelo) {
        for (Carro carro : carros) {
            if (carro.getModelo().equalsIgnoreCase(modelo)) {
                return carro;
            }
        }
        return null; 
    }
    private int encontrarMaiorIDCliente() {
        int maiorID = Integer.MIN_VALUE;

        for (Cliente cliente : clientes) {
            if (cliente.getId() > maiorID) {
                maiorID = cliente.getId();
            }
        }

        return maiorID;
    }
    private int encontrarMaiorIDVenda() {
        int maiorID = 0;

        for (Venda venda : vendas) {
            if (Integer.parseInt(venda.getId()) > maiorID) {
                maiorID = Integer.parseInt(venda.getId());
            }
        }

        return maiorID;
    }

    private int encontrarMaiorIDVendedor() {
        int maiorID = Integer.MIN_VALUE;

        for (Vendedor vendedor : vendedores) {
            if (vendedor.getId() > maiorID) {
                maiorID = vendedor.getId();
            }
        }

        return maiorID;
    }

    private int encontrarMaiorIDCarro() {
        int maiorID = Integer.MIN_VALUE;

        for (Carro carro : carros) {
            if (Integer.parseInt(carro.getId()) > maiorID) {
                maiorID = Integer.parseInt(carro.getId());
            }
        }

        return maiorID;
    }

    public void addCliente(Cliente cliente) {
        if (cliente.getId() <= 0) {
            cliente.setId(encontrarMaiorIDCliente() + 1);
            clientes.add(cliente);
            arvoreClientes.inserir(cliente);
        } else {
            for (Cliente clienteExistente : clientes) {
                if (clienteExistente.getId() == cliente.getId()) {
                    clienteExistente.setNome(cliente.getNome());
                    arvoreClientes.atualizarCliente(clienteExistente);
                    break;
                }
            }
        }
        saveClientes();
    }
    public void addVenda(Venda venda) {
        if (Integer.parseInt(venda.getId()) <= 0) {
            venda.setId((encontrarMaiorIDVenda() + 1)+"");
            vendas.add(venda);            
        } else {
            
        }
        saveVendas();
    }
    public void excluirClienteID(int idCliente) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == idCliente) {
                excluirCliente(cliente);
                return;
            }
        }
    }

    public void excluirVendedorID(int idVendedor) {
        for (Vendedor vendedor : vendedores) {
            if (vendedor.getId() == idVendedor) {
                excluirVendedor(vendedor);
                return;
            }
        }
    }

    public void excluirCarroID(String idCarro) {
        for (Carro carro : carros) {
            if (carro.getId().equals(idCarro)) {
                excluirCarro(carro);
                return;
            }
        }
    }

    public void excluirCliente(Cliente cliente) {        
        clientes.remove(cliente);
        this.arvoreClientes = new ArvoreCliente(3);
        for (Cliente clienteT : clientes) {            
            arvoreClientes.inserir(clienteT);            
        }        
        saveClientes();
    }
    public void excluirVendedor(Vendedor vendedor) {        
        vendedores.remove(vendedor);
        this.arvoreVendedores = new ArvoreVendedor(3);
        for (Vendedor vendedorT : vendedores) {            
            arvoreVendedores.inserir(vendedorT);            
        }        
        saveVendedores();
    }

    public void excluirCarro(Carro carro) {
        carros.remove(carro);
        saveCarros();
    }

    private void saveClientes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH_CLIENTES))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCarros() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH_CARROS))) {
            oos.writeObject(carros);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveVendas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH_VENDAS))) {
            oos.writeObject(vendas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listClientes() {
        listClientesNaArvore(arvoreClientes.getRaiz());
    }

    private void listClientesNaArvore(NoArvoreCliente no) {
        if (no != null) {
            // Imprimir chaves/nós do nó atual
            for (Cliente cliente : no.getChaves()) {
                System.out.println(cliente);
            }

            // Recursivamente chamar para os filhos
            for (NoArvoreCliente filho : no.getFilhos()) {
                listClientesNaArvore(filho);
            }
        }
    }

    public void listCarros() {
        listCarrosNaArvore(arvoreCarros.getRaiz());
    }

    private void listCarrosNaArvore(NoArvoreCarro no) {
        if (no != null) {
            // Imprimir chaves/nós do nó atual
            for (Carro carro : no.getChaves()) {
                System.out.println(carro);
            }

            // Recursivamente chamar para os filhos
            for (NoArvoreCarro filho : no.getFilhos()) {
                listCarrosNaArvore(filho);
            }
        }
    }

    public void listVendedors() {
        listVendedorsNaArvore(arvoreVendedores.getRaiz());
    }

    private void listVendedorsNaArvore(NoArvoreVendedor no) {
        if (no != null) {
            // Imprimir chaves/nós do nó atual
            for (Vendedor vendedor : no.getChaves()) {
                System.out.println(vendedor);
            }

            // Recursivamente chamar para os filhos
            for (NoArvoreVendedor filho : no.getFilhos()) {
                listVendedorsNaArvore(filho);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadVendedores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH_VENDEDORES))) {
            vendedores = (List<Vendedor>) ois.readObject();
            for (Vendedor vendedor : vendedores) {
                arvoreVendedores.inserir(vendedor);
            }
            // System.out.println(vendedores.size());
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de vendedores não encontrado. Criando um novo.");
            saveVendedores();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCarros() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH_CARROS))) {
            carros = (List<Carro>) ois.readObject();
            for (Carro carro : carros) {
                arvoreCarros.inserir(carro);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de carros não encontrado. Criando um novo.");
            saveCarros();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addVendedor(Vendedor vendedor) {
        if (vendedor.getId() <= 0) {
            vendedor.setId(encontrarMaiorIDVendedor() + 1);
            vendedores.add(vendedor);
            arvoreVendedores.inserir(vendedor);
        } else {
            for (Vendedor vendedorExistente : vendedores) {
                if (vendedorExistente.getId() == vendedor.getId()) {
                    vendedorExistente.setNome(vendedor.getNome());
                    arvoreVendedores.atualizarVendedor(vendedorExistente);
                    break;
                }
            }
        }
        saveVendedores();
    }

    public void addCarro(Carro carro) {
        // System.out.println("achou "+carro.getId());
        if (Integer.parseInt(carro.getId()) <= 0) {
            carro.setId((encontrarMaiorIDCarro() + 1) + "");
            carros.add(carro);
        } else {
            for (Carro carroExistente : carros) {
                if (carroExistente.getId().equals(carro.getId())) {
                    carroExistente.setCor(carro.getCor());
                    carroExistente.setCreatedAt(obterDataAtualFormatada());
                    carroExistente.setDisponivel(carro.isDisponivel());
                    carroExistente.setIdCliente(carro.getIdCliente());
                    carroExistente.setIdVendedor(carro.getIdVendedor());
                    carroExistente.setModelo(carro.getModelo());
                    carroExistente.setPreco(carro.getPreco());
                    carroExistente.setUpdatedAt(carro.getUpdatedAt());
                    break;
                }
            }
        }
        saveCarros();
    }

    private void saveVendedores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH_VENDEDORES))) {
            oos.writeObject(vendedores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listVendedores() {
        for (Vendedor vendedor : vendedores) {
            System.out.println(vendedor);
        }
    }

    public String obterDataAtualFormatada() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return agora.format(formatter);
    }
}
