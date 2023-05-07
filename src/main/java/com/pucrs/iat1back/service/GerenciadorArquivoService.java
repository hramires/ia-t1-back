package com.pucrs.iat1back.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class GerenciadorArquivoService {

    public double[][] carregarArquivoTreino() throws IOException {
        int qtdLinhas = 957;
        int qtdColunas = 10;
        double[][] dadosTreino = new double[qtdLinhas][qtdColunas];

        BufferedReader br = new BufferedReader(new FileReader("src/treino.txt"));
        br.readLine();
        String line = br.readLine();

        for (int i = 0; i < qtdLinhas; i++) {
            String[] colunas = line.split(",");

            dadosTreino[i][0] = retornaReferencialPossibilidade(colunas[0]);
            dadosTreino[i][1] = retornaReferencialPossibilidade(colunas[1]);
            dadosTreino[i][2] = retornaReferencialPossibilidade(colunas[2]);

            dadosTreino[i][3] = retornaReferencialPossibilidade(colunas[3]);
            dadosTreino[i][4] = retornaReferencialPossibilidade(colunas[4]);
            dadosTreino[i][5] = retornaReferencialPossibilidade(colunas[5]);

            dadosTreino[i][6] = retornaReferencialPossibilidade(colunas[6]);
            dadosTreino[i][7] = retornaReferencialPossibilidade(colunas[7]);
            dadosTreino[i][8] = retornaReferencialPossibilidade(colunas[8]);

            dadosTreino[i][9] = retornaReferencialClasse(colunas[9]);

            line = br.readLine();
        }

        br.close();

        return dadosTreino;
    }

    public int retornaReferencialPossibilidade(String valor) {
        switch (valor) {
            case "x":
                return 1;
            case "o":
                return 2;
            case "b":
                return 3;
            default:
                return -1;
        }
    }

    private int retornaReferencialClasse(String valor) {
        switch (valor) {
            case "positive":
                return 1;
            case "negative":
                return 0;
            default:
                return -1;
        }
    }

}
