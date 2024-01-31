package com.antonio.vendas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoArvoreCarro implements Serializable {

    private List<Carro> chaves;
    private List<NoArvoreCarro> filhos;
    private boolean folha;

    public NoArvoreCarro(int grau, boolean folha) {
        this.chaves = new ArrayList<>(2 * grau - 1);
        this.filhos = new ArrayList<>(2 * grau);
        this.folha = folha;
    }

    public List<Carro> getChaves() {
        return chaves;
    }

    public void setChaves(List<Carro> chaves) {
        this.chaves = chaves;
    }

    public List<NoArvoreCarro> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<NoArvoreCarro> filhos) {
        this.filhos = filhos;
    }

    public boolean isFolha() {
        return folha;
    }

    public void setFolha(boolean folha) {
        this.folha = folha;
    }
}
