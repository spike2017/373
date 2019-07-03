package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        back = new Node<T>(back, item, null);
        if (front == null) {
            front = back;
        }else {
            back.prev.next=back;
        }
        size++;
    }
    
    @Override
    public T remove() {
        if (front == null) {
            throw new EmptyContainerException();
        }
        return delete(size-1);
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        Node<T> current = front;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }
    
    @Override
    public void set(int index, T item) {
        checkIndex(index);
        if (size == 1) {
            front = new Node<T>(null, item, null);             
            back = front;
        } else if (index == 0){
            front = new Node<T>(null, item, front.next);
            front.next.prev = front;
        } else if (index == size-1) {
            back = new Node<T>(back.prev, item, null);
            back.prev.next = back;
        } else {
            Node<T> current = front;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            //different
            current = new Node<T>(current.prev, item, current.next);
            current.prev.next = current;
            current.next.prev = current;
        }
    }

    
    @Override
    public void insert(int index, T item) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        if (index ==size) {
            add(item);
        } else if (index == 0) {
            Node<T> insert = new Node<T>(null, item, front);
            front.prev = insert;
            front = insert;
            size++;
        } else if (index <= size / 2) {//should I use iterator??
            Node<T> current = front;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            insertNode(current, item);
        } else {
            Node<T> current = back;
            for (int i = size - index; i > 0; i--) {
                current = current.prev;
            }
            insertNode(current, item);
        }
    }

    private void insertNode(Node<T> current, T item) {
        current.next = new Node<T>(current, item, current.next);
        current.next.next.prev = current.next;
        size++;
    }
    
    @Override
    public T delete(int index) {
        checkIndex(index);
        T returnValue = get(index);
        if (size == 1){
            front = null;
            back = null;
        } else if (index==0) {
            front = front.next;
            front.prev = null;
        } else if (index == size-1) {
            back = back.prev;
            back.next = null;
        } else {
            Node<T> current = front;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }
        size--;
        return returnValue;
    }

    @Override
    public int indexOf(T item) {
        Node<T> current = front;
        if (item == null) {
            for (int i = 0; i < size; i++) {
                if (current.data == null) {
                    return i;
                }
                current = current.next;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (item.equals(current.data)) {
                    return i;
                }
                current = current.next;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        return indexOf(other) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;
        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }
    //static???? 
    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T temp = current.data;
            current = current.next;
            return temp;
        }
    }
}
