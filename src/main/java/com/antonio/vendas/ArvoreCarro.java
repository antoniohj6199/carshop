package com.antonio.vendas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArvoreCarro implements Serializable { 

    private NoArvoreCarro raiz;
    private int grau;

    public ArvoreCarro(int grau) { 
        this.raiz = null;
        this.grau = grau;
    }
    public NoArvoreCarro getRaiz() {
        return raiz;
    }
    public void inserir(Carro carro) {
        if (raiz == null) {
            raiz = new NoArvoreCarro(grau, true);
            raiz.getChaves().add(carro);
        } else {
            if (raiz.getChaves().size() == (2 * grau - 1)) {
                NoArvoreCarro novoNo = new NoArvoreCarro(grau, false);
                novoNo.getFilhos().add(raiz);
                dividirNo(novoNo, 0, raiz);
                raiz = novoNo;
            }
            inserirNo(raiz, carro);
        }
    }

    private void dividirNo(NoArvoreCarro pai, int indice, NoArvoreCarro no) {
        NoArvoreCarro novoNo = new NoArvoreCarro(grau, no.isFolha());
        pai.getChaves().add(indice, no.getChaves().get(grau - 1));
        pai.getFilhos().add(indice + 1, novoNo);

        for (int i = 0; i < grau - 1; i++) {
            novoNo.getChaves().add(no.getChaves().remove(grau));
        }

        if (!no.isFolha()) {
            for (int i = 0; i < grau; i++) {
                novoNo.getFilhos().add(no.getFilhos().remove(grau));
            }
        }
    }

    private void inserirNo(NoArvoreCarro no, Carro carro) {
        int indice = no.getChaves().size() - 1;

        if (no.isFolha()) {
            while (indice >= 0 && carro.compareTo(no.getChaves().get(indice)) < 0) {
                indice--;
            }
            no.getChaves().add(indice + 1, carro);
        } else {
            while (indice >= 0 && carro.compareTo(no.getChaves().get(indice)) < 0) {
                indice--;
            }

            indice++;

            if (no.getFilhos().get(indice).getChaves().size() == (2 * grau - 1)) {
                dividirNo(no, indice, no.getFilhos().get(indice));
                if (carro.compareTo(no.getChaves().get(indice)) > 0) {
                    indice++;
                }
            }
            inserirNo(no.getFilhos().get(indice), carro);
        }
    }

    public void editar(Carro carro) {
        editarNo(raiz, carro);
    }

    private void editarNo(NoArvoreCarro no, Carro carro) {
        int indice = encontrarIndiceChave(no, carro.getId());

        if (indice != -1) {
            no.getChaves().set(indice, carro);
        } else if (!no.isFolha()) {
            int i = 0;
            while (i < no.getChaves().size() && carro.compareTo(no.getChaves().get(i)) > 0) {
                i++;
            }
            editarNo(no.getFilhos().get(i), carro);
        }
    }

    public void excluir(String id) {
        excluirNo(raiz, id);
    }

    private void excluirNo(NoArvoreCarro no, String id) {
        int indice = encontrarIndiceChave(no, id);

        if (indice != -1) {
            if (no.isFolha()) {
                no.getChaves().remove(indice);
            } else {
                // Substituir a chave a ser excluída pela chave mínima do subárvore à direita
                Carro chaveSubstituta = encontrarChaveMinima(no.getFilhos().get(indice + 1));
                no.getChaves().set(indice, chaveSubstituta);

                // Recursivamente excluir a chave substituta na subárvore à direita
                excluirNo(no.getFilhos().get(indice + 1), chaveSubstituta.getId());
            }
        } else if (!no.isFolha()) {
            int i = 0;
            while (i < no.getChaves().size() && id.compareTo(no.getChaves().get(i).getId()) > 0) {
                i++;
            }

            boolean isUltimoFilho = (i == no.getChaves().size());
            NoArvoreCarro filho = no.getFilhos().get(isUltimoFilho ? i - 1 : i);

            // Garantir que o filho tem pelo menos t grau chaves
            if (filho.getChaves().size() < grau) {
                preencherFilho(no, i);
            }

            // Recursivamente excluir a chave na subárvore apropriada
            excluirNo(filho, id);
        }
    }

    // Métodos auxiliares para encontrar índice da chave e chave mínima
    private int encontrarIndiceChave(NoArvoreCarro no, String id) {
        int i = 0;
        while (i < no.getChaves().size() && id.compareTo(no.getChaves().get(i).getId()) > 0) {
            i++;
        }
        return (i < no.getChaves().size() && id.equals(no.getChaves().get(i).getId())) ? i : -1;
    }

    private Carro encontrarChaveMinima(NoArvoreCarro no) {
        while (!no.isFolha()) {
            no = no.getFilhos().get(0);
        }
        return no.getChaves().get(0);
    }

    private void preencherFilho(NoArvoreCarro pai, int indiceFilho) {
        int grau = this.grau;

        NoArvoreCarro filhoAtual = pai.getFilhos().get(indiceFilho);
        NoArvoreCarro irmaoEsquerda = (indiceFilho > 0) ? pai.getFilhos().get(indiceFilho - 1) : null;
        NoArvoreCarro irmaoDireita = (indiceFilho < pai.getFilhos().size() - 1) ? pai.getFilhos().get(indiceFilho + 1)
                : null;

        // Tentar pegar uma chave do irmão à esquerda
        if (irmaoEsquerda != null && irmaoEsquerda.getChaves().size() >= grau) {
            // Mover uma chave do irmão à esquerda para o filho
            int indiceChavePai = indiceFilho - 1;
            Carro chavePai = pai.getChaves().get(indiceChavePai);
            Carro chaveEsquerda = irmaoEsquerda.getChaves().remove(irmaoEsquerda.getChaves().size() - 1);

            // Atualizar a chave do pai
            pai.getChaves().set(indiceChavePai, chaveEsquerda);

            // Adicionar a chave do pai ao filho
            filhoAtual.getChaves().add(0, chavePai);

            // Se os nós contêm filhos, transferir o filho mais à direita do irmão à
            // esquerda para o filho
            if (!irmaoEsquerda.isFolha()) {
                NoArvoreCarro filhoEsquerdaMaisDireita = irmaoEsquerda.getFilhos()
                        .remove(irmaoEsquerda.getFilhos().size() - 1);
                filhoAtual.getFilhos().add(0, filhoEsquerdaMaisDireita);
            }
        }
        // Tentar pegar uma chave do irmão à direita
        else if (irmaoDireita != null && irmaoDireita.getChaves().size() >= grau) {
            // Mover uma chave do irmão à direita para o filho
            int indiceChavePai = indiceFilho;
            Carro chavePai = pai.getChaves().get(indiceChavePai);
            Carro chaveDireita = irmaoDireita.getChaves().remove(0);

            // Atualizar a chave do pai
            pai.getChaves().set(indiceChavePai, chaveDireita);

            // Adicionar a chave do pai ao filho
            filhoAtual.getChaves().add(chavePai);

            // Se os nós contêm filhos, transferir o filho mais à esquerda do irmão à
            // direita para o filho
            if (!irmaoDireita.isFolha()) {
                NoArvoreCarro filhoDireitaMaisEsquerda = irmaoDireita.getFilhos().remove(0);
                filhoAtual.getFilhos().add(filhoDireitaMaisEsquerda);
            }
        }
        // Caso especial: fusão com o irmão à esquerda
        else if (irmaoEsquerda != null) {
            fusaoFilhoEsquerda(pai, indiceFilho, filhoAtual, irmaoEsquerda);
        }
        // Caso especial: fusão com o irmão à direita
        else if (irmaoDireita != null) {
            fusaoFilhoDireita(pai, indiceFilho, filhoAtual, irmaoDireita);
        }
    }

    private void fusaoFilhoEsquerda(NoArvoreCarro pai, int indiceFilho, NoArvoreCarro filho,
            NoArvoreCarro irmaoEsquerda) {
        int grau = this.grau;

        // Mover uma chave do pai para o filho
        Carro chavePai = pai.getChaves().remove(indiceFilho - 1);
        filho.getChaves().add(0, chavePai);

        // Mover todas as chaves do irmão à esquerda para o filho
        filho.getChaves().addAll(0, irmaoEsquerda.getChaves());

        // Mover todos os filhos do irmão à esquerda para o filho
        filho.getFilhos().addAll(0, irmaoEsquerda.getFilhos());

        // Remover o irmão à esquerda do pai
        pai.getFilhos().remove(indiceFilho - 1);
    }

    private void fusaoFilhoDireita(NoArvoreCarro pai, int indiceFilho, NoArvoreCarro filho,
            NoArvoreCarro irmaoDireita) {
        int grau = this.grau;

        // Mover uma chave do pai para o filho
        Carro chavePai = pai.getChaves().remove(indiceFilho);
        filho.getChaves().add(chavePai);

        // Mover todas as chaves do irmão à direita para o filho
        filho.getChaves().addAll(irmaoDireita.getChaves());

        // Mover todos os filhos do irmão à direita para o filho
        filho.getFilhos().addAll(irmaoDireita.getFilhos());

        // Remover o irmão à direita do pai
        pai.getFilhos().remove(indiceFilho + 1);
    }

}
