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

    public static HashMap<Character, Integer> frequencies(String s){
        System.out.println("The starting size of file is: " +(s.length()*8)+ " bits");
        HashMap<Character, Integer> freq = new HashMap<>();
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

    public static HashMap<Character, String> generateCodes(Set<Character> chars, Node node) {
        final HashMap<Character, String> map = new HashMap<>();
        doGenerateCode(node, map, "");
        return map;
    }

    public static void doGenerateCode(Node node, Map<Character, String> map, String s) {
        if (node.left == null && node.right == null) {
            map.put(node.ch, s);
            return;
        }
        doGenerateCode(node.left, map, s + '0');
        doGenerateCode(node.right, map, s + '1' );
    }

    public static String encodeMessage(Map<Character, String> charCode, String sentence) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sentence.length(); i++) {
            stringBuilder.append(charCode.get(sentence.charAt(i)));
        }

        System.out.println("The encoded string of bits looks like:\n" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static void serializeTree(Node node, String location) throws IOException {
        final BitSet bitSet = new BitSet();
        try (ObjectOutputStream streamTree = new ObjectOutputStream(new FileOutputStream(location +"-tree"))) {
            try (ObjectOutputStream streamChar = new ObjectOutputStream(new FileOutputStream(location+"-char"))) {
                IntObject o = new IntObject();
                preOrder(node, streamChar, bitSet, o);
                bitSet.set(o.bitPosition, true);
                streamTree.writeObject(bitSet);
            }
        }
    }

    public static class IntObject {
        int bitPosition;
    }

    public static void preOrder(Node node, ObjectOutputStream streamChar, BitSet bitSet, IntObject intObject) throws IOException {
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

    public static BitSet getBitSet(String message) {
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

    public static void serializeString(String message, String location) throws IOException {
        final BitSet bitSet = getBitSet(message);
        System.out.println("Size when converted to bits : " + (bitSet.length()-1));
        System.out.println("Size when converted to bytes: " + (Math.ceil((bitSet.length()-1)/8)));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(location+"-encoded"))){
            oos.writeObject(bitSet);
        }
    }



    public static Node deserializeTree(String location) throws IOException, ClassNotFoundException {
        try(ObjectInputStream streamBranch = new ObjectInputStream(new FileInputStream(location + "-tree"))){
            try(ObjectInputStream streamChar = new ObjectInputStream(new FileInputStream(location + "-char"))){
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

    public static String decoder(Node n, String location) throws IOException, ClassNotFoundException {
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(location + "-encoded"))){
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
            System.out.println("The decoded file at: " +location+ " is:\n" +sb.toString());
            saveDecode(sb.toString(), location);
            return sb.toString();
        }
    }

    public static void saveDecode(String s, String location) throws FileNotFoundException {

        final OutputStream os = new FileOutputStream(location+"-decoded");
        final PrintStream ps = new PrintStream(os);
        ps.print(s);
        ps.close();
    }

}

