/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package doublelinkedlist;

/**
 *New version
 * 
 * @author Kcap
 */
public class DoubleLinkedList<T> {
    private int justForFun_;
    private int count_;
    private ListItem<T> begin_;
    private ListItem<T> sentinel_;

    DoubleLinkedList() {
        sentinel_ = new ListItem(-1, 0);
        sentinel_.setNext(null);
        sentinel_.setPrev(null);
        begin_ = sentinel_;
        count_ = 0;
    }

    public ListItem<T> getSentinel() { 
        return sentinel_;
    }
    
    public int getCount() {
        return count_;
    }

    public ListItem<T> getBegin() {
        if (count_ == 0) {
            return null;
        }
        return begin_;
    }

    public void insertBegin(int key, T data) {
        ListItem<T> newElem = new ListItem(key, data);
        newElem.setNext(begin_);
        begin_.setPrev(newElem);
        begin_ = newElem;
        count_++;
    }

    public void insertAfter(ListItem<T> pointer, int key, T data) {
        ListItem<T> newElem = new ListItem(key, data);
        newElem.setNext(pointer.getNext());
        newElem.setPrev(pointer);
        pointer.setNext(newElem);
        newElem.getNext().setPrev(newElem);
        count_++;
    }

    public boolean deleteItem(int key) {
        if (count_ == 0) {
            return false;
        }
        if (begin_.getKey() == key) {
            begin_ = begin_.getNext();
            begin_.setPrev(null);
            count_--;
            return true;
        }
        ListItem<T> pointer = findItem(key);
        if (pointer == null) {
            return false;
        }
        pointer.getPrev().setNext(pointer.getNext());
        if (pointer.getNext() != null) {
            pointer.getNext().setPrev(pointer.getPrev());
        }
        count_--;
        return true;

    }

    public void insertBefore(ListItem<T> pointer, int key, T data) {
        if (pointer == begin_) {
            insertBegin(key, data);
            return;
        }
        ListItem<T> newElem = new ListItem(key, data);
        newElem.setNext(pointer);
        newElem.setPrev(pointer.getPrev());
        pointer.getPrev().setNext(newElem);
        pointer.setPrev(newElem);
        count_++;
    }

    public void printList() {
        if (count_ == 0) {
            System.out.println("List is empty");
        } else {
            ListItem ptr = begin_;
            while (ptr != sentinel_) {
                System.out.println("node " + ptr.getKey() + " = " + ptr.getData());
                ptr = ptr.getNext();
            }
            System.out.println("Count of nodes: " + count_);
        }
    }

    public ListItem<T> findItem(int key) {
        ListItem<T> ptr = begin_;
        while (ptr != sentinel_ && ptr.getKey() != key) {
            ptr = ptr.getNext();
        }
        if (ptr != sentinel_) {
            return ptr;
        } else {
            return null;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DoubleLinkedList<Integer> list = new DoubleLinkedList();
        for (int i = 0; i < 10; i++) {
            list.insertBegin(i, i * 10);
        }
        list.insertAfter(list.findItem(5), 11, 22);
        list.insertBefore(list.findItem(5), 10, 33);
        list.deleteItem(9);
        list.printList();
    }

    class ListItem<T> {

        private int key_;
        private T data_;
        private ListItem<T> next_;
        private ListItem<T> prev_;

        public ListItem(Integer key, T data) {
            this.key_ = key;
            this.data_ = data;
            next_ = prev_ = null;
        }

        public void setKey(int key) {
            this.key_ = key;
        }

        public void setData(T data) {
            this.data_ = data;
        }

        public void setNext(ListItem<T> next) {
            this.next_ = next;
        }

        public void setPrev(ListItem<T> prev) {
            this.prev_ = prev;
        }

        public int getKey() {
            return key_;
        }

        public T getData() {
            return data_;
        }

        public ListItem<T> getNext() {
            return next_;
        }

        public ListItem<T> getPrev() {
            return prev_;
        }
    }
}
