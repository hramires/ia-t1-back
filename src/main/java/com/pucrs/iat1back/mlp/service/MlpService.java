package com.pucrs.iat1back.mlp.service;

import com.pucrs.iat1back.dto.CalculoDTO;
import com.pucrs.iat1back.enumerator.StatusEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.io.File;
import java.util.ArrayList;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

@Service
public class MlpService {    

    public ResponseEntity<CalculoDTO> calcular(List<String> matriz) throws Exception {    

        // Carrega o dataset de treino
        CSVLoader loader = new CSVLoader();
        loader.setFieldSeparator(",");
        loader.setSource(new File("src/treino_balanceado.csv"));
        Instances data = loader.getDataSet();

        // seta classe de atributos
        data.setClassIndex(data.numAttributes() - 1);

        //codificação nos atributos categóricos.
        int[] categoricalIndices = {0, 1, 2, 4, 5, 6, 8};
        for (int i : categoricalIndices) {
            weka.filters.unsupervised.attribute.StringToNominal filter = new weka.filters.unsupervised.attribute.StringToNominal();
            filter.setAttributeRange(Integer.toString(i + 1));
            filter.setInputFormat(data);
            data = weka.filters.Filter.useFilter(data, filter);
        }

        //treina o classificador mlp
        MultilayerPerceptron mlp = new MultilayerPerceptron();
        mlp.setHiddenLayers("10");
        mlp.setLearningRate(0.1);
        mlp.setTrainingTime(2000);
        mlp.buildClassifier(data);

        //Faz previsões no input da matriz usando o classificador treinado 
        double[] predictions = new double[matriz.size()];
        for (int i = 0; i < matriz.size(); i++) {
            Instance instance = createInstanceFromMatriz(matriz, data);
            predictions[i] = mlp.classifyInstance(instance);
        }

        // retorna o resultado
        List<Double> result = new ArrayList<>();
        for (double prediction : predictions) {
            result.add(prediction);
        }

        return ResponseEntity.ok(
                CalculoDTO.builder()
                        .status(StatusEnum.CONTINUA)                        
                        .build());
    }

    private Instance createInstanceFromMatriz(List<String> matriz, Instances data) {
        Instance instance = new DenseInstance(data.numAttributes());
        instance.setDataset(data);
        for (int i = 0; i < matriz.size(); i++) {
            instance.setValue(i, matriz.get(i));
        }
        return instance;
    }
    
}



