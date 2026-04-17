import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * DoublingList.java
 *
 * A generic doubly-linked list where the j-th node (0-indexed) has an
 * array of length 2^j.  Implements the AbstractSequentialList interface.
 *
 * Invariants:
 *   - head and tail are dummy nodes with 0-length arrays.
 *   - Real node j has data.length == 2^j.
 *   - Elements inside a node are always packed from offset 0.
 *   - cap == 2^k - 1 for k real nodes.
 *   - Null elements are not permitted.
 *   
 *   @author Ali Kablanbek
 *   @version 4/15/26
 */
public class DoublingList<E> extends AbstractSequentialList<E> {

    // ================================================================
    //  Inner class: Node
    // ================================================================

    /**
     * One node of the doubling list.
     * data[0..count-1] are live elements; data[count..length-1] are null.
     */
    private class Node {
        E[]  data;    // element array; length = 2^j for the j-th node
        int  count;   // number of live elements currently stored
        Node prev;
        Node next;

        @SuppressWarnings("unchecked")
        Node(int capacity) {
            data  = (E[]) new Object[capacity];
            count = 0;
        }

        /** True when every array slot holds a live element. */
        boolean isFull() { return count == data.length; }

        /** Total array length (fixed capacity) of this node. */
        int capacity()   { return data.length; }
    }

    // ================================================================
    //  Inner class: NodeInfo
    // ================================================================

    /**
     * A (node, offset) pair identifying the location of one element.
     * offset is the index into node.data[].
     */
    private class NodeInfo {
        Node node;
        int  offset;
        NodeInfo(Node node, int offset) {
            this.node   = node;
            this.offset = offset;
        }
    }

    // ================================================================
    //  Fields
    // ================================================================

    private final Node head;  // dummy head (capacity 0)
    private final Node tail;  // dummy tail (capacity 0)
    private int        size;  // total live elements
    private int        cap;   // max capacity = 2^k - 1  for k real nodes

    // ================================================================
    //  Constructor
    // ================================================================

    /** Creates an empty doubling list. */
    public DoublingList() {
        head      = new Node(0);
        tail      = new Node(0);
        head.next = tail;
        tail.prev = head;
        size      = 0;
        cap       = 0;
    }

    // ================================================================
    //  AbstractSequentialList abstract methods
    // ================================================================

    @Override
    public int size() { return size; }

    /**
     * Returns a ListIterator positioned at logical index pos.
     * @throws IndexOutOfBoundsException if pos < 0 or pos > size
     */
    @Override
    public ListIterator<E> listIterator(int pos) {
        if (pos < 0 || pos > size)
            throw new IndexOutOfBoundsException("Index: " + pos);
        return new DoublingListIterator(pos);
    }

    // ================================================================
    //  iterator() — overridden to return DoublingIterator
    // ================================================================

    @Override
    public Iterator<E> iterator() {
        return new DoublingIterator();
    }

    // ================================================================
    //  find(pos) - O(log n) index lookup
    // ================================================================

    /**
     * Returns the NodeInfo for the element at logical index pos.
     * Skips entire nodes by their live-element count, giving O(log n) time.
     * pos == size is allowed; returns NodeInfo(tail, 0) as a sentinel.
     */
    private NodeInfo find(int pos) {
        if (pos == size) return new NodeInfo(tail, 0);

        Node cur  = head.next;
        int  seen = 0;
        while (cur != tail) {
            if (seen + cur.count > pos)
                return new NodeInfo(cur, pos - seen);
            seen += cur.count;
            cur   = cur.next;
        }
        return new NodeInfo(tail, 0); // unreachable for valid pos
    }

    // ================================================================
    //  addNewNode() - appends a new real node before tail
    // ================================================================

