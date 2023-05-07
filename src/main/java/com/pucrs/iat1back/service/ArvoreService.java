package com.pucrs.iat1back.service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArvoreService {
    private static final String FILE_PATH = "treino.txt";
    private static final String POSITIVE = "positive";
    private static final String NEGATIVE = "negative";

    public static void main(String[] args) {
        try {
            List<List<String>> data = readData(FILE_PATH);
            Node root = buildDecisionTree(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<List<String>> readData(String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<String> instance = new ArrayList<>();
                for (String value : values) {
                    instance.add(value);
                }
                data.add(instance);
            }
        }

        return data;
    }

    private static Node buildDecisionTree(List<List<String>> data) {

        if (data.isEmpty() || allAttributesSame(data)) {
            String classLabel = getMajorityClassLabel(data);
            return new Node(-1, null, null, classLabel);
        }

        int bestAttrIndex = getBestAttributeIndex(data);
        Node root = new Node(bestAttrIndex, null, null, null);

        List<String> attributeValues = getAttributeValues(data, bestAttrIndex);
        for (String attrValue : attributeValues) {
            List<List<String>> subsetData = getSubset(data, bestAttrIndex, attrValue);
            Node child = buildDecisionTree(subsetData);
            root.addChild(attrValue, child);
        }

        return root;
    }

    private static boolean allAttributesSame(List<List<String>> data) {
        List<String> firstInstance = data.get(0);
        for (List<String> instance : data) {
            if (!instance.equals(firstInstance)) {
                return false;
            }
        }
        return true;
    }

    private static String getMajorityClassLabel(List<List<String>> data) {
        Map<String, Integer> classCounts = new HashMap<>();
        for (List<String> instance : data) {
            String classLabel = instance.get(instance.size() - 1);
            classCounts.put(classLabel, classCounts.getOrDefault(classLabel, 0) + 1);
        }
        String majorityClassLabel = null;
        int maxCount = -1;
        for (Map.Entry<String, Integer> entry : classCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                majorityClassLabel = entry.getKey();
            }
        }
        return majorityClassLabel;
    }
    
    private static int getBestAttributeIndex(List<List<String>> data) {
        double maxInfoGain = Double.MIN_VALUE;
        int bestAttrIndex = -1;
    
        for (int i = 0; i < data.get(0).size() - 1; i++) {
            double currInfoGain = computeInformationGain(data, i);
            if (currInfoGain > maxInfoGain) {
                maxInfoGain = currInfoGain;
                bestAttrIndex = i;
            }
        }
    
        return bestAttrIndex;
    }
    
    private static double computeInformationGain(List<List<String>> data, int attrIndex) {
        double entropyBefore = getEntropy(data);
    
        Map<String, List<List<String>>> subsets = getSubsets(data, attrIndex);
    
        double entropyAfter = 0;
        for (Map.Entry<String, List<List<String>>> entry : subsets.entrySet()) {
            double weight = (double) entry.getValue().size() / data.size();
            entropyAfter += weight * getEntropy(entry.getValue());
        }
    
        return entropyBefore - entropyAfter;
    }

    private static List<String> getAttributeValues(List<List<String>> data, int attributeIndex) {
        List<String> attributeValues = new ArrayList<>();
        for (List<String> instance : data) {
            String attributeValue = instance.get(attributeIndex);
            if (!attributeValues.contains(attributeValue)) {
                attributeValues.add(attributeValue);
            }
        }
        return attributeValues;
    }
    
    private static List<List<String>> getSubset(List<List<String>> data, int attributeIndex, String attributeValue) {
        List<List<String>> subset = new ArrayList<>();
        for (List<String> instance : data) {
            if (instance.get(attributeIndex).equals(attributeValue)) {
                List<String> newInstance = new ArrayList<>(instance);
                newInstance.remove(attributeIndex);
                subset.add(newInstance);
            }
        }
        return subset;
    }
    
    
    private static Map<String, List<List<String>>> getSubsets(List<List<String>> data, int bestAttrIndex) {
        Map<String, List<List<String>>> subsets = new HashMap<>();
    
        for (List<String> instance : data) {
            String attrValue = instance.get(bestAttrIndex);
    
            List<List<String>> subset = subsets.get(attrValue);
            if (subset == null) {
                subset = new ArrayList<>();
                subsets.put(attrValue, subset);
            }
    
            subset.add(instance);
        }
    
        return subsets;
    }
    
    private static double getEntropy(List<List<String>> data) {
        if (data.isEmpty()) {
            return 0;
        }
    
        int positiveCount = 0;
        for (List<String> instance : data) {
            if (instance.get(instance.size() - 1).equals("positive")) {
                positiveCount++;
            }
        }
    
        if (positiveCount == 0 || positiveCount == data.size()) {
            return 0;
        }
    
        double negativeCount = data.size() - positiveCount;
    
        double positiveRatio = (double) positiveCount / data.size();
        double negativeRatio = (double) negativeCount / data.size();
    
        return -positiveRatio * log2(positiveRatio) - negativeRatio * log2(negativeRatio);
    }

    private static double log2(double num) {
        return Math.log(num) / Math.log(2);
    }
}