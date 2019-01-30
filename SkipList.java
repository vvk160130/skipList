import java.util.Iterator;
import java.util.Random;
import java.lang.Integer;
import java.util.NoSuchElementException;

//package vvk160130

/**
 * @author      Vishnu Kunadharaju
 * NetID       vvk160130
 * Purpose     Skip List Implementation Library
 * */

public class SkipList<T extends Comparable<? super T>> {

    /**
     * Restricts the number of levels in a skip list
     */
    static final int PossibleLevels = 33;

    static class Entry<E> {
        E element;
        Entry<E>[] next;
        Entry<E> prev;


        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];

        }

        public E getElement() {
            return element;
        }
    }

    /* SkipList Class: */

    /**
     * The beginning of the skip list
     */
    Entry head;

    /**
     * The end of the skip list
     */
    Entry tail;

    /**
     * Stores the size of the skip list
     */
    int size;

    /**
     * Stores the highest level in a skip list at that time
     */
    int maxLevel;

    /**
     * Sets last[i] to node at which search came down from level i to i-1
     */
    Entry<T>[] last;

    /**
     * Generates a random number for a levels in skip list
     */
    Random random;



    public SkipList() {

        head = new Entry<T>(null, PossibleLevels);
        tail = new Entry<T>(null, PossibleLevels);

        /* We set each index of the next array in head to point to the
         * tail node  */

        for(int i = 0; i < PossibleLevels; i++){

            head.next[i] = tail;
            // new add


        }

        size = 0;
        maxLevel = 1;
        last = new Entry[PossibleLevels];
        random = new Random();
    }


    /**
     * helper method to search for x.
     *
     * <p>Sets last[i] = node at which search came down from level i to i -1
     *
     * @param x The element that find searches for
     * @return  Nothing
     * precond Head nodes point to tail nodes
     * postcon The locate array will contain an node from skip list
     */
    public void find(T x)
    {
        /**
         * Acts as a cursor and searches for the node that contains x
         */
        Entry<T> locate;

        locate = head;


        for(int i = maxLevel - 1; i >= 0; i--){

            /* You iterate through level i as long as the element value in a node is less than x and you did
             * not reach the tail element at level i. Then we proceed to level i-1 */


            while( locate.next[i] != null && locate.next[i].getElement() != null && locate.next[i].getElement().compareTo(x) < 0){

                locate = locate.next[i];
            }

            // last[i] is set to node at which search came down from level i to i-1

            last[i] = locate;
        }
    }


    /**
     * Chooses a level within skip list
     *
     * <p>Prob(choosing level i) = 1/2Prob(choosing level i-1)
     *
     * @param
     * @return  A number corresponding to a level in the skip list
     * @precond Level number is positive and greater than 0
     * @postcon The level will not exceed the maximum level of the skip list
     */

    public int chooseLevel(){

        int level;

        /* We are randomly generating a value for a new level in
         * the skip list between the range of 1 - 33 */

        level = 1 + Integer.numberOfTrailingZeros(random.nextInt());

        /* If the new level value is greater than the current level of the skip list then
        *  we update maxLevel value */

        if(level > maxLevel){

            maxLevel = level;
        }

        return level;
    }


    /**
     * Add x to list
     *
     * @param x The element being added to list
     * @return  True if new node is added, false otherwise
     * @precond Skip list is initialized with heads pointing to tail
     * @postcon Level of new node is not greater than max level
     */

    public boolean add(T x) {

            int level;

            /* If x is find within the list, we reject it */

            if (contains(x)) {
                return false;
            }

            // Determining the levels for new entry node in skip list

            level = chooseLevel();

            Entry ent = new Entry<T>(x, level);

            for (int i = 0; i <= level - 1; i++) {

                /* Inserting the new entry node into each level  */

                if( last[i] == null){

                    last[i] = head.next[i];

                }
                ent.next[i] = last[i].next[i];
                last[i].next[i] = ent;

            }

            /* Updating what prev for the node after ent to point to ent. */

            ent.next[0].prev = ent;

            /* Updating the prev for ent to point to the node before it */

            ent.prev = last[0];
            size = size + 1;
            return true;
    }

    /**
     * Finds smallest element that is greater or equal to x
     *
     * @param x The element we finding the ceiling for
     * @return  The floor of x in skip list, null if floor is not found
     * @precond Helper method find returns the correct node in last array
     * @postcon Skip list remains unaltered
     */

    public T ceiling(T x) {

        // If x is in the skip list, x is returned

        if(contains(x)){
            return last[0].next[0].getElement();
        }
        else{

            // if skip list is empty we return null

            if(isEmpty()){
                return null;
            }
            // if we reach the end of the list then ceiling does not exist
            else if(last[0].next[0].getElement()== null){
                return null;
            }
            else{
                return last[0].next[0].getElement();
            }
        }

    }

    /**
     * Does list contain x?
     *
     * @param x The element passed in by user
     * @return  true if element is within skip list, false otherwise
     * @precond find(x) helper method returns correct node in last array
     * @postcon The skip list remains unaltered
     */

    public boolean contains(T x) {

        find(x);

        // if list is empty then x is not in there

        if(isEmpty())
        {
            return false;
        }

        // if last[0] points to the tail element, x is not there

       if(last[0].next[0].element == null){
            return false;
        }
        else{

            // return true if node last[0] is pointing contains the element x

            return (x.compareTo(last[0].next[0].getElement()) == 0);
        }

    }

    /**
     * Return first element of list
     *
     * @param
     * @return  First element of list, null if skip list is empty
     * @precond
     * @postcon The skip list remains unaltered
     */

    public T first() {

        /* If the list is empty than we return null */

        if(isEmpty()){

            return null;
        }
        else{

            // Return the first element, located at where head.next[0]

            return (T)head.next[0].getElement();
        }


    }

    /**
     * Find largest element that is less than or equal to x
     *
     * @param x The element passed in by the user
     * @return  The floor of the element x in skip list, null if floor cannot be found in skip list
     * @precond find(x) helper method returns the correct node in last array
     * @postcon The skip list remains unaltered
     */
    public T floor(T x) {

        /* If x is found in the skip list then we return x as the floor */

        if(contains(x)){
            return last[0].next[0].getElement();
        }
        else{

            /* If x is not found in the list we proceed here  */

            // We check if skip list is empty

            if( isEmpty() ){
                return null;
            }
            // If last[0] is pointing to the head then the floor for x does not exist
            else if(  last[0].getElement() == null )
            {
                return null;
            }
            else{

                // Else the element that last[0] is pointing at is returned

                return last[0].getElement();
            }
        }
    }

    /**
     * Return element at index n of list
     *
     * <p> First element is at index 0
     *
     * @param n The index value passed in by user
     * @return  The element at for valdi index n, null for invalid index values
     * @precond The index value n is not less than 0 and greater than size of skip list
     * @postcon The skip list remains unaltered
     */

    public T get(int n) {

        /* If the index that is passed in, is less than 0 or greater than the
         * size of the list then we throw a new NoSuchElementException */

        if(n < 0 || n > size() - 1){

            return null;
        }

        Entry<T> p;

        p = head;

        /* The loop iterates until the desired index and the cursor p is moved forward */

        for(int i = 0; i <= n; i++){

           p = p.next[0];
        }

        return p.getElement();
    }

    /**
     * Is the list empty?
     *
     * @param
     * @return  True if skip list is empty, otherwise false
     * @precond Head node will be pointing to a node in the skip list
     * @postcon The skip list remains unaltered
     */

    public boolean isEmpty() {

        // if head it pointing to tail element then list is empty, return true

        return head.next[0].getElement() == null;
    }

    /**
     * Iterate through the elements of list in sorted order
     *
     * @param
     * @return  New iterator object
     * @precond The skip list is initiliazed properly. Head points to a node.
     * @postcon The skip list remains unaltered
     */

    public Iterator<T> iterator() {
        return new SKLIterator();
    }

    class SKLIterator implements Iterator<T>{

        /**
         * Acts a cursor to iterate through skip list
         */

        Entry<T> cursor;

        // iterator constructor

        SKLIterator(){
            cursor = head;
        }

        /**
         * Checks if next node in skip list is the tail node
         *
         * @param
         * @return  True if next node is not the tail node, else false
         * @precond The cursor starts at head node
         * @postcon The cursor ends at tail node
         */
        public boolean hasNext(){
            return cursor.next[0].getElement() != null;
        }

        /**
         * Return the element that cursor is pointing at
         *
         * @param
         * @return  A node in skip list
         * @precond hasNext() method returns true
         * @postcon The skip list remains unaltered
         */
        public T next(){

            cursor = cursor.next[0];
            return cursor.getElement();
        }

        public void remove(){


        }

    }


    /**
     * Return last element of list
     *
     * @param
     * @return  Returns the last element of skip list, otherwise null if skip list is empty
     * @precond The tail node prev property points back to a node in the skip list
     * @postcon The skip list remains unaltered
     */

    public T last() {

        // If the skip list is empty then we return null else we return the element that tail is pointing to

        if(isEmpty()){

            return null;
        }
        else{

            return (T)tail.prev.getElement();
        }

    }


    /**
     * Remove x from list
     *
     * @param x The element passed in from user that needs to be removed
     * @return  Removed element is returned, or null is returned if x is not in the list
     * @precond The find(x) helper method returns the proper node in the last array
     * @postcon Only one node will be removed granted x belongs in the skip list
     */

    public T remove(T x)  {

        /* If x is not in the list then return null  */

        if(contains(x) == false){

            return null;
        }

        Entry<T> ent;

        ent = last[0].next[0];

        /* Loop Invaraint - The number of levels that the ent node
         * appears in the skip list  */

        for(int i = 0; i <= ent.next.length-1; i++)
        {


            if(i == 0){

                /* If we are removing the last element
                * we by pass it and have the previous node point to
                * tail and tail point to the new node. */

                if(ent.next[0].getElement() == null){
                    last[0].next[0] = tail;
                    tail.prev = last[0];
                }
                else{

                    /* Similar operation as listed above
                     * except we are not removing the
                      * last element in the list */

                    last[0].next[0] = ent.next[0];
                    ent.next[0].prev = last[0];
                }


            }
            else{

                 //By pass the node that is being removed in the skip list

                last[i].next[i] = ent.next[i];
            }


        }

        size = size - 1;

        return ent.getElement();
    }


    /**
     * Returns the number of elements in the skip list
     *
     * @param
     * @return  Returns the number of elements in the list
     * @precond Size is 0 when list is empty
     * @postcon Size will be positive value 0 or greater
     */

    public int size() {

        return size;
    }
}
