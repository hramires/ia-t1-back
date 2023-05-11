package com.pucrs.iat1back.decisiontree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

        public static void main(String[] args) {
            // Crie um conjunto de treinamento com algumas entradas de exemplo
            List<String[]> trainingData = new ArrayList<>();
            trainingData.add(new String[]{"O", "-", "O", "-", "X", "-", "X", "-", "X", "X"});
            trainingData.add(new String[]{"O", "-", "O", "-", "X", "-", "X", "-", "O", "O"});
            trainingData.add(new String[]{"X", "-", "O", "-", "O", "-", "X", "-", "X", "O"});
            trainingData.add(new String[]{"X", "-", "X", "-", "O", "-", "O", "-", "O", "O"});
            trainingData.add(new String[]{"X", "-", "X", "-", "O", "-", "O", "-", "X", "X"});
            trainingData.add(new String[]{"O", "-", "X", "-", "X", "-", "O", "-", "O", "O"});
            trainingData.add(new String[]{"O", "-", "X", "-", "X", "-", "O", "-", "X", "X"});

            // Crie a árvore de decisão a partir do conjunto de treinamento
            TicTacToeDecisionTree decisionTree = new TicTacToeDecisionTree(trainingData);

            // Crie um conjunto de teste com algumas entradas
            List<String[]> testData = new ArrayList<>();
            testData.add(new String[]{"O", "-", "O", "-", "X", "-", "X", "-", "O"});
            testData.add(new String[]{"X", "-", "O", "-", "X", "-", "O", "-", "X"});
            testData.add(new String[]{"O", "-", "O", "-", "X", "-", "X", "-", "O"});

            // Teste a árvore de decisão
            for (String[] board : testData) {
                String predictedWinner = decisionTree.predictWinner(board);
                System.out.println("Board: " + Arrays.toString(board) + ", Predicted Winner: " + predictedWinner);
            }
        }



    // método para carregar o dataset de um arquivo CSV
    private static List<String[]> loadCsv(String filename) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
