import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedWriter;

/** CompactPrefixTree class, implements Dictionary ADT and
 *  several additional methods. Can be used as a spell checker.
 *  Fill in code in the methods of this class. You may add additional methods. */
public class CompactPrefixTree implements Dictionary {

    private Node root; // the root of the tree

    /** Default constructor.
     * Creates an empty "dictionary" (compact prefix tree).
     * */
    public CompactPrefixTree(){
        root = new Node();
    }

    /**
     * Creates a dictionary ("compact prefix tree")
     * using words from the given file.
     * @param filename the name of the file with words
     */
    public CompactPrefixTree(String filename) {

        root = new Node();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String currentWord = br.readLine();

            while(currentWord != null) {
                add(currentWord);
                currentWord = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("Exception with inputFile Rong");
            e.printStackTrace();
        }
    }

    /** Adds a given word to the dictionary.
     * @param word the word to add to the dictionary
     */
    public void add(String word) {
        root = add(word.toLowerCase(), root); // Calling private add method
    }

    /**
     * Checks if a given word is in the dictionary
     * @param word the word to check
     * @return true if the word is in the dictionary, false otherwise
     */
    public boolean check(String word) {
        return check(word.toLowerCase(), root); // Calling private check method
    }

    /**
     * Checks if a given prefix is a prefix of any word stored in the dictionary
     * @param prefix The prefix of a word
     * @return true if the prefix is a prefix of any word in the dictionary, false otherwise
     */
    public boolean checkPrefix(String prefix) {
        return checkPrefix(prefix.toLowerCase(), root); // Calling private checkPrefix method
    }


    /**
     * Prints all the words in the dictionary, in alphabetical order,
     * one word per line.
     */
    public void print() {
        print("", root); // Calling private print method
    }

    /**
     * Print out the nodes of the compact prefix tree, in a pre-order fashion.
     * First, print out the root at the current indentation level
     * (followed by * if the node's valid bit is set to true),
     * then print out the children of the node at a higher indentation level.
     */
    public void printTree() {
        printTreeConsole("", root);
    }

    /**
     * Private helper method used to print out the nodes of the compact prefix tree.
     * @param spacing
     * @param node
     */
    private void printTreeConsole(String spacing, Node node) {

        if (node == null) {
            return;
        }

        if(node.isWord == true) {
            System.out.println(spacing + node.prefix + "*");
        }

        if (node.isWord == false) {
            System.out.println(spacing + node.prefix);
        }

        for (Node children: node.children) {
            printTreeConsole(spacing + " ", children);
        }

    }


    /**
     * Prints out the nodes of the tree to a file, using indentations to specify the level
     * of the node.
     * @param filename the name of the file where to output the tree
     */
    public void printTree(String filename) {

        Path outPath = Paths.get(filename);
        StringBuilder sb = new StringBuilder();

        try (BufferedWriter out = Files.newBufferedWriter(outPath)){

            out.write(printTreeFile("", root, sb));

        }  catch(IOException e) {
            e.getMessage();
        }

    }

    /**
     * Private helper method uses String builder to append the items to the
     * file in the required format
     * @param spacing
     * @param node
     * @param builder
     * @return
     */
    private String printTreeFile(String spacing, Node node, StringBuilder builder) {

        if (node == null) {
            return "\n";
        }

        if (node.isWord == true) {
            builder.append(spacing + node.prefix + "*\n");
        }

        if (node.isWord == false) {
            builder.append(spacing + node.prefix + "\n");
        }

        for (Node children: node.children) {
            printTreeFile(spacing + " ", children, builder);
        }

        return builder.toString();
    }


    /**
     * Return an array of the entries in the dictionary that are as close as possible to
     * the parameter word.
     *
     * If the word passed in is in the dictionary, then
     * return an array of length 1 that contains only that word.
     *
     * If the word is not in the dictionary, then return an array of numSuggestions different words
     * that are in the dictionary, that are as close as possible to the target word.
     *
     * Implementation details are up to you, but you are required to make it efficient
     * and make good use of the compact prefix tree.
     *
     * @param word The word to check
     * @param numSuggestions The length of the array to return.  Note that if the word is
     * in the dictionary, this parameter will be ignored, and the array will contain a
     * single world.
     * @return An array of the closest entries in the dictionary to the target word
     */

