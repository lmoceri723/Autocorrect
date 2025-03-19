public class Trie {
    public class TrieNode {
        // Each node has space for 26 children, one for each letter of the alphabet, plus one for other characters
        TrieNode[] children = new TrieNode[26];
        // If a node is a word ending, set a boolean to communicate it
        boolean isWordEnding = false;
    }

    TrieNode root = new TrieNode();

    public char[] formatWord(String word) {
        return word.toCharArray();
    }

    // Add a word to the tree
    public void addWord(String word) {
        TrieNode current = root;

        for (char letter : formatWord(word)) {
            letter = Character.toLowerCase(letter);
            // turn the letter into an index 0-25
            int index = letter - 'a';


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
}