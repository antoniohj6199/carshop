package com.antonio.vendas;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataManager {
    private List<Vendedor> vendedores;
    private List<Carro> carros;
    private List<Cliente> clientes;
    // Adicione outras listas para Venda, etc., conforme necessário

    private static final String FILE_PATH_VENDEDORES = "vendedores.dat";
    private static final String FILE_PATH_CARROS = "carros.dat";
    private static final String FILE_PATH_CLIENTES = "clientes.dat";
    // Adicione outros caminhos de arquivo conforme necessário

    public DataManager() {
        this.vendedores = new ArrayList<>();
        this.carros = new ArrayList<>();
        this.clientes = new ArrayList<>();
        // Inicialize outras listas conforme necessário
        loadExistingData();
    }

    private void loadExistingData() {
        // Carregue dados existentes dos arquivos binários, se houver
        loadVendedores();
        loadCarros();
        loadClientes();
        // Carregue outras entidades conforme necessário
    }

    @SuppressWarnings("unchecked")
    private void loadClientes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH_CLIENTES))) {
            clientes = (List<Cliente>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de clientes não encontrado. Criando um novo.");
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

    // Métodos para adicionar, listar e salvar clientes
    public void addCliente(Cliente cliente) {
        clientes.add(cliente);
        saveClientes();
    }

    private void saveClientes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH_CLIENTES))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listClientes() {
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadVendedores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH_VENDEDORES))) {
            vendedores = (List<Vendedor>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de vendedores não encontrado. Criando um novo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCarros() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH_CARROS))) {
            carros = (List<Carro>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de carros não encontrado. Criando um novo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addVendedor(Vendedor vendedor) {
        vendedores.add(vendedor);
        saveVendedores();
    }

    private void saveVendedores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH_VENDEDORES))) {
            oos.writeObject(vendedores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Implemente métodos semelhantes para outras operações e entidades

    public void listVendedores() {
        for (Vendedor vendedor : vendedores) {
            System.out.println(vendedor);
        }
    }

    // Implemente métodos semelhantes para listar outras entidades

    // Outras operações e métodos conforme necessário
}
