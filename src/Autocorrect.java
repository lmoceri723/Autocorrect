import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author Landon Moceri
 */

public class Autocorrect {
    public String[] dictionary;
    public int threshold;

    public TST dictTree;

    ArrayList<ArrayList<String>> matches;

    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    public Autocorrect(String[] words, int threshold) {
        this.dictionary = words;
        this.threshold = threshold;

        matches = new ArrayList<ArrayList<String>>();
        for (int i = 0; i <= threshold; i++) {
            matches.add(new ArrayList<String>());
        }

        dictTree = new TST();
        for (String word : words) {
            dictTree.addWord(word);
        }
    }

    // Uses tabulation to find the edit Levenshtein distance between two words
    private int editDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        int [][] results = new int[n + 1][m + 1];

        // Iterate over each spot in the table
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                // If either of the words is empty, the distance is the length of the other word
                if (i == 0) {
                    results[i][j] = j;
                } else if (j == 0) {
                    results[i][j] = i;
                // If the letters are the same, the distance is the same as the previous letters
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    results[i][j] = results[i - 1][j - 1];
                } else {
                    // Otherwise, return 1 plus the minimum distance of the three possible operations
                    int add = results[i][j - 1];
                    int remove = results[i - 1][j];
                    int replace = results[i - 1][j - 1];

                    results[i][j] = Math.min(add, Math.min(remove, replace)) + 1;
                }
            }
        }

        return results[n][m];
    }

    public void dictDFS(TST.TSTNode node, String mistyped, String currentWord) {
        if (node == null) {
            return;
        }

        // Traverse the left subtree
        dictDFS(node.left, mistyped, currentWord);

        // Check the current node
        String newWord = currentWord + node.character;
        int distance = editDistance(mistyped, newWord);
        if (node.isWordEnding && distance <= threshold) {
            matches.get(distance).add(newWord);
        }

        // Early pruning
        int minDistance = distance - Math.abs(mistyped.length() - newWord.length());
        if (minDistance > threshold) {
            return;
        }

        // Traverse the middle subtree
        dictDFS(node.middle, mistyped, newWord);

        // Traverse the right subtree
        dictDFS(node.right, mistyped, currentWord);
    }

    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {
        // Plan: start at the root of the trie, append each initialized child node to its own word and check the edit distance
        // Subtract the difference of the length of the word from the threshold to get a max distance
        // If the distance is less than or equal to the threshold, keep going until an actual match is found
        // Keep progressing down the tree until all branches are explored or terminated
        dictDFS(dictTree.root, typed, "");

        // Add all matches starting from the smallest distance
        ArrayList<String> results = new ArrayList<String>();

        for (ArrayList<String> distanceMatches : matches) {
            for (String match : distanceMatches) {
                results.add(match);
            }
        }

        return results.toArray(new String[0]);
    }

    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary)  {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}