    /**
     * Appends a new real node before tail and updates cap.
     * The new node has capacity = cap + 1 = 2^k  (for k existing real nodes),
     * which follows from the identity  1 + 2 + ... + 2^(k-1) = 2^k - 1 = cap.
     */
    private Node addNewNode() {
        int  newCap = (cap == 0) ? 1 : cap + 1;  // 2^k
        Node n      = new Node(newCap);
        n.prev         = tail.prev;
        n.next         = tail;
        tail.prev.next = n;
        tail.prev      = n;
        cap += newCap;   // cap becomes 2^(k+1) - 1
        return n;
    }

    // ================================================================
    //  removeLastNode() - removes the node immediately before tail
    // ================================================================

    /**
     * Removes the last real node (immediately before tail) and updates cap.
     * The node should be empty before calling this.
     */
    private void removeLastNode() {
        Node last = tail.prev;
        if (last == head) return;
        cap            -= last.capacity();
        last.prev.next  = tail;
        tail.prev       = last.prev;
    }

    // ================================================================
    //  nodeCount() - counts real nodes
    // ================================================================

    /** Returns the number of real (non-dummy) nodes. */
    private int nodeCount() {
        int  k   = 0;
        Node cur = head.next;
        while (cur != tail) { k++; cur = cur.next; }
        return k;
    }

    // ================================================================
    //  shiftLeft(nodeR, offsetI)
    // ================================================================

    /**
     * Leftward shift at logical index i (where element i lives at
     * nodeR.data[offsetI]).
     *
     * Preconditions:
     *   - nodeR is full.
     *   - Some predecessor of nodeR has at least one empty slot.
     *
     * Effect:
     *   Finds the rightmost predecessor t of nodeR that has a free slot.
     *   Pulls every element in nodes [t.next .. nodeR] that has logical
     *   index <= i one step to the left, across node boundaries.
     *   This creates a free slot at offsetI inside nodeR.
     *
     *   After the call:
     *     nodeR.data[offsetI] == null   (ready to receive the new element)
     *     nodeR.count is unchanged      (caller does data[offsetI]=item; count++)
     */
    private void shiftLeft(Node nodeR, int offsetI) {
        // Find the rightmost predecessor of nodeR that has room
        Node t = nodeR.prev;
        while (t != head && t.isFull()) t = t.prev;
        // t.data[t.count] is the first free slot in t

        // Pull elements left one node-pair at a time from t toward nodeR.
        // For each pair (cur, nxt = cur.next) where cur != nodeR:
        //   • move nxt.data[0] into cur.data[cur.count]  (fills the free slot in cur)
        //   • shift nxt left by 1 within nxt (compresses nxt, freeing its last used slot)
        Node cur = t;
        while (cur != nodeR) {
            Node nxt = cur.next;
            cur.data[cur.count] = nxt.data[0];   // pull first of nxt into free slot of cur
            cur.count++;
            // shift nxt's live elements left by 1
            System.arraycopy(nxt.data, 1, nxt.data, 0, nxt.count - 1);
            nxt.data[nxt.count - 1] = null;
            nxt.count--;
            cur = nxt;
        }
        // After the loop, nodeR.count has been decremented by 1.
        // nodeR.data[0..count-1] are live; nodeR.data[count] == null (free slot).
        // We need the free slot at offsetI, so shift data[offsetI..count-1]
        // one position RIGHT within nodeR to open the slot at offsetI.
        System.arraycopy(nodeR.data, offsetI, nodeR.data, offsetI + 1,
                         nodeR.count - offsetI);
        nodeR.data[offsetI] = null;
        // nodeR.count was decremented in the loop; caller will do count++ after insert.
    }

    // ================================================================
    //  shiftRight(nodeR, offsetI)
    // ================================================================

