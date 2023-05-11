package com.pucrs.iat1back.mlp.service;

import com.pucrs.iat1back.dto.CalculoDTO;
import com.pucrs.iat1back.enumerator.StatusEnum;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.File;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;

@Service
public class MlpService {    

    public ResponseEntity<CalculoDTO> calcular(List<String> board) throws Exception {
        System.out.println("dentro do método calcular matriz: " + board);
    
        // Carrega o dataset de treino
        String filePath = "src/treino_balanceado_mlp.arff";
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File(filePath));
        System.out.println("arquivo: " + loader);
    
        Instances instances = loader.getDataSet();

        //PrintDataSet(instances);

        instances.setClassIndex(instances.numAttributes() - 1);

        System.out.println("loader conseguiu buscar o dataset: " + loader.getClass() + "\n");
    
        // codificação nos atributos categóricos
        int[] categoricalIndices = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        for (int i : categoricalIndices) {
            StringToNominal filter = new StringToNominal();
            filter.setAttributeRange(Integer.toString(i + 1));
            filter.setInputFormat(instances);
            instances = Filter.useFilter(instances, filter);
        }
    
        // treina o classificador MLP
        MultilayerPerceptron mlp = new MultilayerPerceptron();
        mlp.setHiddenLayers("10,10,10");
        mlp.setLearningRate(0.5);
        mlp.setTrainingTime(1000);
        mlp.buildClassifier(instances);
    
        // cria uma instância a partir do tabuleiro do jogo
        Instance instance = createInstanceFromBoard(board, instances);
    
        // faz a previsão usando o classificador treinado
        double prediction = mlp.classifyInstance(instance);

        System.out.println("valor previsto pelo algoritmo: " + prediction + "\n");

        // retorna o resultado
        StatusEnum status = StatusEnum.ENCERRAR;
        if(prediction == 0.0)
            status = StatusEnum.POSITIVO_X;
        if(prediction == 1.0)
            status = StatusEnum.NEGATIVO_X;
        if(prediction == 2.0)
            status = StatusEnum.CONTINUA;

        return ResponseEntity.ok(
                CalculoDTO.builder()
                        .status(status)
                        .build());
    }

    private Instance createInstanceFromBoard(List<String> board, Instances data) {
        Instance instance = new DenseInstance(data.numAttributes());
        instance.setDataset(data);
    
        // cria os atributos da instância
        int attributeIndex = 0;
        for (String cell : board) {
            switch (cell) {
                case "X":
                    instance.setValue(attributeIndex, 0.0);
                    break;
                case "O":
                    instance.setValue(attributeIndex, 1.0);
                    break;
                default:
                    instance.setValue(attributeIndex, 2.0);
            }
            attributeIndex++;
        }
    
        return instance;
    }  
    
    public ResponseEntity<String> avaliar() throws Exception {
        // Carrega o dataset de teste
        String filePath = "src/teste_balanceado_mlp.arff";
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File(filePath));
        Instances instances = loader.getDataSet();
        instances.setClassIndex(instances.numAttributes() - 1);
    
        // codificação nos atributos categóricos
        int[] categoricalIndices = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        for (int i : categoricalIndices) {
            StringToNominal filter = new StringToNominal();
            filter.setAttributeRange(Integer.toString(i + 1));
            filter.setInputFormat(instances);
            instances = Filter.useFilter(instances, filter);
        }
    
        // treina o classificador MLP
        MultilayerPerceptron mlp = trainMlpModel(instances);
    
        // avalia o modelo
        Evaluation eval = new Evaluation(instances);
        eval.evaluateModel(mlp, instances);
    
        // obtém as métricas de avaliação do modelo
        double accuracy = eval.pctCorrect();
        double precision = eval.weightedPrecision();
        double acertos = eval.correct();
    
        // formata a saída das métricas
        String result = String.format("Acurácia do modelo: %.2f%%\n"
                + "Precisão do modelo: %.2f\n"
                + "Acertos do modelo: %.2f\n", accuracy, precision, acertos);
    
        return ResponseEntity.ok(result);
    }
    

    private MultilayerPerceptron trainMlpModel(Instances instances) throws Exception {
        // treina o classificador MLP
        MultilayerPerceptron mlp = new MultilayerPerceptron();
        mlp.setHiddenLayers("10,10,10");
        mlp.setLearningRate(0.7);
        mlp.setTrainingTime(700);
        mlp.buildClassifier(instances);
        return mlp;
    }
    
    private void PrintDataSet(Instances instances){
        if (instances == null) {
            System.out.println("Falhou em carregar o arquivo ARFF");
        } else {
            System.out.println("Loaded " + instances.numInstances() + " instances from ARFF file");
            for (int i = 0; i < instances.numInstances(); i++) {
                Instance instance = instances.instance(i);
                for (int j = 0; j < instances.numAttributes(); j++) {
                    Attribute attribute = instances.attribute(j);
                    double value = instance.value(j);
                    System.out.println(attribute.name() + ": " + value);
                }
                System.out.println(); 
            }
        }
    }
}



