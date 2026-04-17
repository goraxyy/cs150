------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all they need to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE:
    DoublingList - A Generic Doubling Linked List

PURPOSE OF PROJECT:
    This project implements a data structure called a "doubling list"
    which is a doubly-linked list with dummy head and tail nodes. Each
    successive real node has twice the storage capacity of its
    predecessor (node j holds 2^j elements). The implementation
    provides O(log n) index-based access, O(1) amortized append, and
    automatic expansion and compaction to maintain occupancy constraints.
    The project fulfills three learning objectives: working with linked
    data structures, mastering the Java List and ListIterator interfaces,
    and practicing debugging skills on a non-trivial data structure.

VERSION or DATE:
    4/15/26

HOW TO START THIS PROJECT:
    1. Open BlueJ and create a new project folder.
    2. Copy DoublingList.java into the project folder and open it in BlueJ.
    3. Copy DoublingListTest.java into the same folder for unit testing.
    4. Copy DoublingListDriver.java into the same folder for manual testing.
    5. Compile all classes using BlueJ's "Compile" button.
    6. To run unit tests, right-click DoublingListTest and select
       "Run Tests" (requires JUnit 4 on the BlueJ class path).
    7. To run the driver, right-click DoublingListDriver and invoke main().

AUTHORS:
    Ali Kablanbek

USER INSTRUCTIONS:
    - DoublingList<E> can be used like any standard Java List.
    - Create an instance: DoublingList<String> list = new DoublingList<>();
    - Add elements:       list.add("Hello");  or  list.add(0, "Hi");
    - Remove elements:    list.remove(0);
    - Iterate forward:    for (String s : list) { ... }
    - Iterate with full control: ListIterator<String> it = list.listIterator(0);
    - Null elements are NOT permitted; a NullPointerException is thrown.
    - toStringInternal() prints the raw node structure for debugging.
    - toStringInternal(iterator) additionally shows the cursor position.

    Problems encountered and how they were solved:
    - Correctly computing the capacity of the new node on expansion:
      the jth node must have capacity 2^j, so the new node always gets
      capacity equal to (current cap + 1), which equals 2^k for k existing
      nodes. This was derived from the identity 1+2+...+2^(k-1) = 2^k - 1.
    - Implementing leftward and rightward shifts across node boundaries
      required careful ordering: pull/push elements one pair of adjacent
      nodes at a time from the target node outward, then open the final
      slot inside the target node.
    - Compaction required collecting all live elements, removing the last
      node, then repacking into the remaining nodes from front to back,
      leaving only the final node empty.
    - The ListIterator cursor must be resynchronized after every
      structural modification (add/remove) because node boundaries
      may shift after compaction or expansion.

    Functionalities successfully implemented:
    - add(E), add(int, E), remove(int) without iterator
    - Expansion (Case 2) and all Case 1 sub-cases
    - List compaction on removal
    - DoublingIterator (forward only, remove unsupported)
    - DoublingListIterator (full: next, previous, add, remove, set)
    - toStringInternal() and toStringInternal(ListIterator)
    - Full JUnit test suite covering all specification figures

    What Ive learned:
    - How doubling arrays in a linked structure achieve amortized O(1)
      append while keeping O(log n) nodes.
    - How to carefully implement cross-node shifts without corrupting
      the packed-from-zero invariant.
    - The importance of maintaining a clean cursor abstraction in
      ListIterator, especially across compactions and expansions.