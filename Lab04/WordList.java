
/**
 * Holds a list of Word objects (each unique word with its frequency).
 * Filters out stop words during construction.
 *
 * @author Ali Kablanbek
 * @version 2/24/26
 */
import java.util.ArrayList;

public class WordList {

    private ArrayList<Word> wordFrequency;

    /**
     * Constructs a WordList from an array of tokens, filtering out stop words.
     * Each unique, non-stop-word token is stored as a Word with its frequency.
     *
     * @param stopWords list of stop words to exclude
     * @param tokens     array of word tokens from the text
     */
    public WordList(ArrayList<String> stopWords, String[] tokens) {
        wordFrequency = new ArrayList<>();

        for (String token : tokens) {
            // Skip empty tokens and stop words
            if (token == null || token.isEmpty()) {
                continue;
            }
            if (isStopWord(token, stopWords)) {
                continue;
            }

            int index = search(token);
            if (index == -1) {
                // New word - add it
                wordFrequency.add(new Word(token));
            } else {
                // Already exists - increment frequency
                wordFrequency.get(index).increment();
            }
        }
    }

    /**
     * Returns the list of Words with frequencies.
     * @return wordFrequency ArrayList
     */
    public ArrayList<Word> getWordFrequency() {
        return wordFrequency;
    }

    /**
     * Searches for a word string in the wordFrequency list.
     * @param w the word string to search for
     * @return the index of the word if found, or -1 if not found
     */
    public int search(String w) {
        for (int i = 0; i < wordFrequency.size(); i++) {
            if (wordFrequency.get(i).getWord().equals(w)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds and returns the most frequent word in the list.
     * If the list is empty, returns null.
     * @return the Word with the highest frequency
     */
    public Word getMostFrequent() {
        if (wordFrequency.isEmpty()) {
            return null;
        }
        Word most = wordFrequency.get(0);
        for (int i = 1; i < wordFrequency.size(); i++) {
            Word current = wordFrequency.get(i);
            // compareTo returns positive if current > most
            if (current.compareTo(most) > 0) {
                most = current;
            }
        }
        return most;
    }

    /**
     * Returns the top k most frequent words without sorting the full list.
     * Uses a selection-style approach: repeatedly finds the next highest
     * frequency word that hasn't been selected yet.
     *
     * @param k the number of top words to retrieve
     * @return an array of the k most frequent Words, in descending order
     */
    public Word[] topKMostFrequent(int k) {
        int size = Math.min(k, wordFrequency.size());
        Word[] result = new Word[size];
        boolean[] used = new boolean[wordFrequency.size()];

        for (int i = 0; i < size; i++) {
            Word best = null;
            int bestIndex = -1;

            for (int j = 0; j < wordFrequency.size(); j++) {
                if (!used[j]) {
                    if (best == null || wordFrequency.get(j).compareTo(best) > 0) {
                        best = wordFrequency.get(j);
                        bestIndex = j;
                    }
                }
            }
            result[i] = best;
            used[bestIndex] = true;
        }
        return result;
    }

    /**
     * Checks whether a given word is in the stop words list.
     * @param word      the word to check
     * @param stopWords the list of stop words
     * @return true if the word is a stop word, false otherwise
     */
    private boolean isStopWord(String word, ArrayList<String> stopWords) {
        for (String sw : stopWords) {
            if (sw.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
}