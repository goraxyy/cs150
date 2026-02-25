import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the Word and WordList classes.
 *
 * Testing strategy:
 *  - Word tests cover construction, getters, increment(), toString(), and compareTo().
 *  - WordList tests cover construction (stop word filtering, duplicate handling),
 *    search(), getMostFrequent(), and topKMostFrequent().
 *  - Edge cases are tested: empty inputs, k larger than list size, all stop words, etc.
 */
public class Lab04Tests {

    // ====================================================================
    // Shared helpers
    // ====================================================================

    /** Returns a simple stop-words list with "the", "and", "of". */
    private ArrayList<String> basicStopWords() {
        ArrayList<String> sw = new ArrayList<>();
        sw.add("the");
        sw.add("and");
        sw.add("of");
        return sw;
    }

    // ====================================================================
    // Word - constructor & getters
    // ====================================================================

    /**
     * A newly constructed Word should store the supplied string
     * and start with a frequency of 1.
     */
    @Test
    public void testWordConstructorAndGetters() {
        Word w = new Word("hello");
        assertEquals("hello", w.getWord(),
                "getWord() should return the word passed to the constructor");
        assertEquals(1, w.getFrequency(),
                "Initial frequency should be 1");
    }

    // ====================================================================
    // Word - increment
    // ====================================================================

    /**
     * Calling increment() once should raise frequency from 1 to 2.
     */
    @Test
    public void testIncrementOnce() {
        Word w = new Word("test");
        w.increment();
        assertEquals(2, w.getFrequency(), "After one increment frequency should be 2");
    }

    /**
     * Multiple increments should accumulate correctly.
     */
    @Test
    public void testIncrementMultipleTimes() {
        Word w = new Word("repeat");
        for (int i = 0; i < 9; i++) {
            w.increment();
        }
        assertEquals(10, w.getFrequency(),
                "After 9 increments starting at 1, frequency should be 10");
    }

    // ====================================================================
    // Word – toString
    // ====================================================================

    /**
     * toString() must follow the exact format <word, frequency>.
     */
    @Test
    public void testToStringFormat() {
        Word w = new Word("pride");
        assertEquals("<pride, 1>", w.toString(),
                "toString() should return <word, frequency>");
    }

    /**
     * toString() after several increments should reflect the updated frequency.
     */
    @Test
    public void testToStringAfterIncrement() {
        Word w = new Word("joy");
        w.increment();
        w.increment();
        assertEquals("<joy, 3>", w.toString(),
                "toString() should show the current frequency");
    }

    // ====================================================================
    // Word – compareTo
    // ====================================================================

    /**
     * Two words with the same frequency should compare as equal (returns 0)
     */
    @Test
    public void testCompareToEqual() {
        Word a = new Word("apple");
        Word b = new Word("banana");
        assertEquals(0, a.compareTo(b),
                "Words with the same frequency should return 0");
    }

    /**
     * A word with lower frequency should compare as less than (negative result).
     */
    @Test
    public void testCompareToLess() {
        Word a = new Word("low");       // freq = 1
        Word b = new Word("high");
        b.increment(); b.increment();   // freq = 3
        assertTrue(a.compareTo(b) < 0,
                "compareTo should be negative when calling word has lower frequency");
    }

    /**
     * A word with higher frequency should compare as greater than (positive result).
     */
    @Test
    public void testCompareToGreater() {
        Word a = new Word("frequent");
        a.increment(); a.increment();   // freq = 3
        Word b = new Word("rare");      // freq = 1
        assertTrue(a.compareTo(b) > 0,
                "compareTo should be positive when calling word has higher frequency");
    }

    /**
     * compareTo must be consistent with subtraction: result == this.freq - other.freq.
     */
    @Test
    public void testCompareToReflectsSubtraction() {
        Word a = new Word("x");         // freq = 1
        Word b = new Word("y");
        b.increment(); b.increment(); b.increment(); // freq = 4
        assertEquals(1 - 4, a.compareTo(b),
                "compareTo should equal this.frequency - other.frequency");
    }

    // ====================================================================
    // WordList - construction & stop word filtering
    // ====================================================================

    /**
     * Stop words should NOT appear in the wordFrequency list.
     */
    @Test
    public void testStopWordsFiltered() {
        String[] tokens = {"the", "quick", "brown", "fox", "and", "the"};
        WordList wl = new WordList(basicStopWords(), tokens);
        assertEquals(-1, wl.search("the"),
                "Stop word 'the' should not be in the list");
        assertEquals(-1, wl.search("and"),
                "Stop word 'and' should not be in the list");
    }

    /**
     * Non-stop words should be present with correct frequencies.
     */
    @Test
    public void testNonStopWordsPresent() {
        String[] tokens = {"quick", "brown", "fox", "quick", "fox", "fox"};
        WordList wl = new WordList(basicStopWords(), tokens);

        int quickIdx = wl.search("quick");
        assertNotEquals(-1, quickIdx, "'quick' should be in the list");
        assertEquals(2, wl.getWordFrequency().get(quickIdx).getFrequency(),
                "'quick' should have frequency 2");

        int foxIdx = wl.search("fox");
        assertNotEquals(-1, foxIdx, "'fox' should be in the list");
        assertEquals(3, wl.getWordFrequency().get(foxIdx).getFrequency(),
                "'fox' should have frequency 3");
    }

