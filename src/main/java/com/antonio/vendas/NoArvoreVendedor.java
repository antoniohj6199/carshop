package com.antonio.vendas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoArvoreVendedor implements Serializable {

    private List<Vendedor> chaves;
    private List<NoArvoreVendedor> filhos;
    private boolean folha;

    public NoArvoreVendedor(int grau, boolean folha) {
        this.chaves = new ArrayList<>(2 * grau - 1);
        this.filhos = new ArrayList<>(2 * grau);
        this.folha = folha;
    }

    public List<Vendedor> getChaves() {
        return chaves;
    }

    public void setChaves(List<Vendedor> chaves) {
        this.chaves = chaves;
    }

    public List<NoArvoreVendedor> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<NoArvoreVendedor> filhos) {
        this.filhos = filhos;
    }

    public boolean isFolha() {
        return folha;
    }

    public void setFolha(boolean folha) {
        this.folha = folha;
    }
}
