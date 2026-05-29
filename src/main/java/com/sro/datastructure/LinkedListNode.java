package com.sro.datastructure;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Node for linked list implementation
 */
@Data
@AllArgsConstructor
public class LinkedListNode<T> {
    private T data;
    private LinkedListNode<T> next;
    private LinkedListNode<T> previous; // For doubly linked list

    public LinkedListNode(T data) {
        this.data = data;
        this.next = null;
        this.previous = null;
    }
}