    /**
     * Rightward shift at logical index i (element i is at nodeR.data[offsetI]).
     *
     * Preconditions:
     *   - nodeR is full.
     *   - Some successor of nodeR has at least one empty slot.
     *
     * Effect:
     *   Finds the leftmost successor t of nodeR that has a free slot.
     *   Pushes every element in nodes [nodeR .. t] that has logical index
     *   >= i one step to the right, across node boundaries.
     *   This creates a free slot at offsetI inside nodeR.
     *
     *   After the call:
     *     nodeR.data[offsetI] == null   (ready to receive the new element)
     *     nodeR.count is unchanged      (caller does data[offsetI]=item; count++)
     */
    private void shiftRight(Node nodeR, int offsetI) {
        // Find leftmost successor of nodeR that has room
        Node t = nodeR.next;
        while (t != tail && t.isFull()) t = t.next;
        // t has a free slot at data[t.count]

        // Push elements right one node-pair at a time from t backward toward nodeR.
        // For each pair (prv = cur.prev, cur) where cur != nodeR:
        //   • shift cur right by 1 to make room at index 0
        //   • move prv.data[prv.count-1] into cur.data[0]
        //   • clear prv's last slot
        Node cur = t;
        while (cur != nodeR) {
            Node prv = cur.prev;
            // shift cur's live elements right by 1
            System.arraycopy(cur.data, 0, cur.data, 1, cur.count);
            cur.data[0] = prv.data[prv.count - 1];  // pull last of prv into cur[0]
            cur.count++;
            prv.data[prv.count - 1] = null;
            prv.count--;
            cur = prv;
        }
        // After the loop, nodeR.count has been decremented by 1.
        // We need a free slot at offsetI, so shift data[offsetI..count-1]
        // one position RIGHT within nodeR to open the slot at offsetI.
        System.arraycopy(nodeR.data, offsetI, nodeR.data, offsetI + 1,
                         nodeR.count - offsetI);
        nodeR.data[offsetI] = null;
        // nodeR.count was decremented in the loop; caller will do count++ after insert.
    }

    // ================================================================
    //  compact()
    // ================================================================

    /**
     * Compacts the list from k real nodes to k-1 real nodes.
     *
     * Called only when size <= 2^(k-2) - 1 after a removal (k >= 2).
     *
     * Post-condition:
     *   - k-1 real nodes remain.
     *   - Nodes 0 .. k-3 are completely full.
     *   - Node k-2 is completely empty.
     *   - cap = 2^(k-1) - 1.
     */
    private void compact() {
        // Step 1: gather all live elements in order
        @SuppressWarnings("unchecked")
        E[] elems = (E[]) new Object[size];
        int idx   = 0;
        for (Node cur = head.next; cur != tail; cur = cur.next)
            for (int i = 0; i < cur.count; i++)
                elems[idx++] = cur.data[i];

        // Step 2: remove the last real node
        removeLastNode();

        // Step 3: repack remaining nodes front-to-back.
        //   Fill nodes 0..k-3 to capacity; leave node k-2 (last) empty.
        idx = 0;
        for (Node cur = head.next; cur != tail; cur = cur.next) {
            // Clear the node
            for (int i = 0; i < cur.data.length; i++) cur.data[i] = null;
            cur.count = 0;
            // Fill to capacity only if this is NOT the last real node
            if (cur.next != tail) {
                for (int i = 0; i < cur.data.length; i++) {
                    cur.data[i] = elems[idx++];
                    cur.count++;
                }
            }
            // The last real node (cur.next == tail) stays empty
        }
    }

    // ================================================================
    //  add(E item) - append to end
    // ================================================================

    /**
     * Appends item to the end of the list (equivalent to add(size, item)).
     * @throws NullPointerException if item is null
     */
    @Override
    public boolean add(E item) {
        if (item == null) throw new NullPointerException("Null elements not allowed.");
        add(size, item);
        return true;
    }

    // ================================================================
    //  add(int pos, E item)
    // ================================================================

