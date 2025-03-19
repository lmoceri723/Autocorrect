public class TST {
    public class TSTNode {
        char character;
        TSTNode left, middle, right;
        boolean isWordEnding;

        TSTNode(char character) {
            this.character = character;
        }
    }

    TSTNode root;

    public void addWord(String word) {
        root = addWord(root, word.toCharArray(), 0);
    }

    private TSTNode addWord(TSTNode node, char[] word, int index) {
        char character = word[index];
        if (node == null) {
            node = new TSTNode(character);
        }
        if (character < node.character) {
            node.left = addWord(node.left, word, index);
        } else if (character > node.character) {
            node.right = addWord(node.right, word, index);
        } else {
            if (index + 1 < word.length) {
                node.middle = addWord(node.middle, word, index + 1);
            } else {
                node.isWordEnding = true;
            }
        }
        return node;
    }
}