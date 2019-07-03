package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        //should I do this or there is other way for 100000;
        this.pairs = makeArrayOfPairs(1000);
    }
    public ArrayDictionary(int index) {
        this.pairs = makeArrayOfPairs(index);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    //if I throw method ahead, I will get an error
    @Override
    public V get(K key) {
        int index = findIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        return pairs[index].value;
    }

    @Override
    public void put(K key, V value) {
        //how to output something
        int index = findIndex(key);
        if ( index != -1) {
            pairs[index].value = value;
        } else  {
            if(size == pairs.length) {
                Pair<K, V>[] temp = makeArrayOfPairs(2 * pairs.length);
                for (int i = 0; i < size; i++) {
                    temp[i] = pairs[i];
                }
                this.pairs = temp;
            }
            Pair<K, V> current = new Pair<K, V>(key, value);
            pairs[size] = current;
            size++;
        }
    }
    
    @Override
    public V remove(K key) {
        int index = findIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        } 
        V curValue = pairs[index].value;
        pairs[index] = pairs[size - 1];
        size--;
        return curValue;  
    }
    
    public int findIndex(K key) {
        if (key == null) {
            for (int i = 0; i < size; i++) {
                if (key == (pairs[i].key)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (key.equals(pairs[i].key)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean containsKey(K key) {
        return findIndex(key) >= 0;
    }

    @Override
    public int size() {
        return size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
