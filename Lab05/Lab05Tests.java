import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Word and WordList.
 *
 * Structure:
 *   - Word tests: constructor, increment, toString, compareTo.
 *   - WordList Lab 4 tests: construction, filtering, search, getMostFrequent,
 *     topKMostFrequent.
 *   - WordList Lab 5 tests: distinctWordsCount, shortWordsCount (including
 *     assertThrows for negative threshold), mostFrequentByLetter.
 *
 *  + there are some explanations to some tests(needed for myself)
 * @author Ali Kablanbek
 * @version 3/3/26
 * 
 */
public class Lab05Tests {

    // -- Helpers --

    /** Minimal stop-words list for tests that just need a few filtered words. */
    private ArrayList<String> basicStopWords() {
        ArrayList<String> sw = new ArrayList<>();
        sw.add("the"); sw.add("and"); sw.add("of");
        return sw;
    }

    /** Builds a flat token array from parallel word/count arrays. */
    private String[] buildTokenArray(String[] words, int[] counts) {
        int total = 0;
        for (int c : counts) total += c;
        String[] tokens = new String[total];
        int idx = 0;
        for (int i = 0; i < words.length; i++)
            for (int j = 0; j < counts[i]; j++) tokens[idx++] = words[i];
        return tokens;
    }

    // ===================================================================
    // Word tests
    // ===================================================================

    /** WHY: Confirms the constructor stores the word and starts frequency at 1. */
    @Test
    public void testWordConstructorAndGetters() {
        Word w = new Word("hello");
        assertEquals("hello", w.getWord());
        assertEquals(1, w.getFrequency());
    }

    /** WHY: One increment is the most common case; counter must go from 1 to 2. */
    @Test
    public void testIncrementOnce() {
        Word w = new Word("test");
        w.increment();
        assertEquals(2, w.getFrequency());
    }

    /** WHY: Multiple increments must accumulate, not reset or cap. */
    @Test
    public void testIncrementMultipleTimes() {
        Word w = new Word("repeat");
        for (int i = 0; i < 9; i++) w.increment();
        assertEquals(10, w.getFrequency());
    }

    /** WHY: The lab specifies the exact format <word, frequency>. */
    @Test
    public void testToStringFormat() {
        assertEquals("<pride, 1>", new Word("pride").toString());
    }

    /** WHY: toString() must reflect the current (live) frequency, not a snapshot. */
    @Test
    public void testToStringAfterIncrement() {
        Word w = new Word("joy");
        w.increment(); w.increment();
        assertEquals("<joy, 3>", w.toString());
    }

    /** WHY: The lab demos self-comparison returning 0 (n - n == 0). */
    @Test
    public void testCompareToSelf() {
        Word w = new Word("self");
        assertEquals(0, w.compareTo(w));
    }

    /** WHY: Two objects with equal frequency must return 0. */
    @Test
    public void testCompareToEqualFrequency() {
        assertEquals(0, new Word("apple").compareTo(new Word("banana")));
    }

    /** WHY: Lower-frequency caller must return a negative value. */
    @Test
    public void testCompareToLess() {
        Word low = new Word("low"), high = new Word("high");
        high.increment(); high.increment();
        assertTrue(low.compareTo(high) < 0);
    }

    /** WHY: Higher-frequency caller must return a positive value. */
    @Test
    public void testCompareToGreater() {
        Word freq = new Word("frequent");
        freq.increment(); freq.increment();
        assertTrue(freq.compareTo(new Word("rare")) > 0);
    }

    /** WHY: The lab spec says compareTo equals this.frequency - w.frequency exactly. */
    @Test
    public void testCompareToExactDifference() {
        Word a = new Word("x"), b = new Word("y");
        b.increment(); b.increment(); b.increment(); // freq 4
        assertEquals(1 - 4, a.compareTo(b));
        assertEquals(4 - 1, b.compareTo(a));
    }

    // ===================================================================
    // WordList tests – Lab 4 methods
    // ===================================================================

    /** WHY: Stop words must be silently excluded from wordFrequency. */
    @Test
    public void testStopWordsFiltered() {
        WordList wl = new WordList(basicStopWords(),
                new String[]{"the", "quick", "brown", "and"});
        assertEquals(-1, wl.search("the"));
        assertEquals(-1, wl.search("and"));
    }

    /**
     * WHY: stopwords.txt has mixed-case entries like "But", so isStopWord()
     * must use equalsIgnoreCase() to catch them after toLowerCase().
     */
    @Test
    public void testStopWordsCaseInsensitive() {
        ArrayList<String> sw = new ArrayList<>();
        sw.add("But");
        WordList wl = new WordList(sw, new String[]{"but", "clever"});
        assertEquals(-1, wl.search("but"));
        assertNotEquals(-1, wl.search("clever"));
    }

