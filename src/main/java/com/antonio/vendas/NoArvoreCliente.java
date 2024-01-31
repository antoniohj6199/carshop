package com.antonio.vendas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoArvoreCliente implements Serializable {

    private List<Cliente> chaves;
    private List<NoArvoreCliente> filhos;
    private boolean folha;

    public NoArvoreCliente(int grau, boolean folha) {
        this.chaves = new ArrayList<>(2 * grau - 1);
        this.filhos = new ArrayList<>(2 * grau);
        this.folha = folha;
    }

    public List<Cliente> getChaves() {
        return chaves;
    }

    public void setChaves(List<Cliente> chaves) {
        this.chaves = chaves;
    }

    public List<NoArvoreCliente> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<NoArvoreCliente> filhos) {
        this.filhos = filhos;
    }

    public boolean isFolha() {
        return folha;
    }

    public void setFolha(boolean folha) {
        this.folha = folha;
    }
}