    public String[] suggest(String word, int numSuggestions) {

        return suggestHelper(word, root.prefix, numSuggestions, this.root);
    }

    /**
     * Private helper method recursively traverses the list and adds
     * suggested words to the array with the same prefix as the word passed in
     * @param word
     * @param prefixAtNode
     * @param numSuggestions
     * @param node
     * @return An array of words suggested given the input word
     */

    private String[] suggestHelper(String word, String prefixAtNode, int numSuggestions, Node node) {


        String new0 = prefixAtNode + word;

        if(check(new0) == true) {
            String[] result = new String[1];
            result[0] = new0;
            return result;

        }

        String[] result = new String[numSuggestions];

        if (word.equals(node.prefix)) {
            int count = 0;
            while(count < numSuggestions) {
                if (searchSuffix(result, node, prefixAtNode) != null) {
                    result[count] = prefixAtNode + searchSuffix(result, node, prefixAtNode);
                } else {
                    return suggestHelper(word.substring(0, word.length() - 2), "", numSuggestions, root);
                }
                count++;
            }
            return result;

        } else {

            String common = commonPrefix(word, node.prefix);

            String suffix = suffix(word, node.prefix);
            int indx = getIndexOfCharacter(suffix);
            Node newNode = node.children[indx];

            if (newNode == null) {
                int count = 0;
                while (count < numSuggestions) {
                    if (searchSuffix(result, node, prefixAtNode) != null) {
                        result[count] = prefixAtNode + searchSuffix(result, node, prefixAtNode);
                    } else {
                        return suggestHelper(word.substring(0, word.length() - 2), "", numSuggestions, root);
                    }
                    count++;
                }
                return result;
            }

            String newPrefix = prefixAtNode + node.prefix;
            return suggestHelper(suffix, newPrefix, numSuggestions, newNode);
        }
    }

    /**
     * Private method recursively searches for the different
     * suffix that matches with the given prefix at node
     * @param array
     * @param node
     * @param prefixAtNode
     * @return returns the prefix of the node when isWord = true.
     */
    private String searchSuffix(String[] array, Node node, String prefixAtNode) {

        boolean exists = checkList(prefixAtNode + node.prefix, array);

        if (node.isWord == true && exists == false) {
            return node.prefix;
        }

        int count = 0;
        prefixAtNode = prefixAtNode + node.prefix;
        while(count < node.children.length) {
            Node newNode = node.children[count];
            if(newNode != null && searchSuffix(array, newNode, prefixAtNode) != null) {
                return node.prefix + searchSuffix(array, newNode, prefixAtNode);
            }
            count++;
        }
        return null;
    }


    /**
     * Private method checks to see if the word/prefix already exists in the array
     * @param checkPrefix
     * @param array
     * @return true if it exists, false otherwise.
     */
    private boolean checkList(String checkPrefix, String[] array) {

        for (String item: array) {
            if (checkPrefix.equals(item)) {
                return true;
            }
        }
        return false;
    }


    // ---------- Private helper methods ---------------

    /**
     *  A private add method that adds a given string to the tree
     * @param word the string to add
     * @param node the root of a tree where we want to add a new string

     * @return a reference to the root of the tree that contains s
     */
    private Node add(String word, Node node) {

        if(node == null) {
            return null;
        }

        String comm = commonPrefix(word, node.prefix); //Common prefix

        if(node.prefix.equals(word)) {
            node.isWord = true;
            return node;

        } else if(comm.equals(node.prefix)) {
            String suffix = suffix(word, node.prefix);
            int indx = getIndexOfCharacter(suffix);

            if (node.children[indx] == null) {
                Node newNode = new Node();
                newNode.set(suffix, true);
                node.children[indx] = newNode;
                return node;

            } else {
                node.children[indx] = add(suffix, node.children[indx]);
                return node;
            }

        } else {

            Node newNode = new Node();
            Node newNode1 = new Node();

            String common = commonPrefix(word, node.prefix);
            String suffix1 = suffix(node.prefix, word);
            String suffix2 = suffix(word, node.prefix);

            newNode.set(common, false);
            node.set(suffix1, node.isWord);

            int indx = getIndexOfCharacter(suffix1);
            newNode.children[indx] = node;

            int indx1 = getIndexOfCharacter(suffix2);
            newNode1.set(suffix2, true);

            newNode.children[indx1] = newNode1;
            return newNode;
        }

    }

