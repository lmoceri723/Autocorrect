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
    // The dictionary of words to compare against
    public String[] dictionary;

    // The maximum edit distance to consider
    public int threshold;

    // The TST dictionary tree
    public TST dictTree;

    /**
     * Constucts an instance of the Autocorrect class.
     */
    public Autocorrect() {
        this.dictionary = loadDictionary("large");
        this.threshold = 1;

        dictTree = new TST();
        for (String word : dictionary) {
            dictTree.addWord(word);
        }
    }

    // Set the threshold for the edit distance
    public void setThreshold(int threshold) {
        this.threshold = threshold;
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

    public void dictDFS(TST.TSTNode node, String mistyped, String currentWord, ArrayList<ArrayList<String>> matches) {
        if (node == null) {
            return;
        }

        // Traverse the left subtree first
        // As we want to go from the smallest words to the largest
        // So we need to traverse as far left as we can before we start checking the words
        dictDFS(node.less, mistyped, currentWord, matches);

        // Check the current node
        String newWord = currentWord + node.value;
        int distance = editDistance(mistyped, newWord);
        if (node.isWordEnding && distance <= threshold) {
            matches.get(distance).add(newWord);
        }

        // Early pruning if the branch results in a distance greater than the threshold in its min case
        int leftover = Math.abs(mistyped.length() - newWord.length());
        int minDistance = distance - leftover;
        if (minDistance > threshold + 1) {
            return;
        }

        // Traverse the middle subtree
        dictDFS(node.equal, mistyped, newWord, matches);

        // Traverse the right subtree
        dictDFS(node.greater, mistyped, currentWord, matches);
    }

    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {
        boolean result = dictTree.searchWord(typed);
        if (result == true) {
            return new String[]{typed};
        }

        // Create and initialize list of lists to store the matches
        ArrayList<ArrayList<String>> matches = new ArrayList<ArrayList<String>>();
        for (int i = 0; i <= threshold; i++) {
            matches.add(new ArrayList<String>());
        }

        // Traverse the dictionary tree to find matches
        dictDFS(dictTree.root, typed, "", matches);

        // Count the number of matches
        int size = 0;
        for (ArrayList<String> distanceMatches : matches) {
            size += distanceMatches.size();
        }

        // Create and fill an array to store the results
        String[] results = new String[size];
        int index = 0;

        for (ArrayList<String> match : matches) {
            for (String word : match) {
                results[index] = word;
                index++;
            }
        }

        return results;
    }

    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary) {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
