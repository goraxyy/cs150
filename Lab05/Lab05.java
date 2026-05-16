import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for Lab 5. Reads stopwords.txt and Pride_and_Prejudice.txt,
 * builds a WordList, writes results to Lab05.txt, and prints a console summary.
 *
 * Console output: 
 *   - Total unique content words and top 5 most frequent (Lab 4)
 *   - Words excluded by stop-word filter
 *   - Distinct words of length <= 3
 *   - Most frequent word starting with 'p'
 *   - Longest word starting with 'p'
 *   
 * @author Ali Kablanbek
 * @version 3/3/26
 *  
 */
public class Lab05 {

    public static void main(String[] args) {

        // --- Step 1: Read stop words ---
        ArrayList<String> stopWords = new ArrayList<>();
        try {
            Scanner swScanner = new Scanner(new File("stopwords.txt"));
            while (swScanner.hasNextLine()) {
                String line = swScanner.nextLine().trim();
                if (!line.isEmpty()) stopWords.add(line);
            }
            swScanner.close();
        } catch (IOException e) {
            System.err.println("Error reading stopwords.txt: " + e.getMessage());
            return;
        }

        // --- Step 2: Read the novel ---
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner textScanner = new Scanner(new File("Pride_and_Prejudice.txt"));
            while (textScanner.hasNextLine()) lines.add(textScanner.nextLine());
            textScanner.close();
        } catch (IOException e) {
            System.err.println("Error reading Pride_and_Prejudice.txt: " + e.getMessage());
            return;
        }

        // --- Step 3: Join, lowercase, split on non-word characters ---
        String   fullText = String.join(" ", lines).toLowerCase();
        String[] tokens   = fullText.split("[\\W]+");

        // --- Step 4: Build WordList ---
        WordList wordList = new WordList(stopWords, tokens);

        // --- Step 5: Write all words to Lab05.txt ---
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("Lab05.txt"));
            for (Word w : wordList.getWordFrequency()) pw.println(w.toString());
            pw.close();
            System.out.println("Word list written to Lab05.txt");
        } catch (IOException e) {
            System.err.println("Error writing Lab05.txt: " + e.getMessage());
        }

        // --- Step 6: Lab 4 summary ---
        System.out.println("\nTotal unique content words: " + wordList.getWordFrequency().size());
        System.out.println("\nTop 5 most frequent words:");
        Word[] top5 = wordList.topKMostFrequent(5);
        for (int i = 0; i < top5.length; i++) System.out.println("  " + (i + 1) + ". " + top5[i]);

        // --- Step 7: Words excluded by the stop-word filter ---
        // distinctWordsCount covers the full raw vocabulary; subtracting the
        // WordList size gives how many distinct words were filtered out.
        long totalDistinct = WordList.distinctWordsCount(tokens);
        long excluded      = totalDistinct - wordList.getWordFrequency().size();
        System.out.println("\nDistinct tokens in raw text:          " + totalDistinct);
        System.out.println("Words excluded by stop-word filter:    " + excluded);

        // --- Step 8: Distinct words of length <= 3 ---
        System.out.println("\nDistinct words of length <= 3: " + WordList.shortWordsCount(tokens, 3));

        // --- Step 9: Most frequent word starting with 'p' ---
        Word mostFreqP = WordList.mostFrequentByLetter(wordList, 'p');
        System.out.println("\nMost frequent word starting with 'p': " +
                (mostFreqP != null ? mostFreqP : "none found"));

        // --- Step 10: Longest word starting with 'p' (inline stream) ---
        // filter to 'p'-words, then max() by word-string length.
        Word longestP = wordList.getWordFrequency().stream()
                .filter(w -> w.getWord().charAt(0) == 'p')
                .max((w1, w2) -> w1.getWord().length() - w2.getWord().length())
                .orElse(null);

        if (longestP != null) {
            System.out.println("Longest word starting with 'p':       "
                    + longestP.getWord() + " (" + longestP.getWord().length() + " letters)");
        } else {
            System.out.println("Longest word starting with 'p':       none found");
        }
    }
}