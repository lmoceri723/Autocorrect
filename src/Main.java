import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create a scanner to read user input
        Scanner scanner = new Scanner(System.in);

        // Load the dictionary
        Autocorrect autocorrect = new Autocorrect();

        // Print the header
        System.out.println("Welcome to Autocorrect!");
        System.out.println("Enter a word to get suggestions or CTRL + C to quit.");

        // Continuously prompt the user for input
        while (true) {
            // Get the user input
            System.out.print("Enter a word: ");
            String typed = scanner.nextLine();

            // Calculate threshold
            int threshold = typed.length() / 3 + 1;
            autocorrect.setThreshold(threshold);

            // Get suggestions from the autocorrect
            String[] suggestions = autocorrect.runTest(typed);

            // print if the word is already a word
            if (suggestions.length == 1 && suggestions[0].equals(typed)) {
                System.out.println("The word \"" + typed + "\" is already a word.");
                continue;
            }
            if (suggestions.length == 0) {
                System.out.println("No suggestions found.");
                continue;
            }
            // Print suggestions
            System.out.println("Did you mean:");
            for (String suggestion : suggestions) {
                System.out.println(" - " + suggestion);
            }
        }
    }
}