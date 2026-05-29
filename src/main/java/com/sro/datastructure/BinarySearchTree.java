package com.sro.datastructure;

import java.util.*;

/**
 * Binary Search Tree implementation
 * Used for time window sorting and interval queries
 * Time Complexity: O(log n) average, O(n) worst case
 */
public class BinarySearchTree<T extends Comparable<T>> {
    private BSTNode<T> root;
    private int size;

    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Insert element into BST
     * Time: O(log n) average
     */
    public void insert(T data) {
        root = insertHelper(root, data);
    }

    private BSTNode<T> insertHelper(BSTNode<T> node, T data) {
        if (node == null) {
            size++;
            return new BSTNode<>(data);
        }

        int cmp = data.compareTo(node.getData());
        if (cmp < 0) {
            node.setLeft(insertHelper(node.getLeft(), data));
        } else if (cmp > 0) {
            node.setRight(insertHelper(node.getRight(), data));
        }
        // Ignore duplicates

        updateHeight(node);
        return node;
    }

    /**
     * Search for element
     * Time: O(log n) average
     */
    public boolean contains(T data) {
        return searchHelper(root, data);
    }

    private boolean searchHelper(BSTNode<T> node, T data) {
        if (node == null) {
            return false;
        }

        int cmp = data.compareTo(node.getData());
        if (cmp < 0) {
            return searchHelper(node.getLeft(), data);
        } else if (cmp > 0) {
            return searchHelper(node.getRight(), data);
        } else {
            return true;
        }
    }

    /**
     * Find minimum element
     */
    public T findMin() {
        if (root == null) {
            throw new NoSuchElementException("Tree is empty");
        }
        return findMinHelper(root).getData();
    }

    private BSTNode<T> findMinHelper(BSTNode<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Find maximum element
     */
    public T findMax() {
        if (root == null) {
            throw new NoSuchElementException("Tree is empty");
        }
        return findMaxHelper(root).getData();
    }

    private BSTNode<T> findMaxHelper(BSTNode<T> node) {
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    /**
     * In-order traversal (returns sorted elements)
     */
    public List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrderHelper(root, result);
        return result;
    }

    private void inOrderHelper(BSTNode<T> node, List<T> result) {
        if (node != null) {
            inOrderHelper(node.getLeft(), result);
            result.add(node.getData());
            inOrderHelper(node.getRight(), result);
        }
    }

    /**
     * Pre-order traversal
     */
    public List<T> preOrder() {
        List<T> result = new ArrayList<>();
        preOrderHelper(root, result);
        return result;
    }

    private void preOrderHelper(BSTNode<T> node, List<T> result) {
        if (node != null) {
            result.add(node.getData());
            preOrderHelper(node.getLeft(), result);
            preOrderHelper(node.getRight(), result);
        }
    }

    /**
     * Post-order traversal
     */
    public List<T> postOrder() {
        List<T> result = new ArrayList<>();
        postOrderHelper(root, result);
        return result;
    }

    private void postOrderHelper(BSTNode<T> node, List<T> result) {
        if (node != null) {
            postOrderHelper(node.getLeft(), result);
            postOrderHelper(node.getRight(), result);
            result.add(node.getData());
        }
    }

    /**
     * Update height of node
     */
    private void updateHeight(BSTNode<T> node) {
        int leftHeight = node.getLeft() == null ? 0 : node.getLeft().getHeight();
        int rightHeight = node.getRight() == null ? 0 : node.getRight().getHeight();
        node.setHeight(1 + Math.max(leftHeight, rightHeight));
    }

    /**
     * Get height of tree
     */
    public int getHeight() {
        return root == null ? 0 : root.getHeight();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
