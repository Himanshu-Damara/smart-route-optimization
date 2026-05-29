package com.sro.datastructure;

import java.util.*;

/**
 * Min-Heap implementation for priority queue operations
 * Used for managing deliveries by priority and vehicle availability
 * Time Complexity: O(log n) for insert and extract
 */
public class Heap<T extends Comparable<T>> {
    private List<T> heap;

    public Heap() {
        this.heap = new ArrayList<>();
    }

    /**
     * Insert element into heap
     * Time: O(log n)
     */
    public void insert(T element) {
        heap.add(element);
        bubbleUp(heap.size() - 1);
    }

    /**
     * Extract minimum element from heap
     * Time: O(log n)
     */
    public T extractMin() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }

        T min = heap.get(0);

        // Move last element to root
        T last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            bubbleDown(0);
        }

        return min;
    }

    /**
     * Get minimum element without removing
     */
    public T peek() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return heap.get(0);
    }

    /**
     * Bubble up to maintain heap property
     */
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    /**
     * Bubble down to maintain heap property
     */
    private void bubbleDown(int index) {
        while (true) {
            int smallest = index;
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;

            if (leftChild < heap.size() && 
                heap.get(leftChild).compareTo(heap.get(smallest)) < 0) {
                smallest = leftChild;
            }

            if (rightChild < heap.size() && 
                heap.get(rightChild).compareTo(heap.get(smallest)) < 0) {
                smallest = rightChild;
            }

            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public List<T> getAll() {
        return new ArrayList<>(heap);
    }
}
