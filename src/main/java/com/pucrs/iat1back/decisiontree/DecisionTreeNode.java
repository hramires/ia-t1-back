package com.pucrs.iat1back.decisiontree;

public abstract class DecisionTreeNode {

    protected int attributeIndex;

    public DecisionTreeNode(int attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public abstract boolean isLeaf();

    public abstract DecisionTreeNode getChild(char attributeValue);

    public abstract void addChild(char attributeValue, DecisionTreeNode childNode);

    public abstract String getLabel();
}
