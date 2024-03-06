import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class App {
    static class Node {
        int key;
        Node left, right;

        public Node(int item) {
            key = item;
            left = right = null;
        }
    }

    static class BinaryTree {
        Node root;

        BinaryTree() {
            root = null;
        }

        void insert(int key) {
            root = insertRec(root, key);
        }

        Node insertRec(Node root, int key) {
            if (root == null) {
                root = new Node(key);
                return root;
            }
            if (key < root.key)
                root.left = insertRec(root.left, key);
            else if (key > root.key)
                root.right = insertRec(root.right, key);
            return root;
        }

        void printToFile(Node root, String filename) throws IOException {
            FileWriter writer = new FileWriter(filename);
            int maxLevel = maxLevel(root);

            printNodeInternal(Collections.singletonList(root), 1, maxLevel, writer);
            writer.flush();
            writer.close();
        }

        void printNodeInternal(List<Node> nodes, int level, int maxLevel, FileWriter writer) throws IOException {
            if (nodes.isEmpty() || isAllElementsNull(nodes))
                return;

            int floor = maxLevel - level;
            int edgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
            int firstSpaces = (int) Math.pow(2, (floor)) - 1;
            int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

            printWhitespaces(firstSpaces, writer);

            List<Node> newNodes = new LinkedList<>();
            for (Node node : nodes) {
                if (node != null) {
                    writer.write("[" + node.key + "]");
                    newNodes.add(node.left);
                    newNodes.add(node.right);
                } else {
                    newNodes.add(null);
                    newNodes.add(null);
                    writer.write("   ");
                }

                printWhitespaces(betweenSpaces, writer);
            }
            writer.write("\n");

            for (int i = 1; i <= edgeLines; i++) {
                for (int j = 0; j < nodes.size(); j++) {
                    printWhitespaces(firstSpaces - i, writer);
                    if (nodes.get(j) == null) {
                        printWhitespaces(edgeLines + edgeLines + i + 1, writer);
                        continue;
                    }

                    // if (nodes.get(j).left != null) {
                    //     writer.write("/");
                    // } else {
                    //     printWhitespaces(1, writer);
                    // }

                    // printWhitespaces(i + i - 1, writer);

                    // if (nodes.get(j).right != null)
                    //     writer.write("\\");
                    // else
                    //     printWhitespaces(1, writer);

                    if (nodes.get(j).left != null) {
                        writer.write("/");
                        printWhitespaces(i + i - 1, writer);
                    } else {
                        printWhitespaces(1 + i + i, writer);
                    }
        
                    if (nodes.get(j).right != null) {
                        printWhitespaces(i + i - 1, writer);
                        writer.write("\\");
                    } else {
                        printWhitespaces(1, writer);
                    }

                    printWhitespaces(edgeLines + edgeLines - i, writer);
                }

                writer.write("\n");
            }

            printNodeInternal(newNodes, level + 1, maxLevel, writer);
        }

        private void printWhitespaces(int count, FileWriter writer) throws IOException {
            for (int i = 0; i < count; i++)
                writer.write(" ");
        }

        private int maxLevel(Node node) {
            if (node == null)
                return 0;

            return Math.max(maxLevel(node.left), maxLevel(node.right)) + 1;
        }

        private boolean isAllElementsNull(List list) {
            for (Object object : list) {
                if (object != null)
                    return false;
            }
            return true;
        }
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();

        // Replace the below line with the actual list of numbers provided by the user.
        int[] values = {4, 2, 1, 3, 6, 5, 7};
        for (int value : values) {
            tree.insert(value);
        }

        try {
            tree.printToFile(tree.root, "BST.txt");
            System.out.println("Successfully printed the BST to BST.txt");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
