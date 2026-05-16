import java.util.ArrayList;
import java.util.Arrays;

/**
 * Holds a list of unique Words built from a raw token array, with stop words excluded.
 * Lab 5 adds three static stream-based methods: distinctWordsCount(),
 * shortWordsCount(), and mostFrequentByLetter().
 * 
 * @author Ali Kablanbek
 * @version 3/3/26
 */
public class WordList {

    /** Unique, non-stop-word entries with their frequencies. */
    private ArrayList<Word> wordFrequency;

    /**
     * Builds the WordList from a token array.
     * Each token is skipped if it is empty or a stop word; otherwise it is
     * added as a new Word or its existing entry is incremented.
     *
     * @param stopWords function words to exclude (may be mixed-case)
     * @param tokens    lowercase word tokens from the source text
     */
    public WordList(ArrayList<String> stopWords, String[] tokens) {
        wordFrequency = new ArrayList<>();

        for (String token : tokens) {
            if (token == null || token.isEmpty()) continue;
            if (isStopWord(token, stopWords))    continue;

            int index = search(token);
            if (index == -1) {
                wordFrequency.add(new Word(token));
            } else {
                wordFrequency.get(index).increment();
            }
        }
    }

    /** @return the full list of Words in insertion order */
    public ArrayList<Word> getWordFrequency() {
        return wordFrequency;
    }

    /**
     * Linear search for a word string.
     * @return index of the matching Word, or -1 if not found
     */
    public int search(String w) {
        for (int i = 0; i < wordFrequency.size(); i++) {
            if (wordFrequency.get(i).getWord().equals(w)) return i;
        }
        return -1;
    }

    /**
     * Returns the most frequent Word using a single linear scan with compareTo().
     * @return the highest-frequency Word, or null if the list is empty
     */
    public Word getMostFrequent() {
        if (wordFrequency.isEmpty()) return null;

        Word most = wordFrequency.get(0);
        for (int i = 1; i < wordFrequency.size(); i++) {
            Word current = wordFrequency.get(i);
            if (current.compareTo(most) > 0) most = current;
        }
        return most;
    }

    /**
     * Returns the top k most frequent Words in descending order without sorting.
     * Uses repeated selection with a boolean used-flag array (O(k*n)).
     * If k > list size, the array is capped at list size with no null padding.
     *
     * @param k number of top words to return
     */
    public Word[] topKMostFrequent(int k) {
        int size       = Math.min(k, wordFrequency.size());
        Word[] result  = new Word[size];
        boolean[] used = new boolean[wordFrequency.size()];

        for (int i = 0; i < size; i++) {
            Word best     = null;
            int bestIndex = -1;

            for (int j = 0; j < wordFrequency.size(); j++) {
                if (!used[j] && (best == null || wordFrequency.get(j).compareTo(best) > 0)) {
                    best      = wordFrequency.get(j);
                    bestIndex = j;
                }
            }
            result[i]       = best;
            used[bestIndex] = true;
        }
        return result;
    }

    // ---------------------------------------------------------------
    // !!! Stream-based methods (Lab 5) !!!!
    // ---------------------------------------------------------------

    /**
     * Counts the number of distinct, non-empty strings in the token array.
     * Stream pipeline: filter empty → distinct → count.
     *
     * @param tokens raw token array from String.split()
     * @return number of distinct non-empty tokens
     */
    public static long distinctWordsCount(String[] tokens) {
        return Arrays.stream(tokens)
                     .filter(s -> s != null && !s.isEmpty())
                     .distinct()
                     .count();
    }

    /**
     * Counts distinct strings of length at most threshold.
     * Stream pipeline: filter empty → filter by length → distinct → count.
     *
     * @param tokens    raw token array from String.split()
     * @param threshold maximum word length to include (must be >= 0)
     * @throws           IllegalArgumentException if threshold is negative
     */
    public static long shortWordsCount(String[] tokens, int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException(
                    "threshold must be >= 0, but was: " + threshold);
        }
        return Arrays.stream(tokens)
                     .filter(s -> s != null && !s.isEmpty())
                     .filter(s -> s.length() <= threshold)
                     .distinct()
                     .count();
    }

    /**
     * Returns the most frequent Word starting with letter using filter() and max().
     * orElse(null) handles the case where no matching word exists.
     *
     * @param wl     the WordList to search
     * @param letter starting character to filter on (case-sensitive)
     * @return the most frequent matching Word, or null if none found
     */
    public static Word mostFrequentByLetter(WordList wl, char letter) {
        return wl.getWordFrequency().stream()
                 .filter(w -> w.getWord().charAt(0) == letter)
                 .max((w1, w2) -> w1.compareTo(w2))
                 .orElse(null);
    }

    /**
     * Checks whether word is in stopWords using case-insensitive comparison.
     * equalsIgnoreCase() is needed because stopwords.txt has mixed-case entries
     * (e.g. "But", "So") while tokens are already lowercased.
     */
    private boolean isStopWord(String word, ArrayList<String> stopWords) {
        for (String sw : stopWords) {
            if (sw.equalsIgnoreCase(word)) return true;
        }
        return false;
    }
}