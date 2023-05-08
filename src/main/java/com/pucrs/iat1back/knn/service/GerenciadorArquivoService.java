package com.pucrs.iat1back.knn.service;

import com.pucrs.iat1back.enumerator.StatusEnum;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class GerenciadorArquivoService {

    public double[][] carregarArquivo(String nomeArquivo, int qtdLinhas) throws IOException {
        int qtdColunas = 10;
        double[][] dadosTreino = new double[qtdLinhas][qtdColunas];

        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));

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

            dadosTreino[i][9] = classToNumber(colunas[9]);

            line = br.readLine();
        }

        br.close();

        return dadosTreino;
    }

    public int retornaReferencialPossibilidade(String valor) {
        switch (valor) {
            case "x":
            case "X":
                return 1;
            case "o":
            case "O":
                return 2;
            case "b":
            case "":
                return 3;
            default:
                return -1;
        }
    }

    public int classToNumber(String valor) {
        switch (valor) {
            case "positive_x":
                return 1;
            case "negative_x":
                return 0;
            default:
                return -1;
        }
    }

    public StatusEnum numberToClass(int valor) {
        switch (valor) {
            case 1:
                return StatusEnum.POSITIVO_X;
            case 0:
                return StatusEnum.NEGATIVO_X;
            default:
                return StatusEnum.CONTINUA;
        }
    }

}
