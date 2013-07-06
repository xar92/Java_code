/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package doublelinkedlist;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kcap
 */
public class DoubleLinkedListTest {

    public DoubleLinkedListTest() {
    }

    @Test
    public void testDeleteItem() {
        DoubleLinkedList<Integer> list = new DoubleLinkedList();
        for (int i = 0; i < 3; i++) {
            list.insertBegin(i, i * 10);
        }

        for (int i = 0; i < 3; i++) {
            assertEquals(true, list.deleteItem(i));
        }
        assertEquals( 0, list.getCount());
        assertEquals(false, list.deleteItem(2));
        assertEquals(false, list.deleteItem(-3));

    }

    @Test
    public void testInsertBegin() {
        DoubleLinkedList<Integer> list = new DoubleLinkedList();
        for (int i = 0; i < 3; i++) {
            list.insertBegin(i, i * 10);
        }
        assertEquals( 3, list.getCount());
        
        DoubleLinkedList.ListItem ptr = list.getBegin();
        for (int i = 2; i > -1; i--) {
            assertEquals(i, ptr.getKey());
            ptr = ptr.getNext();
        }

    }
    
    @Test
    public void testGetBegin() {
        DoubleLinkedList<Integer> list = new DoubleLinkedList();
        list.insertBegin(1, 1);
        list.insertBegin(2, 2);
        assertEquals( 2, list.getBegin().getKey());
        list.deleteItem(1);
        list.deleteItem(2);
        assertEquals( null, list.getBegin());
        assertEquals( null, list.findItem(2));
    }
    
    @Test
    public void testFindItem() {
        DoubleLinkedList<Integer> list = new DoubleLinkedList();
        for (int i = 0; i < 3; i++) {
            list.insertBegin(i, i * 10);
        }
        
        DoubleLinkedList.ListItem ptr = list.getBegin();
        for (int i = 2; i > -1; i--) {
            assertEquals(ptr, list.findItem(i));
            ptr = ptr.getNext();
        }
        
        ptr = list.getSentinel().getPrev();
        for (int i = 0; i < 3; i++) {
            assertEquals(ptr, list.findItem(i));
            ptr = ptr.getPrev();
        }
        
    }
}
    