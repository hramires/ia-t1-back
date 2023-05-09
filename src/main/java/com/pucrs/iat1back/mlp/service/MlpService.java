package com.pucrs.iat1back.mlp.service;

import com.pucrs.iat1back.dto.CalculoDTO;
import com.pucrs.iat1back.enumerator.StatusEnum;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.File;

import weka.classifiers.functions.MultilayerPerceptron;
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
        String filePath = "C:\\Users\\leona\\Desktop\\ia-back2\\ia-t1-back-main\\src\\treino_balanceado_mlp.arff";
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File(filePath));
        System.out.println("arquivo: " + loader);
    
        Instances instances = loader.getDataSet();
        instances.setClassIndex(instances.numAttributes() - 1);
    
        System.out.println("loader conseguiu buscar o dataset: " + loader + "\n");
    
        // seta classe de atributos
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
        MultilayerPerceptron mlp = new MultilayerPerceptron();
        mlp.setHiddenLayers("10");
        mlp.setLearningRate(0.5);
        mlp.setTrainingTime(2000);
        mlp.buildClassifier(instances);
    
        // cria uma instância a partir do tabuleiro do jogo
        Instance instance = createInstanceFromBoard(board, instances);
    
        // faz a previsão usando o classificador treinado
        double prediction = mlp.classifyInstance(instance);

        System.out.println("valor previsto pelo algoritmo: " + prediction + "\n");

        // retorna o resultado
        StatusEnum status = prediction == 0.0 ? StatusEnum.CONTINUA : StatusEnum.ENCERRAR;
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
                case "x":
                    instance.setValue(attributeIndex, 1.0);
                    break;
                case "o":
                    instance.setValue(attributeIndex, 2.0);
                    break;
                default:
                    instance.setValue(attributeIndex, 0.0);
            }
            attributeIndex++;
        }
    
        return instance;
    }        
}