    /**
     * Inserts item at logical index pos, shifting existing elements right.
     * Implements Cases 1 and 2 from Section 3.2 of the specification.
     *
     * @throws NullPointerException      if item is null
     * @throws IndexOutOfBoundsException if pos < 0 or pos > size
     */
    @Override
    public void add(int pos, E item) {
        if (item == null)
            throw new NullPointerException("Null elements not allowed.");
        if (pos < 0 || pos > size)
            throw new IndexOutOfBoundsException("Index: " + pos);

        // ---- Case 2: list is completely full (size == cap) ----
        if (size == cap) {
            Node newNode = addNewNode();   // new node has capacity 2^k
            if (pos == size) {
                // Figure 9: append -first slot of the new node
                newNode.data[0] = item;
                newNode.count   = 1;
            } else {
                // rightward shift into the new (empty) node, then insert
                NodeInfo ni = find(pos);
                shiftRight(ni.node, ni.offset);
                ni.node.data[ni.offset] = item;
                ni.node.count++;
            }
            size++;
            return;
        }

        // ---- cap == 0: absolutely empty list ----
        // (Case 1, step 2 — Figure 4)
        if (cap == 0) {
            Node n    = addNewNode();  // capacity 1
            n.data[0] = item;
            n.count   = 1;
            size++;
            return;
        }

        // ---- Case 1: size < cap and cap > 0 ----

        if (pos == size) {
            // Sub-case 4: appending at the end
            // Find the last non-empty node (£ in the spec)
            Node last = tail.prev;
            while (last != head && last.count == 0) last = last.prev;

            if (last == head) {
                // All real nodes exist but are empty - put in first slot of first node
                head.next.data[0] = item;
                head.next.count   = 1;
                size++;
                return;
            }

            if (!last.isFull()) {
                // Figure 8(a-b): last non-empty node has room
                last.data[last.count] = item;
                last.count++;
                size++;
                return;
            }

            // last is full - check whether any predecessor has room
            boolean predHasRoom = false;
            for (Node t = last.prev; t != head; t = t.prev) {
                if (!t.isFull()) { predHasRoom = true; break; }
            }

            if (predHasRoom) {
                // Figure 8(c-d): leftward shift at pos = size
                // offsetI = last.count means "open the slot just after the last element"
                shiftLeft(last, last.count);
                last.data[last.count] = item;
                last.count++;
            } else {
                // Figure 8(e): all nodes up to last are full;
                // successor of last is empty - put X in first slot there
                Node succ     = last.next;  // guaranteed to exist and be empty
                succ.data[0]  = item;
                succ.count    = 1;
            }
            size++;
            return;
        }

        // Sub-case 3: pos < size
        NodeInfo ni    = find(pos);
        Node     nodeR = ni.node;
        int      offI  = ni.offset;

        if (!nodeR.isFull()) {
            // Figure 5: node has room - shift [offI..count-1] right, insert at offI
            System.arraycopy(nodeR.data, offI, nodeR.data, offI + 1,
                             nodeR.count - offI);
            nodeR.data[offI] = item;
            nodeR.count++;
            size++;
            return;
        }

        // nodeR is full - check predecessors for leftward shift
        boolean predHasRoom = false;
        for (Node t = nodeR.prev; t != head; t = t.prev) {
            if (!t.isFull()) { predHasRoom = true; break; }
        }

        if (predHasRoom) {
            // Figure 6: leftward shift, slot opens at offI in nodeR
            shiftLeft(nodeR, offI);
        } else {
            // Figure 7: rightward shift, slot opens at offI in nodeR
            shiftRight(nodeR, offI);
        }
        nodeR.data[offI] = item;
        nodeR.count++;
        size++;
    }

    // ================================================================
    //  remove(int pos)
    // ================================================================

    /**
     * Removes and returns the element at logical index pos.
     * Triggers compaction when the occupancy constraint size <= 2^(k-2)-1
     * is violated (Section 3.3 of the specification).
     *
     * @throws IndexOutOfBoundsException if pos < 0 or pos >= size
     */
    @Override
    public E remove(int pos) {
        if (pos < 0 || pos >= size)
            throw new IndexOutOfBoundsException("Index: " + pos);

        NodeInfo ni    = find(pos);
        Node     nodeR = ni.node;
        int      offJ  = ni.offset;

        // Step 2: save and null-out the element
        E x = nodeR.data[offJ];
        nodeR.data[offJ] = null;
        size--;

        // Step 3: if list is now empty, reset to bare state
        if (size == 0) {
            while (head.next != tail) removeLastNode();
            return x;
        }

        // Step 4: shift elements after offJ left within nodeR
        System.arraycopy(nodeR.data, offJ + 1, nodeR.data, offJ,
                         nodeR.count - offJ - 1);
        nodeR.data[nodeR.count - 1] = null;
        nodeR.count--;

        // Step 5: compaction check - size <= 2^(k-2) - 1 ?
        int k = nodeCount();
        if (k >= 2 && size <= (1 << (k - 2)) - 1) {
            compact();
        }

        return x;
    }

