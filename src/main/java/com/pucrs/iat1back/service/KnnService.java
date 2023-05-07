package com.pucrs.iat1back.service;

import com.pucrs.iat1back.dto.CalculoDTO;
import com.pucrs.iat1back.dto.MatrizDTO;
import com.pucrs.iat1back.enumerator.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class KnnService {

    private static double[][] dadosTreino;
    private static double[][] dadosTeste;
    private static double[][] distancia;
    private static int k;

    @Autowired
    private GerenciadorArquivoService gerenciadorArquivoService;

    public ResponseEntity<CalculoDTO> calcular(MatrizDTO matriz) throws IOException {

        dadosTreino = gerenciadorArquivoService.carregarArquivoTreino();
        //        escreveDados(dadosTreino);

        dadosTeste = new double[1][9];

        for (int i = 0; i < 9; i++) {
            dadosTeste[0][i] = gerenciadorArquivoService.retornaReferencialPossibilidade(matriz.getMatriz().get(i));
        }

        executaKnn();


        List<String> primeiraLinha = matriz.getMatriz().subList(0, 3);
        List<String> segudnaLinha = matriz.getMatriz().subList(3, 6);
        List<String> terceiraLinha = matriz.getMatriz().subList(6, 9);

        return ResponseEntity.ok(
                CalculoDTO.builder()
                        .status(StatusEnum.CONTINUA)
                        .build());
    }

    private void escreveDados(int[][] dados) {
        System.out.println("\n\n--------- DADOS TREINO ---------");
        System.out.println("    x1  x2  x3  x4  x5  x6  x7  x8  x9  classe");
        for (int i = 0; i < dados.length; i++) {
            System.out.print((i + 1) + " : ");
            for (int j = 0; j < dados[i].length; j++) {
                System.out.print(dados[i][j] + "   ");
            }
            System.out.println();
        }
    }

    public void executaKnn() {
        int acertos = 0;
        for (int i = 0; i < dadosTeste.length; i++) {
            distancia = new double[dadosTreino.length][2];

            for (int j = 0; j < dadosTreino.length; j++) {
                distancia[j][0] = euclidiana(dadosTeste[i], dadosTreino[j]);
                distancia[j][1] = dadosTreino[j][4];
            }

            //System.out.println("Antes");
            //exibeDistancias(distancia);
            ordena(distancia);
            //System.out.println("Depois");
            //exibeDistancias(distancia);
            System.out.println("Classe Predita:" + moda(distancia));
            //break;
        }
    }

    private double euclidiana(double[] atual, double[] amostra) {
        double soma = 0;
        for (int i = 0; i < atual.length - 1; i++) {
            soma += Math.pow(atual[i] - amostra[i], 2);
        }
        return Math.sqrt(soma);
    }

    private void ordena(double[][] distancia) {
        for (int i = 0; i < distancia.length - 1; i++) {
            for (int j = 0; j < distancia.length - 1 - i; j++) {
                if (distancia[j][0] > distancia[j + 1][0]) {
                    troca(distancia[j], distancia[j + 1]);
                }
            }
        }
    }

    private void troca(double linha1[], double linha2[]) {
        double aux;
        for (int i = 0; i < linha1.length; i++) {
            aux = linha1[i];
            linha1[i] = linha2[i];
            linha2[i] = aux;
        }
    }

    private int moda(double[][] distancia) {
        int cont, classe = -1, quant = 0;

        for (int c = 1; c <= 3; c++) {
            cont = 0;

            for (int i = 0; i < k; i++) {
                if (distancia[i][1] == c) cont++;
            }

            if (cont > quant) {
                quant = cont;
                classe = c;
            }
        }

        return classe;
    }

}
