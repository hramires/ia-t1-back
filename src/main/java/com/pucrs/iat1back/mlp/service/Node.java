package com.pucrs.iat1back.mlp.service;
import java.util.Map;

public class Node {
    private final int attributeIndex;
    private final String attributeValue;
    private final Map<String, Node> children;
    private String classLabel;
    
    public Node(int attributeIndex, String attributeValue, Map<String, Node> children, String classLabel) {
        this.attributeIndex = attributeIndex;
        this.attributeValue = attributeValue;
        this.children = children;
        this.classLabel = classLabel;
    }
    
    public Node(int classLabel) {
        this.attributeIndex = -1;
        this.attributeValue = null;
        this.children = null;
        this.classLabel = Integer.toString(classLabel);
    }
    
    public Node(int bestAttrIndex, Map<String, Node> children) {
        this.attributeIndex = bestAttrIndex;
        this.attributeValue = null;
        this.children = children;
        this.classLabel = null;
    }
    
    public int getAttributeIndex() {
        return attributeIndex;
    }
    
    public String getAttributeValue() {
        return attributeValue;
    }
    
    public void addChild(String attributeValue, Node child) {
        children.put(attributeValue, child);
    }
    
    public boolean isLeaf() {
        return classLabel != null;
    }
    
    public String getClassLabel() {
        return classLabel;
    }
    
    public void setClassLabel(String classLabel) {
        this.classLabel = classLabel;
    }
    
    public Node getChild(String attributeValue) {
        return children.get(attributeValue);
    }
}