package michail;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ParsingTable table = new ParsingTable();
        Parser parser = new Parser(table);

        Scanner scanner = new Scanner(System.in);

        int choice = -1;
        boolean isValid = false;

        do {
            isValid = false;
            displayMenu();

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume the invalid input
                continue; // Restart the loop
            }

            String inputString = choiceString(choice, scanner, parser);

            if(inputString == null){ // Exit the program
                continue;
            }
            else if(inputString.isEmpty())
            {
                System.out.println("A string is empty! Try again.");
                continue;
            }
            else {
                isValid = parser.parse(inputString);
            }
            System.out.println("\nSTRING: " + inputString);
            System.out.println("RESULT: THE STRING IS " + (isValid ? "VALID" : "INVALID") + ".\n");
            System.out.println("Press enter to continue...");
            System.in.read(); // Wait for user input
        } while (choice != 3);
    }

    private static void displayMenu() {
        System.out.println("\n-----------------\nMenu:");
        System.out.println("1. Enter custom input");
        System.out.println("2. Generate random string.");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static String choiceString(int choice, Scanner scanner, Parser parser) {
        if (choice == 1) {
            System.out.print("Enter your custom input: ");
            scanner.nextLine(); // Consume the newline character
            String customInput = scanner.nextLine();
            System.out.println("You entered: " + customInput);
            return customInput;
        } else if (choice == 2) {
            String random = parser.generate();
            System.out.println("Generated STRING: " + random);
            return random;
        }
        else if (choice == 3) {
            System.out.println("Exiting...");
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
        return null;
    }
}
