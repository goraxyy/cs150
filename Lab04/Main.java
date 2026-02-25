import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class that orchestrates reading Pride and Prejudice, building a WordList,
 * and outputting results to console and file.
 * 
 * @author Ali Kablanbek
 * @version 2/24/26
 */
public class Main {

    public static void main(String[] args) {

        // ----------------------------------------------------------------
        // 1. Read stop words from stopwords.txt
        // ----------------------------------------------------------------
        ArrayList<String> stopWords = new ArrayList<>();
        try {
            Scanner swScanner = new Scanner(new File("stopwords.txt"));
            while (swScanner.hasNextLine()) {
                String line = swScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    stopWords.add(line);
                }
            }
            swScanner.close();
        } catch (IOException e) {
            System.err.println("Error reading stopwords.txt: " + e.getMessage());
            return;
        }

        // ----------------------------------------------------------------
        // 2. Read Pride_and_Prejudice.txt, build a single lowercase string,
        //    then split into tokens.
        // ----------------------------------------------------------------
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner textScanner = new Scanner(new File("Pride_and_Prejudice.txt"));
            while (textScanner.hasNextLine()) {
                lines.add(textScanner.nextLine());
            }
            textScanner.close();
        } catch (IOException e) {
            System.err.println("Error reading Pride_and_Prejudice.txt: " + e.getMessage());
            return;
        }

        String fullText = String.join(" ", lines).toLowerCase();
        String delimiter = "[\\W]+";
        String[] tokens = fullText.split(delimiter);

        // ----------------------------------------------------------------
        // 3. Build WordList
        // ----------------------------------------------------------------
        WordList wordList = new WordList(stopWords, tokens);

        // ----------------------------------------------------------------
        // 4. Write all words to Lab04.txt
        // ----------------------------------------------------------------
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("Lab04.txt"));
            for (Word w : wordList.getWordFrequency()) {
                pw.println(w.toString());
            }
            pw.close();
            System.out.println("Output written to Lab04.txt");
        } catch (IOException e) {
            System.err.println("Error writing Lab04.txt: " + e.getMessage());
        }

        // ----------------------------------------------------------------
        // 5. Console output
        // ----------------------------------------------------------------
        System.out.println("Total number of unique words in WordList: "
                + wordList.getWordFrequency().size());

        System.out.println("\nTop 5 Most Frequent Words:");
        Word[] top5 = wordList.topKMostFrequent(5);
        for (int i = 0; i < top5.length; i++) {
            System.out.println("  " + (i + 1) + ". " + top5[i]);
        }
    }
}