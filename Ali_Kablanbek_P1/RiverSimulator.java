import java.util.Scanner;

/**
 * Main class that drives the river ecosystem simulation.
 * Repeatedly prompts the user to create and simulate river ecosystems.
 *
 * @author Ali Kablanbek
 * @version 2/23/2026
 */
public class RiverSimulator {

    /**
     * Main method: runs the river ecosystem simulator in a loop.
     * The user can choose to simulate a random river or exit.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to CS206 River Ecosystem Simulator!");
        System.out.println();

        while (true) {
            System.out.println("River Ecosystem Simulator");
            System.out.println("Please choose: 1 (random river) 2 (exit)");
            int choice = readAnyInt(scanner);

            if (choice == 1) {
                runSimulation(scanner);
            } else if (choice == 2) {
                System.out.println("Goodbye !");
                break;
            } else {
                System.out.println("Invalid selection. Please try again.");
                System.out.println();
            }
        }

        scanner.close();
    }

    /**
     * Runs one complete simulation: prompts for length and cycles,
     * creates the river, then prints each cycle.
     *
     * @param scanner the Scanner to read input from
     */
    private static void runSimulation(Scanner scanner) {
        System.out.println("Creating a random river...");

        System.out.println("Enter the river length (an integer bigger than 0):");
        int length = readPositiveInt(scanner);

        System.out.println();
        System.out.println("Enter the number of cycles (an integer bigger than 0):");
        int cycles = readPositiveInt(scanner);

        River river = new River(length);
        System.out.println("Initial river:");
        System.out.println(river.toString());

        for (int c = 1; c <= cycles; c++) {
            river.updateRiver();
            System.out.println("After cycle " + c);
            System.out.println(river.toString());
        }

        System.out.println();
    }

    /**
     * Reads any integer from the scanner (used for the main menu).
     *
     * @param scanner the Scanner to read from
     * @return the integer entered by the user
     */
    private static int readAnyInt(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                scanner.next(); // discard non-integer token
            }
        }
    }

    /**
     * Reads integers from the scanner until a positive integer (> 0) is entered.
     * Used for river length and cycle count inputs.
     *
     * @param scanner the Scanner to read from
     * @return a positive integer entered by the user
     */
    private static int readPositiveInt(Scanner scanner) {
        int value = 0;
        while (value <= 0) {
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
            } else {
                scanner.next(); // discard non-integer token
            }
        }
        return value;
    }
}