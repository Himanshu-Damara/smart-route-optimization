package com.sro.datastructure;

import java.util.*;

/**
 * Doubly Linked List implementation
 * Used for maintaining delivery sequence in routes
 * Time Complexity: O(1) insertion/deletion at known position
 */
public class LinkedList<T> implements Iterable<T> {
    private LinkedListNode<T> head;
    private LinkedListNode<T> tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Add element at the end
     * Time: O(1)
     */
    public void add(T data) {
        LinkedListNode<T> newNode = new LinkedListNode<>(data);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
        }
        size++;
    }

    /**
     * Add element at specific index
     * Time: O(n)
     */
    public void add(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        if (index == size) {
            add(data);
            return;
        }

        LinkedListNode<T> newNode = new LinkedListNode<>(data);
        LinkedListNode<T> current = getNodeAt(index);

        newNode.setNext(current);
        newNode.setPrevious(current.getPrevious());

        if (current.getPrevious() != null) {
            current.getPrevious().setNext(newNode);
        } else {
            head = newNode;
        }

        current.setPrevious(newNode);
        size++;
    }

    /**
     * Remove element at index
     * Time: O(n)
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        LinkedListNode<T> current = getNodeAt(index);
        T data = current.getData();

        if (current.getPrevious() != null) {
            current.getPrevious().setNext(current.getNext());
        } else {
            head = current.getNext();
        }

        if (current.getNext() != null) {
            current.getNext().setPrevious(current.getPrevious());
        } else {
            tail = current.getPrevious();
        }

        size--;
        return data;
    }

    /**
     * Get element at index
     * Time: O(n)
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        return getNodeAt(index).getData();
    }

    /**
     * Get node at index
     */
    private LinkedListNode<T> getNodeAt(int index) {
        LinkedListNode<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrevious();
            }
        }
        return current;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private LinkedListNode<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        LinkedListNode<T> current = head;
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}