    /**
     * Finds the common prefix between current and existing
     * @param current
     * @param existing
     * @return
     */
    private String commonPrefix(String current, String existing) {

        int count = 0;
        String prefix = "";

        while (count < current.length() && count < existing.length()) {
            char one = current.charAt(count);
            char two = existing.charAt(count);
            if (one != two) {
                break;
            }
            count++;
        }

        prefix = current.substring(0, count);

        return prefix;

    }

    /**
     * Finds the common prefix between word1 and word2
     * Returns the suffix of word1 without its prefix
     * @param word1
     * @param word2
     * @return
     */

    private String suffix(String word1, String word2) {

        int count = 0;
        String suffix = "";

        while (count < word1.length() && count < word2.length()) {
            char one = word1.charAt(count);
            char two = word2.charAt(count);
            if (one != two) {
                break;
            }
            count++;
        }

        suffix = word1.substring(count, word1.length());

        return suffix;
    }

    /**
     * Private method converts the the first letter of the input
     * to a number between 0 and 25 using ASCII code
     * @param word
     * @return
     */
    private int getIndexOfCharacter(String word) {


        char c = word.charAt(0);
        char a = 'a';

        int index = (int) c - (int) a;

        return index;
    }



    /** A private method to check whether a given string is stored in the tree.
     *
     * @param word the string to check
     * @param node the root of a tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean check(String word, Node node) {

        if (node == null) {
            return false;
        }

        if (!commonPrefix(word, node.prefix).equals(node.prefix)) {
            return false;
        }

        if (node.prefix.equals(word)) {
            return node.isWord;
        }

        if (commonPrefix(word, node.prefix).equals(node.prefix)) {
            String suffix = suffix(word, node.prefix);
            int indx = getIndexOfCharacter(suffix);
            return check(suffix, node.children[indx]);
        }

        return false; // don't forget to change it
    }

    /**
     * A private recursive method to check whether a given prefix is in the tree
     *
     * @param prefix the prefix
     * @param node the root of the tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean checkPrefix(String prefix, Node node) {

        if(node == null) {
            return false;
        }

        if(prefix.equals(commonPrefix(prefix, node.prefix))) {
            return true;
        }
        if(commonPrefix(prefix, node.prefix).equals(node.prefix)) {
            String suffix = suffix(prefix, node.prefix);
            int indx = getIndexOfCharacter(suffix);
            return checkPrefix(suffix, node.children[indx]);
        }

        return false;

    }

    /**
     * Outputs all the words stored in the dictionary
     * to the console, in alphabetical order, one word per line.
     * @param word the string obtained by concatenating prefixes on the way to this node
     * @param node the root of the tree
     */
    private void print(String word, Node node) {

        if(node.isWord == true) {
            System.out.println(word + node.prefix);
        }
        for(Node children: node.children) {
            if(children != null) {
                print(word + node.prefix, children);
            }
        }
    }

    // FILL IN CODE: add a private suggest method. Decide which parameters
    // it should have

    // --------- Private class Node ------------
    // Represents a node in a compact prefix tree
    private class Node {
        String prefix; // prefix stored in the node
        Node children[]; // array of children (26 children)
        boolean isWord; // true if by concatenating all prefixes on the path from the root to this node, we get a valid word

        Node() {
            prefix = "";
            children = new Node[26]; // initialize the array of children
            isWord = false;
        }


        public void set(String word, boolean isAWord) {
            this.prefix = word;
            this.isWord = isAWord;
        }

    }

}
