package com.pucrs.iat1back.knn.service;

import com.pucrs.iat1back.dto.CalculoDTO;
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

    public ResponseEntity<CalculoDTO> calcular(List<String> matriz) throws IOException {

        dadosTreino = gerenciadorArquivoService.carregarArquivo("src/treino_balanceado.csv", 558);
        dadosTeste = gerenciadorArquivoService.carregarArquivo("src/teste_balanceado.csv", 106);
//        escreveDados(dadosTreino);
//        escreveDados(dadosTeste);

        System.out.println("k ; acertos");

        for (int i = 0; i < 278; i++) {
            k = i;
            executaKnn();

            System.out.print(k);
        }


        return ResponseEntity.ok(
                CalculoDTO.builder()
                        .status(StatusEnum.CONTINUA)
                        .build());
    }

    public static void escreveDados(double[][] dados) {
        //System.out.println("\n\n--------- D A D O S ---------");
        System.out.println("    x1  x2  x3  x4  x5  x6  x7  x8  x9  classe");
        for (int i = 0; i < dados.length; i++) {
            System.out.print((i + 1) + " : ");
            for (int j = 0; j < dados[i].length; j++) {
                System.out.print(dados[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void executaKnn() {
        int acertos = 0;
        int colunaRespostaEuclidiana = 0;
        int colunaRespostaTreino = 1;
        int colunaClasse = 9;

        for (int linhaTeste = 0; linhaTeste < dadosTeste.length; linhaTeste++) {
            distancia = new double[dadosTreino.length][2];

            for (int linhaTreino = 0; linhaTreino < dadosTreino.length; linhaTreino++) {
                distancia[linhaTreino][colunaRespostaEuclidiana] = euclidiana(dadosTeste[linhaTeste], dadosTreino[linhaTreino]);
                distancia[linhaTreino][colunaRespostaTreino] = dadosTreino[linhaTreino][colunaClasse];
            }

            ordena(distancia);

            if (dadosTeste[linhaTeste][colunaClasse] == moda(distancia)) {
                acertos++;
            }
        }

        System.out.println(" ; " + acertos);
    }

    private double euclidiana(double[] atual, double[] amostra) {
        double soma = 0;
        for (int i = 0; i < atual.length - 1; i++) {
            soma += Math.pow(atual[i] - amostra[i], 2);
        }
        return Math.sqrt(soma);
    }

    private void ordena(double[][] distancia) {
        double[] aux;
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
        int rotulo, cont, classe = -1, quant = 0;
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
