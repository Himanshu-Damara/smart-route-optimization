package com.sro.datastructure;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Node for Binary Search Tree
 */
@Data
@AllArgsConstructor
public class BSTNode<T extends Comparable<T>> {
    private T data;
    private BSTNode<T> left;
    private BSTNode<T> right;
    private int height;

    public BSTNode(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}
