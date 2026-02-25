/**
 * Represents a word and its frequency count in a text.
 * Implements Comparable<Word> so words can be compared by frequency.
 * 
 * @author Ali Kablanbek
 * @version 2/24/26
 */
public class Word implements Comparable<Word> {

    private String word;
    private int frequency;

    /**
     * Constructs a Word with the given string and an initial frequency of 1.
     * @param word the word string
     */
    public Word(String word) {
        this.word = word;
        this.frequency = 1;
    }

    /**
     * Returns the word string.
     * 
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns the current frequency of this word.
     * 
     * @return frequency count
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Increments the frequency count by 1.
     */
    public void increment() {
        frequency++;
    }

    /**
     * Returns a string representation in the format <word, frequency>.
     * 
     * @return formatted string
     */
    @Override
    public String toString() {
        return "<" + word + ", " + frequency + ">";
    }

    /**
     * Compares this Word to another Word by frequency.
     * Returns a negative integer if this word has lower frequency,
     * zero if equal, positive if higher frequency.
     * 
     * @param w the Word to compare to
     * @return difference in frequencies (this.frequency - w.frequency)
     */
    @Override
    public int compareTo(Word w) {
        return this.frequency - w.frequency;
    }
}