    /** WHY: Each unique non-stop token must accumulate the correct count. */
    @Test
    public void testFrequencyCounting() {
        WordList wl = new WordList(basicStopWords(),
                new String[]{"quick","brown","fox","quick","fox","fox"});
        assertEquals(2, wl.getWordFrequency().get(wl.search("quick")).getFrequency());
        assertEquals(3, wl.getWordFrequency().get(wl.search("fox")).getFrequency());
    }

    /** WHY: All stop-words input must produce an empty (not null) list. */
    @Test
    public void testAllStopWords() {
        assertTrue(new WordList(basicStopWords(), new String[]{"the","and","of"})
                .getWordFrequency().isEmpty());
    }

    /** WHY: Empty token array must produce an empty list without throwing. */
    @Test
    public void testEmptyTokenArray() {
        assertTrue(new WordList(basicStopWords(), new String[]{})
                .getWordFrequency().isEmpty());
    }

    /** WHY: getWordFrequency() must return a non-null list of the correct size. */
    @Test
    public void testGetWordFrequency() {
        WordList wl = new WordList(basicStopWords(),
                new String[]{"elizabeth","darcy","elizabeth"});
        assertNotNull(wl.getWordFrequency());
        assertEquals(2, wl.getWordFrequency().size());
    }

    /** WHY: search() must return -1 for an absent word. */
    @Test
    public void testSearchNotFound() {
        WordList wl = new WordList(basicStopWords(), new String[]{"cat","dog"});
        assertEquals(-1, wl.search("bird"));
    }

    /** WHY: The returned index must point to the correct Word, not just be >= 0. */
    @Test
    public void testSearchReturnsCorrectIndex() {
        WordList wl = new WordList(basicStopWords(), new String[]{"elizabeth","darcy"});
        int idx = wl.search("elizabeth");
        assertTrue(idx >= 0);
        assertEquals("elizabeth", wl.getWordFrequency().get(idx).getWord());
    }

    /** WHY: getMostFrequent() must identify the word with the highest count. */
    @Test
    public void testGetMostFrequent() {
        WordList wl = new WordList(basicStopWords(),
                new String[]{"pride","pride","pride","prejudice","prejudice","bennet"});
        Word most = wl.getMostFrequent();
        assertNotNull(most);
        assertEquals("pride", most.getWord());
        assertEquals(3, most.getFrequency());
    }

    /** WHY: getMostFrequent() must return null for an empty list, not throw. */
    @Test
    public void testGetMostFrequentEmptyList() {
        assertNull(new WordList(basicStopWords(), new String[]{}).getMostFrequent());
    }

    /** WHY: In a tie, getMostFrequent() must still return a valid Word. */
    @Test
    public void testGetMostFrequentAllTied() {
        Word most = new WordList(basicStopWords(),
                new String[]{"alpha","beta","gamma"}).getMostFrequent();
        assertNotNull(most);
        assertEquals(1, most.getFrequency());
    }

    /** WHY: Verifies correct descending order and array length. */
    @Test
    public void testTopKMostFrequent() {
        WordList wl = new WordList(basicStopWords(),
                buildTokenArray(new String[]{"cat","fish","dog","bird"}, new int[]{5,4,3,1}));
        Word[] top3 = wl.topKMostFrequent(3);
        assertEquals(3, top3.length);
        assertEquals("cat",  top3[0].getWord());
        assertEquals("fish", top3[1].getWord());
        assertEquals("dog",  top3[2].getWord());
    }

    /** WHY: k > list size must cap at list size with no null entries. */
    @Test
    public void testTopKExceedsListSize() {
        Word[] result = new WordList(basicStopWords(), new String[]{"apple","orange"})
                .topKMostFrequent(10);
        assertEquals(2, result.length);
        assertNotNull(result[0]); assertNotNull(result[1]);
    }

    /** WHY: topKMostFrequent(1) and getMostFrequent() must agree. */
    @Test
    public void testTopOneMostFrequent() {
        WordList wl = new WordList(basicStopWords(),
                new String[]{"elizabeth","elizabeth","darcy"});
        Word[] top1 = wl.topKMostFrequent(1);
        assertEquals(1, top1.length);
        assertEquals(top1[0].getWord(), wl.getMostFrequent().getWord());
    }

    // ===================================================================
    // !!!!!!!!!! WordList stream tests – Lab 5 methods !!!!!!!!!!!!!!!!!!!!
    // ===================================================================

