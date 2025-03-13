import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author YOUR NAME HERE
 */
public class Autocorrect {
    private class TrieNode {
        // Each node has space for 26 children, one for each letter of the alphabet, plus one for other characters
        TrieNode[] children = new TrieNode[26];
        // If a node is a word ending, set a boolean to communicate it
        boolean isWordEnding = false;
    }

    private class Trie {
        TrieNode root = new TrieNode();

        public char[] formatWord(String word) {
            return word.toCharArray();
        }

        // Add a word to the tree
        public void addWord(String word) {
            TrieNode current = root;

            for (char letter : formatWord(word)) {
                int index = letter;

                // If the node doesn't exist, create it
                if (current.children[index] == null) {
                    current.children[index] = new TrieNode();
                }
                // Move to the next node
                current = current.children[index];
            }
            // Set the last node to be the end of a word
            current.isWordEnding = true;
        }

        // Search for a word in the tree
        // Does basically the same checks as addWord by following its path to validate a word
        public boolean searchWord(String word) {
            TrieNode node = root;

            for (char letter : formatWord(word)) {
                int index = letter;
                // If the node doesn't exist, no combination of these letters makes a word
                if (node.children[index] == null) {
                    return false;
                }
                // Move on to the next node
                node = node.children[index];
            }

            // Once we've gotten to the end of the word, we can check if it's a valid ending and return the result
            return node.isWordEnding;
        }
    }

    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    public String[] dictionary;
    public int threshold;

    public Trie trie;

    public Autocorrect(String[] words, int threshold) {
        this.dictionary = words;
        this.threshold = threshold;

        trie = new Trie();
        for (String word : words) {
            trie.addWord(word);
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
    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {

        return new String[0];
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