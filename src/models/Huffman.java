package models;

import java.io.*;
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String s = "mississippi river";

        System.out.println(s);

        HashMap<Character, Integer> frequencies = frequencies(s);
        Node root = huffmanTree(frequencies);

        HashMap<Character, String> encoding = generateCodes(frequencies.keySet(), root);

        String encodedString = encodeMessage(encoding, s);

        serializeTree(root);

        serializeString(encodedString);

        Node dRoot = deserializeTree();

        decoder(dRoot);

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

    private static void serializeTree(Node node) throws IOException {
        final BitSet bitSet = new BitSet();
        try (ObjectOutputStream streamTree = new ObjectOutputStream(new FileOutputStream("././data/tree"))) {
            try (ObjectOutputStream streamChar = new ObjectOutputStream(new FileOutputStream("././data/char"))) {
                IntObject o = new IntObject();
                preOrder(node, streamChar, bitSet, o);
                bitSet.set(o.bitPosition, true);
                streamTree.writeObject(bitSet);
            }
        }
    }

    private static class IntObject {
        int bitPosition;
    }

    private static void preOrder(Node node, ObjectOutputStream streamChar, BitSet bitSet, IntObject intObject) throws IOException {
        if (node.left == null && node.right == null) {
            bitSet.set(intObject.bitPosition++, false);
            streamChar.writeChar(node.ch);
            return;
        }
        bitSet.set(intObject.bitPosition++, true);
        preOrder(node.left, streamChar, bitSet, intObject);

        bitSet.set(intObject.bitPosition++, true);
        preOrder(node.right, streamChar, bitSet, intObject);
    }

    private static BitSet getBitSet(String message) {
        final BitSet bitSet = new BitSet();
        int i = 0;
        for (i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '0') {
                bitSet.set(i, false);
            } else {
                bitSet.set(i, true);
            }
        }
        bitSet.set(i, true);
        return bitSet;
    }

    private static void serializeString(String message) throws IOException {
        final BitSet bitSet = getBitSet(message);
        System.out.println(bitSet);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("././data/encodedMessage"))){
            oos.writeObject(bitSet);
        }
    }



    public static Node deserializeTree() throws IOException, ClassNotFoundException {
        try(ObjectInputStream streamBranch = new ObjectInputStream(new FileInputStream("././data/tree"))){
            try(ObjectInputStream streamChar = new ObjectInputStream(new FileInputStream("././data/char"))){
                BitSet bitSet = (BitSet) streamBranch.readObject();
                return preOrder(bitSet, streamChar, new IntObject());
            }
        }
    }

    public static Node preOrder(BitSet bitSet, ObjectInputStream streamChar, IntObject intObject) throws IOException {
        Node node = new Node('\0',0,null,null);
        if(!bitSet.get(intObject.bitPosition)){
            intObject.bitPosition++;
            node.ch = streamChar.readChar();
            return node;
        }
        intObject.bitPosition = intObject.bitPosition + 1;
        node.left = preOrder(bitSet, streamChar, intObject);

        intObject.bitPosition = intObject.bitPosition + 1;
        node.right = preOrder(bitSet, streamChar, intObject);

        return node;
    }

    public static String decoder(Node n) throws IOException, ClassNotFoundException {
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("././data/encodedMessage"))){
            BitSet bitSet = (BitSet) inputStream.readObject();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i<(bitSet.length()-1);){
                Node temp = n;
                while(temp.left!=null){
                    if(!bitSet.get(i)){
                        temp = temp.left;
                    }else{
                        temp = temp.right;
                    }
                    i = i + 1;
                }
                sb.append(temp.ch);
            }
            System.out.println(sb.toString());
            return sb.toString();
        }
    }

}

