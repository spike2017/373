package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {
    @Test(timeout=SECOND)
    public void basicTest() {//test size
        // Feel free to modify or delete this dummy test.
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(-10);
        list.add(-20);
        list.add(30);
        list.add(40);
        list.delete(1);
        this.assertListMatches(new Integer[]{-10,30,40}, list);
    }
    
    @Test(timeout=SECOND)
    public void nullTest() {
        IList<Integer> list = new DoubleLinkedList<>();
        try {
            list.delete(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // This is ok: do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void boundary() {
        IList<Integer> list = new DoubleLinkedList<>();
        for(int i=0;i<=50;i++) {
            list.add(i);
        }
        list.delete(0);
        assertEquals(list.get(0),1);
        list.delete(49);
        assertEquals(list.get(48),49);
        try {
            list.get(49);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // This is ok: do nothing
        }
    }
    


}


