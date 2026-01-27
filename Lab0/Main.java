
/**
 * This class prints all the arguments passed to it
 *
 * @author Ali Kablanbek
 * @version 1.26.26
 */
public class Main
{
    /**
     * Main entry point that outputs each command-line argument on a new line
     * 
     * @param args arguments to print
     */
    public static void main(String[] args) { 
        //Iterate through each argument and print individually
        for(int i=0; i<args.length; i++){
            System.out.println(args[i]);
        }
    }
}