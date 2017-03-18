package models;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by ciaranroche on 18/03/2017.
 */
public class Huffman{
    public static class Node implements Comparable<Node> {
        private char ch;
        private int freq;
        private Node left, right;

        public Node(char ch, int freq, Node left, Node right){
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf(){
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return ((left == null) && (right == null));
        }

        public int compareTo(Node that){
            return this.freq - that.freq;
        }
    }

    public Huffman(){
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


    public static void depthFirst(Node node, String code, HashMap<Character, String> encoding){
        if(node.isLeaf()){
            encoding.put(node.ch, code);
        }else{
            if(node.left!=null)
                depthFirst(node.left, code+"0", encoding);
            if(node.right!=null)
                depthFirst(node.right, code+"1", encoding);
        }
    }

    public static HashMap<Character, String> encodied(Node root) {
        HashMap<Character, String> encoding = new HashMap<>();
        depthFirst( root, "", encoding );
        return encoding;
    }

    public static void main(String[] args){
        String s = "mississippi river";

        HashMap<Character, Integer> frequencies = frequencies(s);
        Node root = huffmanTree(frequencies);

        HashMap<Character, String> encoding = encodied(root);
        System.out.println("<<<<------------------------------------------------->>>>");
        System.out.println("The value to each key after encoding is:\n" + encoding);
        System.out.println("<<<<------------------------------------------------->>>>");

        String huff = "";
        for(int i=0; i<s.length(); i++){
            char ch = s.charAt(i);
            if(encoding.containsKey(ch)){
                huff = huff + encoding.get(ch);
            }
        }
        System.out.println("\nThe huffman encoded string of bits:\n" + huff);
        System.out.println("<<<<------------------------------------------------->>>>");
    }




}