    /**
     * When all tokens are stop words, the list should be empty.
     */
    @Test
    public void testAllStopWords() {
        String[] tokens = {"the", "and", "of"};
        WordList wl = new WordList(basicStopWords(), tokens);
        assertTrue(wl.getWordFrequency().isEmpty(),
                "WordList should be empty when all tokens are stop words");
    }

    /**
     * Empty token array should produce an empty WordList.
     */
    @Test
    public void testEmptyTokenArray() {
        String[] tokens = {};
        WordList wl = new WordList(basicStopWords(), tokens);
        assertTrue(wl.getWordFrequency().isEmpty(),
                "WordList should be empty for an empty token array");
    }

    // ====================================================================
    // WordList – search
    // ====================================================================

    /**
     * search() should return -1 for a word that doesn't exist.
     */
    @Test
    public void testSearchNotFound() {
        String[] tokens = {"cat", "dog"};
        WordList wl = new WordList(basicStopWords(), tokens);
        assertEquals(-1, wl.search("bird"),
                "search() should return -1 for a word not in the list");
    }

    /**
     * search() should return a valid index (>= 0) for an existing word.
     */
    @Test
    public void testSearchFound() {
        String[] tokens = {"elizabeth", "darcy"};
        WordList wl = new WordList(basicStopWords(), tokens);
        assertTrue(wl.search("elizabeth") >= 0,
                "search() should return a non-negative index for a present word");
    }

    // ====================================================================
    // WordList – getMostFrequent
    // ====================================================================

    /**
     * getMostFrequent() should return the word with the highest frequency.
     */
    @Test
    public void testGetMostFrequent() {
        String[] tokens = {"pride", "pride", "pride", "prejudice", "prejudice", "bennet"};
        WordList wl = new WordList(basicStopWords(), tokens);
        Word most = wl.getMostFrequent();
        assertNotNull(most, "getMostFrequent() should not return null for a non-empty list");
        assertEquals("pride", most.getWord(),
                "The most frequent word should be 'pride' with frequency 3");
        assertEquals(3, most.getFrequency());
    }

    /**
     * getMostFrequent() should return null when the WordList is empty.
     */
    @Test
    public void testGetMostFrequentEmptyList() {
        String[] tokens = {};
        WordList wl = new WordList(basicStopWords(), tokens);
        assertNull(wl.getMostFrequent(),
                "getMostFrequent() should return null for an empty list");
    }

    /**
     * When all words have the same frequency, getMostFrequent() returns one of them.
     */
    @Test
    public void testGetMostFrequentTie() {
        String[] tokens = {"alpha", "beta", "gamma"};
        WordList wl = new WordList(basicStopWords(), tokens);
        Word most = wl.getMostFrequent();
        assertNotNull(most);
        assertEquals(1, most.getFrequency(),
                "All words have frequency 1 – most frequent should also have frequency 1");
    }

    // ====================================================================
    // WordList – topKMostFrequent
    // ====================================================================

    /**
     * topKMostFrequent(3) should return the 3 highest-frequency words
     * in descending order of frequency.
     */
    @Test
    public void testTopKMostFrequent() {
        // Setup frequencies: cat=5, dog=3, fish=4, bird=1
        String[] tokens = new String[5 + 4 + 3 + 1];
        int idx = 0;
        for (int i = 0; i < 5; i++) tokens[idx++] = "cat";
        for (int i = 0; i < 4; i++) tokens[idx++] = "fish";
        for (int i = 0; i < 3; i++) tokens[idx++] = "dog";
        tokens[idx] = "bird";

        WordList wl = new WordList(basicStopWords(), tokens);
        Word[] top3 = wl.topKMostFrequent(3);

        assertEquals(3, top3.length, "Should return exactly 3 words");
        assertEquals("cat",  top3[0].getWord(), "1st most frequent should be 'cat'");
        assertEquals("fish", top3[1].getWord(), "2nd most frequent should be 'fish'");
        assertEquals("dog",  top3[2].getWord(), "3rd most frequent should be 'dog'");
    }

    /**
     * When k exceeds the number of words, the result should only contain
     * as many words as are available (no null padding, no exception)
     */
    @Test
    public void testTopKExceedsListSize() {
        String[] tokens = {"apple", "orange"};
        WordList wl = new WordList(basicStopWords(), tokens);
        Word[] result = wl.topKMostFrequent(10);
        assertEquals(2, result.length,
                "topKMostFrequent should cap at the actual list size");
    }

    /**
     * topKMostFrequent(1) should return the single most frequent word -
     * same result as getMostFrequent().
     */
    @Test
    public void testTopOneMostFrequent() {
        String[] tokens = {"elizabeth", "elizabeth", "darcy"};
        WordList wl = new WordList(basicStopWords(), tokens);
        Word[] top1 = wl.topKMostFrequent(1);
        assertEquals(1, top1.length);
        assertEquals("elizabeth", top1[0].getWord(),
                "topKMostFrequent(1) should match getMostFrequent()");
        assertEquals(top1[0].getWord(), wl.getMostFrequent().getWord());
    }
}