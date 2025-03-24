public class TST {
    public class TSTNode {
        char value;
        boolean isWordEnding;
        TSTNode less, equal, greater;

        // Basic constructor
        public TSTNode(char value) {
            this.value = value;
        }
    }

    TSTNode root;

    // Adds a word to the TST
    public void addWord(String word) {
        // If the tree is empty, create a new root node
        if (root == null) {
            root = new TSTNode(word.charAt(0));
        }
        TSTNode currentNode = root;
        int letterIndex = 0;

        // Traverse the tree until we've added all the letters of the word
        while (letterIndex < word.length()) {
            char letter = word.charAt(letterIndex);

            // If the current node's value is greater than the current letter, traverse to the less node
            if (letter < currentNode.value) {
                if (currentNode.less == null) {
                    currentNode.less = new TSTNode(letter);
                }
                currentNode = currentNode.less;
            }
            // If the current node's value is less than the current letter, traverse to the greater node
            else if (letter > currentNode.value) {
                if (currentNode.greater == null) {
                    currentNode.greater = new TSTNode(letter);
                }
                currentNode = currentNode.greater;
            }
            // If the current node's value is equal to the current letter, traverse to the equal node
            else {
                // Increment the letter index, as we've found a match
                letterIndex++;
                if (letterIndex < word.length()) {
                    if (currentNode.equal == null) {
                        currentNode.equal = new TSTNode(word.charAt(letterIndex));
                    }
                    currentNode = currentNode.equal;
                }
            }
        }
        // Once we've added all the letters, mark the node as a word ending
        currentNode.isWordEnding = true;
    }

    public boolean searchWord(String word) {
        TSTNode currentNode = root;
        int letterIndex = 0;

        // Traverse the tree until we've found the word or reached a null node
        while (currentNode != null && letterIndex < word.length()) {
            char letter = word.charAt(letterIndex);

            // If the current node's value is greater than the current letter, traverse to the less node
            if (letter < currentNode.value) {
                currentNode = currentNode.less;
                // If the current node's value is less than the current letter, traverse to the greater node
            } else if (letter > currentNode.value) {
                currentNode = currentNode.greater;
                // If the current node's value is equal to the current letter, traverse to the equal node
            } else {
                // Increment the letter index, as we've found a match
                letterIndex++;
                if (letterIndex < word.length()) {
                    currentNode = currentNode.equal;
                }
            }
        }
        // Return whether the current node is a word ending and not null
        return currentNode != null && currentNode.isWordEnding;
    }
}