    // -- distinctWordsCount --

    /** WHY: Repeated tokens must be collapsed; 5 raw tokens → 3 distinct strings. */
    @Test
    public void testDistinctWordsCountNormal() {
        assertEquals(3L, WordList.distinctWordsCount(
                new String[]{"cat","dog","cat","bird","cat"}));
    }

    /** WHYYYY: All identical tokens must return 1, not the raw count. */
    @Test
    public void testDistinctWordsCountAllSame() {
        assertEquals(1L, WordList.distinctWordsCount(new String[]{"rose","rose","rose"}));
    }

    /**
     * WHY?: split() can produce empty strings at array boundaries; they must
     * not be counted as distinct words.
     */
    @Test
    public void testDistinctWordsCountIgnoresEmptyStrings() {
        assertEquals(2L, WordList.distinctWordsCount(
                new String[]{"","hello","","world"}));
    }

    /** WHY: Empty array must return 0 without throwing. */
    @Test
    public void testDistinctWordsCountEmptyArray() {
        assertEquals(0L, WordList.distinctWordsCount(new String[]{}));
    }

    // -- shortWordsCount --

    /**
     * WHY: With threshold 3, "cat"(3) and "ox"(2) qualify; "lion"(4) does not.
     * Verifies the filter is <= (inclusive), not <.
     */
    @Test
    public void testShortWordsCountNormal() {
        assertEquals(2L, WordList.shortWordsCount(
                new String[]{"cat","ox","lion","tiger","cat"}, 3));
    }

    /** WHY: Threshold 0 means no word can qualify (every string has length >= 1). */
    @Test
    public void testShortWordsCountThresholdZero() {
        assertEquals(0L, WordList.shortWordsCount(new String[]{"hi","me"}, 0));
    }

    /** WHY: Threshold >= longest word means every word qualifies. */
    @Test
    public void testShortWordsCountThresholdCoversAll() {
        assertEquals(3L, WordList.shortWordsCount(new String[]{"hi","bye","hello"}, 5));
    }

    /**
     * WHY: A negative threshold has no meaningful definition and must throw
     * IllegalArgumentException.  The lambda () -> ... is the code under test;
     * assertThrows catches the exception and fails the test if none is thrown.
     */
    @Test
    public void testShortWordsCountNegativeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> WordList.shortWordsCount(new String[]{"hello"}, -1));
    }

    /**
     * WHY: assertThrows returns the caught exception so we can inspect its
     * message and confirm the right code path was hit.
     */
    @Test
    public void testShortWordsCountNegativeThrowsMessage() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> WordList.shortWordsCount(new String[]{"hello"}, -5));
        assertTrue(ex.getMessage().contains("-5"));
    }

    // -- mostFrequentByLetter --

    /**
     * WhY: "python"(4) beats "prime"(2) among 'p'-words; "castle"(3) is
     * excluded by the filter even though it has higher frequency than "prime".
     */
    @Test
    public void testMostFrequentByLetterNormal() {
        WordList wl = new WordList(basicStopWords(),
                buildTokenArray(new String[]{"python","prime","castle"}, new int[]{4,2,3}));
        Word result = WordList.mostFrequentByLetter(wl, 'p');
        assertNotNull(result);
        assertEquals("python", result.getWord());
        assertEquals(4, result.getFrequency());
    }

    /**
     * WHY: No matching letter means the filtered stream is empty;
     * orElse(null) must return null rather than throw.
     */
    @Test
    public void testMostFrequentByLetterNoMatch() {
        WordList wl = new WordList(basicStopWords(), new String[]{"river","rock","rain"});
        assertNull(WordList.mostFrequentByLetter(wl, 'z'));
    }

    /** WHY: max() on a single-element stream must return that element. */
    @Test
    public void testMostFrequentByLetterSingleMatch() {
        WordList wl = new WordList(basicStopWords(),
                buildTokenArray(new String[]{"penguin","zebra","lion"}, new int[]{2,5,3}));
        Word result = WordList.mostFrequentByLetter(wl, 'p');
        assertNotNull(result);
        assertEquals("penguin", result.getWord());
    }

    /**
     * WHY: Tokens are lowercased in main, so uppercase 'P' should find nothing
     * in a properly built WordList.  Confirms the method is case-sensitive.
     */
    @Test
    public void testMostFrequentByLetterCaseSensitive() {
        WordList wl = new WordList(basicStopWords(), new String[]{"pride","prejudice"});
        assertNull(WordList.mostFrequentByLetter(wl, 'P'));
    }
}