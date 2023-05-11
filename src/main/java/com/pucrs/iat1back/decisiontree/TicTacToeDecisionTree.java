package com.pucrs.iat1back.decisiontree;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeDecisionTree {

    private static final int BOARD_SIZE = 9;

    private Node root;

    public TicTacToeDecisionTree(List<String[]> trainingData) {
        root = buildTree(trainingData);
    }

    public String predictWinner(String[] board) {
        Node currentNode = root;
        while (!currentNode.isLeaf()) {
            int attributeIndex = currentNode.getAttributeIndex();
            char attributeValue = board[attributeIndex].charAt(0);
            currentNode = currentNode.getChild(attributeValue);
        }
        return currentNode.getLabel();
    }

    private Node buildTree(List<String[]> trainingData) {
        if (trainingData.isEmpty()) {
            return new LeafNode("Empate");
        }
        boolean sameLabel = true;
        String firstLabel = trainingData.get(0)[BOARD_SIZE];
        for (String[] data : trainingData) {
            if (!data[BOARD_SIZE].equals(firstLabel)) {
                sameLabel = false;
                break;
            }
        }
        if (sameLabel) {
            return new LeafNode(firstLabel);
        }
        int bestAttributeIndex = findBestAttribute(trainingData);
        Node rootNode;
        rootNode = new Node(bestAttributeIndex) {
            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public Node getChild(char attributeValue) {
                return null;
            }

            @Override
            public void addChild(char attributeValue, Node childNode) {

            }

            @Override
            public String getLabel() {
                return null;
            }
        };
        for (char attributeValue : new char[] {'X', 'O', 'V'}) {
            List<String[]> subset = getSubset(trainingData, bestAttributeIndex, attributeValue);
            Node childNode = buildTree(subset);
            rootNode.addChild(attributeValue, childNode);
        }
        return rootNode;
    }

    private int findBestAttribute(List<String[]> trainingData) {
        double maxInformationGain = Double.MIN_VALUE;
        int bestAttributeIndex = -1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            double informationGain = calculateInformationGain(trainingData, i);
            if (informationGain > maxInformationGain) {
                maxInformationGain = informationGain;
                bestAttributeIndex = i;
            }
        }
        return bestAttributeIndex;
    }

    private double calculateInformationGain(List<String[]> trainingData, int attributeIndex) {
        double entropyBefore = calculateEntropy(trainingData);
        double sum = 0;
        for (char attributeValue : new char[] {'X', 'O', 'V'}) {
            List<String[]> subset = getSubset(trainingData, attributeIndex, attributeValue);
            double weight = (double) subset.size() / trainingData.size();
            sum += weight * calculateEntropy(subset);
        }
        double entropyAfter = sum;
        return entropyBefore - entropyAfter;
    }

    private double calculateEntropy(List<String[]> data) {
        double sum = 0;
        for (char label : new char[] {'X', 'O', 'E'}) {
            int count = countLabels(data, label);
            double proportion = (double) count / data.size();
            if (proportion > 0) {
                sum -= proportion * Math.log(proportion) / Math.log(2);
            }
        }
        return sum;
    }

    private int countLabels(List<String[]> data, char label) {
        int count = 0;
        for (String[] instance : data) {
            if (instance[BOARD_SIZE].equals(String.valueOf(label))) {
                count++;
            }
        }
        return count;
    }

    private List<String[]> getSubset(List<String[]> data, int attributeIndex, char attributeValue) {
        List<String[]> subset = new ArrayList<>();
        for (String[] instance : data) {
            if (instance[attributeIndex].charAt(0) == attributeValue) {
                subset.add(instance);
            }
        }
        return subset;
    }

    private abstract static class Node {

        protected int attributeIndex;

        public Node(int attributeIndex) {
            this.attributeIndex = attributeIndex;
        }

        public int getAttributeIndex() {
            return attributeIndex;
        }

        public abstract boolean isLeaf();

        public abstract Node getChild(char attributeValue);

        public abstract void addChild(char attributeValue, Node childNode);

        public abstract String getLabel();
    }

    private static class InternalNode extends Node {

        private Node[] children;

        public InternalNode(int attributeIndex) {
            super(attributeIndex);
            children = new Node[3];
        }

        public boolean isLeaf() {
            return false;
        }

        public Node getChild(char attributeValue) {
            switch (attributeValue) {
                case 'X':
                    return children[0];
                case 'O':
                    return children[1];
                default:
                    return children[2];
            }
        }

        public void addChild(char attributeValue, Node childNode) {
            switch (attributeValue) {
                case 'X':
                    children[0] = childNode;
                    break;
                case 'O':
                    children[1] = childNode;
                    break;
                default:
                    children[2] = childNode;
                    break;
            }
        }

        public String getLabel() {
            return null;
        }
    }

    private static class LeafNode extends Node {

        private String label;

        public LeafNode(String label) {
            super(-1);
            this.label = label;
        }

        public boolean isLeaf() {
            return true;
        }

        public Node getChild(char attributeValue) {
            return null;
        }

        public void addChild(char attributeValue, Node childNode) {
            throw new UnsupportedOperationException();
        }

        public String getLabel() {
            return label;
        }
    }
}