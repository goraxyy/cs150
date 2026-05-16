
/**
 * Reads user input lines, counts lines/words/characters
 * and writes results to Terminal Window and Lab1.txt
 *
 * @author Ali Kablanbek
 * @version 1.31.26
 */
import java.util.Scanner;
import java.io.*;

public class Main
{

    /**
     * Program entry point; processes input until a blank line is entered
     *
     * @param args Command-line arguments (not used)
     * @throws Exception If an I/O error occurs
     */
    public static void main(String[] args) throws Exception
    {
        // Initializing the variables
        int lineCount = 0;
        int wordCount = 0;
        int charCount = 0;
        
        // Clearing the Terminal Window
        System.out.print('\u000C');
        
        // Overwriting the file
        BufferedWriter b = new BufferedWriter(new FileWriter("Lab01.txt"));

        // Scanner for reading user input form the console
        Scanner s = new Scanner(System.in);
        
        // POint of entering the loop
        System.err.println("entering loop");
        
        while(true){
            // Store the given line
            String givenLine = s.nextLine();
            
            // Check if the line is empty
            if(givenLine.isBlank()){
                break;
            }
            
            // Keeping track of amount of lines
            lineCount++;
            
            // Program processes the given line
            System.err.println("continuing to loop >>" + givenLine + "<<");
            
            // Writing down the line to the file
            b.write(givenLine + "\n");
            
            // Scanner to split the line into tokens
            Scanner p = new Scanner(givenLine);
 
            // Adding the length of the line to the charCount
            charCount += givenLine.length();
            
            // Looping through tokens
            while(p.hasNext()){
                // String storing tokens seperated by space
                String w = p.next();
                wordCount++;
                
                // Printing out the token
                System.out.println(w);
            
                // Writing down the token to the file
                b.write(w + "\n");
            
            }
        }
        
        // Point of exiting the loop
        System.err.println("exiting the loop");
        
        System.out.println("==============================================");
        System.out.println("Line Count: " + lineCount);
        System.out.println("Word Count: " + wordCount);
        System.out.println("Char Count: " + charCount);
        
        b.write("\n\n=============================================\n");
        b.write("Line Count: " + lineCount + "\n");
        b.write("Word Count: " + wordCount + "\n");
        b.write("Char Count: " + charCount + "\n");
        
        s.close();
        b.close();
    }
}