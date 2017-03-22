package models;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * Created by ciaranroche on 22/03/2017.
 */
public class Huffman {

    public Huffman(){};

    public static class Node implements Comparable<Node> {
        private char ch;
        private int freq;
        private Node left, right;

        public Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return ((left == null) && (right == null));
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    public static void main(String[] args) throws IOException {
        String s = "mississippi river";

        HashMap<Character, Integer> frequencies = frequencies(s);
        Node root = huffmanTree(frequencies);

        HashMap<Character, String> encoding = generateCodes(frequencies.keySet(), root);

        String encodedString = encodeMessage(encoding, s);

        serializeTree(root);

    }

    public static HashMap<Character, Integer> frequencies(String s){
        HashMap<Character, Integer> freq = new HashMap<Character, Integer>();
        for(int i=0; i<s.length(); i++){
            char ch = s.charAt(i);
            if(freq.containsKey(ch)){
                freq.put(ch, freq.get(ch)+1);
            }else{
                freq.put(ch, 1);
            }
        }
        return freq;
    }

    public static Node huffmanTree(HashMap<Character, Integer> freq){
        PriorityQueue<Node> priority = new PriorityQueue<>();
        for(char ch : freq.keySet()){
            priority.add(new Node(ch, freq.get(ch), null, null));
        }
        while(priority.size() > 1){
            Node left = priority.poll();
            Node right = priority.poll();
            priority.add(new Node('\0', left.freq + right.freq, left, right));
        }
        return priority.poll();
    }

    private static HashMap<Character, String> generateCodes(Set<Character> chars, Node node) {
        final HashMap<Character, String> map = new HashMap<Character, String>();
        doGenerateCode(node, map, "");
        return map;
    }


    private static void doGenerateCode(Node node, Map<Character, String> map, String s) {
        if (node.left == null && node.right == null) {
            map.put(node.ch, s);
            return;
        }
        doGenerateCode(node.left, map, s + '0');
        doGenerateCode(node.right, map, s + '1' );
    }

    private static String encodeMessage(Map<Character, String> charCode, String sentence) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < sentence.length(); i++) {
            stringBuilder.append(charCode.get(sentence.charAt(i)));
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    private static void serializeTree(Node node) throws FileNotFoundException, IOException {
        final BitSet bitSet = new BitSet();
        try (ObjectOutputStream oosTree = new ObjectOutputStream(new FileOutputStream("././data/tree"))) {
            try (ObjectOutputStream oosChar = new ObjectOutputStream(new FileOutputStream("././data/char"))) {
                IntObject o = new IntObject();
                preOrder(node, oosChar, bitSet, o);
                bitSet.set(o.bitPosition, true);
                oosTree.writeObject(bitSet);
            }
        }
    }

    private static class IntObject {
        int bitPosition;
    }

    private static void preOrder(Node node, ObjectOutputStream oosChar, BitSet bitSet, IntObject intObject) throws IOException {
        if (node.left == null && node.right == null) {
            bitSet.set(intObject.bitPosition++, false);
            oosChar.writeChar(node.ch);
            return;
        }
        bitSet.set(intObject.bitPosition++, true);
        preOrder(node.left, oosChar, bitSet, intObject);

        bitSet.set(intObject.bitPosition++, true);
        preOrder(node.right, oosChar, bitSet, intObject);
    }

}

