package com.antonio.vendas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArvoreCliente implements Serializable {

    private NoArvoreCliente raiz;
    private int grau;

    public ArvoreCliente(int grau) {
        this.raiz = null;
        this.grau = grau;
    }
    public NoArvoreCliente getRaiz() {
        return raiz;
    }
    public void inserir(Cliente cliente) {
        if (raiz == null) {
            raiz = new NoArvoreCliente(grau, true);
            raiz.getChaves().add(cliente);
        } else {
            if (raiz.getChaves().size() == (2 * grau - 1)) {
                NoArvoreCliente novoNo = new NoArvoreCliente(grau, false);
                novoNo.getFilhos().add(raiz);
                dividirNo(novoNo, 0, raiz);
                raiz = novoNo;
            }
            inserirNo(raiz, cliente);
        }
    }

    private void dividirNo(NoArvoreCliente pai, int indice, NoArvoreCliente no) {
        NoArvoreCliente novoNo = new NoArvoreCliente(grau, no.isFolha());
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

    private void inserirNo(NoArvoreCliente no, Cliente cliente) {
        int indice = no.getChaves().size() - 1;

        if (no.isFolha()) {
            while (indice >= 0 && cliente.compareTo(no.getChaves().get(indice)) < 0) {
                indice--;
            }
            no.getChaves().add(indice + 1, cliente);
        } else {
            while (indice >= 0 && cliente.compareTo(no.getChaves().get(indice)) < 0) {
                indice--;
            }

            indice++;

            if (no.getFilhos().get(indice).getChaves().size() == (2 * grau - 1)) {
                dividirNo(no, indice, no.getFilhos().get(indice));
                if (cliente.compareTo(no.getChaves().get(indice)) > 0) {
                    indice++;
                }
            }
            inserirNo(no.getFilhos().get(indice), cliente);
        }
    }

    public void editar(Cliente cliente) {
        editarNo(raiz, cliente);
    }

    private void editarNo(NoArvoreCliente no, Cliente cliente) {
        int indice = encontrarIndiceChave(no, cliente.getId()+"");

        if (indice != -1) {
            no.getChaves().set(indice, cliente);
        } else if (!no.isFolha()) {
            int i = 0;
            while (i < no.getChaves().size() && cliente.compareTo(no.getChaves().get(i)) > 0) {
                i++;
            }
            editarNo(no.getFilhos().get(i), cliente);
        }
    }

    public void excluir(String id) {
        excluirNo(raiz, id);
    }

    private void excluirNo(NoArvoreCliente no, String id) {
        int indice = encontrarIndiceChave(no, id);

        if (indice != -1) {
            if (no.isFolha()) {
                no.getChaves().remove(indice);
            } else {
                // Substituir a chave a ser excluída pela chave mínima do subárvore à direita
                Cliente chaveSubstituta = encontrarChaveMinima(no.getFilhos().get(indice + 1));
                no.getChaves().set(indice, chaveSubstituta);

                // Recursivamente excluir a chave substituta na subárvore à direita
                excluirNo(no.getFilhos().get(indice + 1), chaveSubstituta.getId()+"");
            }
        } else if (!no.isFolha()) {
            int i = 0;
            while (i < no.getChaves().size() && id.compareTo(no.getChaves().get(i).getId()+"") > 0) {
                i++;
            }

            boolean isUltimoFilho = (i == no.getChaves().size());
            NoArvoreCliente filho = no.getFilhos().get(isUltimoFilho ? i - 1 : i);

            // Garantir que o filho tem pelo menos t grau chaves
            if (filho.getChaves().size() < grau) {
                preencherFilho(no, i);
            }

            // Recursivamente excluir a chave na subárvore apropriada
            excluirNo(filho, id);
        }
    }

    // Métodos auxiliares para encontrar índice da chave e chave mínima
    private int encontrarIndiceChave(NoArvoreCliente no, String id) {
        int i = 0;
        while (i < no.getChaves().size() && id.compareTo(no.getChaves().get(i).getId()+"") > 0) {
            i++;
        }
        return (i < no.getChaves().size() && id.equals(no.getChaves().get(i).getId()+"")) ? i : -1;
    }

    private Cliente encontrarChaveMinima(NoArvoreCliente no) {
        while (!no.isFolha()) {
            no = no.getFilhos().get(0);
        }
        return no.getChaves().get(0);
    }

    private void preencherFilho(NoArvoreCliente pai, int indiceFilho) {
        int grau = this.grau;

        NoArvoreCliente filhoAtual = pai.getFilhos().get(indiceFilho);
        NoArvoreCliente irmaoEsquerda = (indiceFilho > 0) ? pai.getFilhos().get(indiceFilho - 1) : null;
        NoArvoreCliente irmaoDireita = (indiceFilho < pai.getFilhos().size() - 1) ? pai.getFilhos().get(indiceFilho + 1)
                : null;

        // Tentar pegar uma chave do irmão à esquerda
        if (irmaoEsquerda != null && irmaoEsquerda.getChaves().size() >= grau) {
            // Mover uma chave do irmão à esquerda para o filho
            int indiceChavePai = indiceFilho - 1;
            Cliente chavePai = pai.getChaves().get(indiceChavePai);
            Cliente chaveEsquerda = irmaoEsquerda.getChaves().remove(irmaoEsquerda.getChaves().size() - 1);

            // Atualizar a chave do pai
            pai.getChaves().set(indiceChavePai, chaveEsquerda);

            // Adicionar a chave do pai ao filho
            filhoAtual.getChaves().add(0, chavePai);

            // Se os nós contêm filhos, transferir o filho mais à direita do irmão à
            // esquerda para o filho
            if (!irmaoEsquerda.isFolha()) {
                NoArvoreCliente filhoEsquerdaMaisDireita = irmaoEsquerda.getFilhos()
                        .remove(irmaoEsquerda.getFilhos().size() - 1);
                filhoAtual.getFilhos().add(0, filhoEsquerdaMaisDireita);
            }
        }
        // Tentar pegar uma chave do irmão à direita
        else if (irmaoDireita != null && irmaoDireita.getChaves().size() >= grau) {
            // Mover uma chave do irmão à direita para o filho
            int indiceChavePai = indiceFilho;
            Cliente chavePai = pai.getChaves().get(indiceChavePai);
            Cliente chaveDireita = irmaoDireita.getChaves().remove(0);

            // Atualizar a chave do pai
            pai.getChaves().set(indiceChavePai, chaveDireita);

            // Adicionar a chave do pai ao filho
            filhoAtual.getChaves().add(chavePai);

            // Se os nós contêm filhos, transferir o filho mais à esquerda do irmão à
            // direita para o filho
            if (!irmaoDireita.isFolha()) {
                NoArvoreCliente filhoDireitaMaisEsquerda = irmaoDireita.getFilhos().remove(0);
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

    private void fusaoFilhoEsquerda(NoArvoreCliente pai, int indiceFilho, NoArvoreCliente filho,
            NoArvoreCliente irmaoEsquerda) {
        int grau = this.grau;

        // Mover uma chave do pai para o filho
        Cliente chavePai = pai.getChaves().remove(indiceFilho - 1);
        filho.getChaves().add(0, chavePai);

        // Mover todas as chaves do irmão à esquerda para o filho
        filho.getChaves().addAll(0, irmaoEsquerda.getChaves());

        // Mover todos os filhos do irmão à esquerda para o filho
        filho.getFilhos().addAll(0, irmaoEsquerda.getFilhos());

        // Remover o irmão à esquerda do pai
        pai.getFilhos().remove(indiceFilho - 1);
    }

    private void fusaoFilhoDireita(NoArvoreCliente pai, int indiceFilho, NoArvoreCliente filho,
            NoArvoreCliente irmaoDireita) {
        int grau = this.grau;

        // Mover uma chave do pai para o filho
        Cliente chavePai = pai.getChaves().remove(indiceFilho);
        filho.getChaves().add(chavePai);

        // Mover todas as chaves do irmão à direita para o filho
        filho.getChaves().addAll(irmaoDireita.getChaves());

        // Mover todos os filhos do irmão à direita para o filho
        filho.getFilhos().addAll(irmaoDireita.getFilhos());

        // Remover o irmão à direita do pai
        pai.getFilhos().remove(indiceFilho + 1);
    }
    public void atualizarCliente(Cliente clienteAtualizado) {
        atualizarClienteRecursivo(raiz, clienteAtualizado);
    }

    private void atualizarClienteRecursivo(NoArvoreCliente no, Cliente clienteAtualizado) {
        if (no != null) {
            for (int i = 0; i < no.getChaves().size(); i++) {
                Cliente clienteNo = no.getChaves().get(i);
                if (clienteNo.getId() == clienteAtualizado.getId()) {
                    // Atualiza os dados do cliente no nó
                    no.getChaves().set(i, clienteAtualizado);                    
                    return;
                }
            }

            // Chama o método recursivamente para os filhos do nó
            for (NoArvoreCliente filho : no.getFilhos()) {
                atualizarClienteRecursivo(filho, clienteAtualizado);
            }
        }
    }
    public void removerCliente(Cliente cliente) {

        raiz = removerRecursivo(raiz, cliente);
    }
    private NoArvoreCliente removerRecursivo(NoArvoreCliente no, Cliente cliente) {
        if (no == null) {
            return null;
        }
    
        // Encontrar a posição da chave (cliente) na lista de chaves
        int index = no.getChaves().indexOf(cliente);
    
        // Recursivamente remover da subárvore esquerda ou direita
        if (index == -1) {
            // Cliente não encontrado na lista de chaves deste nó
            return no;
        }
    
        no.getChaves().remove(index);
    
        if (!no.isFolha()) {
            // Nó interno, remova o cliente recursivamente da subárvore
            NoArvoreCliente filho = no.getFilhos().get(index);
            Cliente substituto = encontrarSubstituto(filho);
            no.getChaves().add(index, substituto);
    
            // Remova o substituto recursivamente na subárvore
            no.getFilhos().set(index, removerRecursivo(filho, substituto));
        }
    
        return no;
    }
    
    private Cliente encontrarSubstituto(NoArvoreCliente no) {
        // Encontrar o nó mais à direita na subárvore
        while (!no.isFolha()) {
            no = no.getFilhos().get(no.getFilhos().size() - 1);
        }
    
        // Retorna o cliente mais à direita na folha
        return no.getChaves().get(no.getChaves().size() - 1);
    }
    
}
