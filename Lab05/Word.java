/**
 * Represents a word and its frequency count in a text.
 * Implements Comparable<Word> ordered by frequency so collections
 * can be ranked from least to most frequent.
 * 
 * @author Ali Kablanbek
 * @version 3/3/26
 */

public class Word implements Comparable<Word> {

    private String word;
    private int frequency;

    /**
     * Creates a Word with initial frequency 1.
     * @param word the word string (should be lowercase)
     */
    public Word(String word) {
        this.word      = word;
        this.frequency = 1;
    }

    /** @return the word string */
    public String getWord() {
        return word;
    }

    /** @return how many times this word has appeared (always >= 1) */
    public int getFrequency() {
        return frequency;
    }

    /** Records one additional occurrence of this word. */
    public void increment() {
        frequency++;
    }

    /**
     * Returns the word in the format {@code <word, frequency>}.
     * Example: {@code <chapter, 61>}
     */
    @Override
    public String toString() {
        return "<" + word + ", " + frequency + ">";
    }

    /**
     * Compares by frequency: negative if this < w, 0 if equal, positive if this > w.
     * Equivalent to this.frequency - w.frequency.
     */
    @Override
    public int compareTo(Word w) {
        return this.frequency - w.frequency;
    }
}