    // ================================================================
    //  toStringInternal()
    // ================================================================

    /**
     * Returns the internal node structure as a string.
     * Live elements print normally; null slots print as "-".
     *
     * Example: [(A), (B, -), (C, D, E, -), (F, G, H, -, -, -, -, -)]
     */
    public String toStringInternal() {
        StringBuilder sb  = new StringBuilder("[");
        Node          cur = head.next;
        while (cur != tail) {
            sb.append("(");
            for (int i = 0; i < cur.data.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(cur.data[i] == null ? "-" : cur.data[i].toString());
            }
            sb.append(")");
            if (cur.next != tail) sb.append(", ");
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // ================================================================
    //  toStringInternal(ListIterator)
    // ================================================================

    /**
     * Returns the internal node structure with a cursor marker "|"
     * inserted immediately before the element at iter.nextIndex().
     *
     * The cursor sits between live elements; null slots are never the
     * cursor target.  The "|" appears before the element that next()
     * would return.
     *
     * Example (nextIndex == 3):
     *   [(A), (B, -), (C, | D, E, -), (F, G, H, -, -, -, -, -)]
     */
    public String toStringInternal(ListIterator<E> iter) {
        int           cursorPos  = iter.nextIndex(); // logical index of next element
        StringBuilder sb         = new StringBuilder("[");
        Node          cur        = head.next;
        int           logIdx     = 0;  // logical index of each live element

        while (cur != tail) {
            sb.append("(");
            for (int i = 0; i < cur.data.length; i++) {
                if (i > 0) sb.append(", ");

                // Insert cursor marker BEFORE the element at cursorPos
                if (cur.data[i] != null && logIdx == cursorPos) {
                    sb.append("| ");
                }

                if (cur.data[i] == null) {
                    sb.append("-");
                } else {
                    sb.append(cur.data[i].toString());
                    logIdx++;
                }
            }
            sb.append(")");
            if (cur.next != tail) sb.append(", ");
            cur = cur.next;
        }

        // Handle cursor at position == size (end of list)
        if (cursorPos == size) {
            int last = sb.lastIndexOf(")");
            if (last >= 0) sb.insert(last, " |");
        }

        sb.append("]");
        return sb.toString();
    }

    // ================================================================
    //  Inner class: DoublingIterator  (forward only)
    // ================================================================

    /**
     * A basic forward Iterator over the doubling list.
     * remove() throws UnsupportedOperationException as permitted by the spec.
     */
    private class DoublingIterator implements Iterator<E> {
        private Node curNode;
        private int  curOffset;
        private int  remaining;

        DoublingIterator() {
            curNode   = head.next;
            curOffset = 0;
            remaining = size;
        }

        @Override
        public boolean hasNext() { return remaining > 0; }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            // Advance past exhausted nodes
            while (curNode != tail && curOffset >= curNode.count) {
                curNode   = curNode.next;
                curOffset = 0;
            }
            E val = curNode.data[curOffset++];
            remaining--;
            return val;
        }

        /** Not supported — throws UnsupportedOperationException. */
        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                "DoublingIterator does not support remove().");
        }
    }

    // ================================================================
    //  Inner class: DoublingListIterator  (full ListIterator)
    // ==================================================================

    /**
     * Full bidirectional ListIterator for DoublingList.
     *
     * Cursor model (standard Java ListIterator):
     *   nextIndex()     = logical index of the element next() will return.
     *   previousIndex() = nextIndex() - 1.
     *   The cursor conceptually sits between elements.
     *
     * lastRet:
     *   Logical index of the element returned by the most recent next()
     *   or previous() call.  -1 means no last element (fresh iterator,
     *   or set()/add()/remove() was just called).
     *
     * lastWasNext:
     *   True if the last movement was next(); false if previous().
     *   Used by remove() to decide which direction to shift the cursor.
     */
    private class DoublingListIterator implements ListIterator<E> {

        private Node    curNode;      // node of the NEXT element
        private int     curOffset;    // offset inside curNode of the next element
        private int     nextIdx;      // logical index of next element (= cursor)
        private int     lastRet;      // last returned logical index; -1 = none
        private boolean lastWasNext;  // true => last move was next()

        DoublingListIterator(int pos) {
            nextIdx = pos;
            lastRet = -1;
            if (pos == size) {
                curNode   = tail;
                curOffset = 0;
            } else {
                NodeInfo ni = find(pos);
                curNode     = ni.node;
                curOffset   = ni.offset;
            }
        }

        // ---- navigation queries ----

        @Override public boolean hasNext()       { return nextIdx < size;  }
        @Override public boolean hasPrevious()   { return nextIdx > 0;     }
        @Override public int     nextIndex()     { return nextIdx;         }
        @Override public int     previousIndex() { return nextIdx - 1;     }

        // ---- next() ----

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            // Skip exhausted nodes
            while (curNode != tail && curOffset >= curNode.count) {
                curNode   = curNode.next;
                curOffset = 0;
            }
            E val       = curNode.data[curOffset];
            lastRet     = nextIdx;
            lastWasNext = true;
            nextIdx++;
            curOffset++;
            return val;
        }

        // ---- previous() ----

        @Override
        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            nextIdx--;
            if (curOffset > 0) {
                curOffset--;
            } else {
                // Jump to the previous real node's last live element
                curNode = curNode.prev;
                while (curNode != head && curNode.count == 0)
                    curNode = curNode.prev;
                curOffset = curNode.count - 1;
            }
            E val       = curNode.data[curOffset];
            lastRet     = nextIdx;
            lastWasNext = false;
            return val;
        }

        // ---- set() ----

        /**
         * Replaces the last element returned by next() or previous().
         * @throws IllegalStateException if no last element
         * @throws NullPointerException  if item is null
         */
        @Override
        public void set(E item) {
            if (item == null) throw new NullPointerException("Null not allowed.");
            if (lastRet < 0)  throw new IllegalStateException();
            NodeInfo ni = find(lastRet);
            ni.node.data[ni.offset] = item;
        }

        // ---- add() ----

        /**
         * Inserts item before the element next() would return, then
         * advances the cursor past it.
         * @throws NullPointerException if item is null
         */
        @Override
        public void add(E item) {
            if (item == null) throw new NullPointerException("Null not allowed");
            DoublingList.this.add(nextIdx, item);
            nextIdx++;   // cursor advances past the newly inserted element
            lastRet = -1;
            syncCursor();
        }

        // ---- remove() ----

        /**
         * Removes the element returned by the most recent next() or previous().
         * Updates the cursor correctly based on direction and whether compaction
         * occurred.
         * @throws IllegalStateException if no last element
         */
        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            DoublingList.this.remove(lastRet);
            // If last move was next(): element before cursor was removed -> cursor back
            // If last move was previous(): element after cursor was removed -> cursor stays
            if (lastWasNext) nextIdx--;
            lastRet = -1;
            syncCursor();
        }

        // ---- syncCursor() - re-align (curNode,curOffset) with nextIdx ----

        /**
         * Re-synchronises the (curNode, curOffset) pointers to nextIdx.
         * Must be called after every structural modification (add/remove),
         * because compaction or expansion may have moved node boundaries.
         */
        private void syncCursor() {
            if (nextIdx >= size) {
                curNode   = tail;
                curOffset = 0;
            } else {
                NodeInfo ni = find(nextIdx);
                curNode     = ni.node;
                curOffset   = ni.offset;
            }
        }
    }
}