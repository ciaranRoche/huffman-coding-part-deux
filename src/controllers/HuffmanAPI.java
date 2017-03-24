package controllers;

import models.Data;
import models.Huffman;
import utils.FileInput;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by ciaranroche on 23/03/2017.
 */
public class HuffmanAPI {

    public static Huffman huff;
    public static Scanner scanner;

    public HuffmanAPI(){
        huff = new Huffman();
        scanner = new Scanner(System.in);
    }

    public static void encodeFile(String file, String location) throws Exception {
        String dataString = "";
        FileInput in = new FileInput();
        List<Data> d = in.loadData(file);
        for(Data data : d){
            dataString = dataString + data;
        }
        HashMap<Character, Integer> freq = huff.frequencies(dataString);
        Huffman.Node root = huff.huffmanTree(freq);
        HashMap<Character, String> encoding = huff.generateCodes(freq.keySet(), root);
        String encodedString = huff.encodeMessage(encoding, dataString);
        huff.serializeTree(root, location);
        huff.serializeString(encodedString, location);

    }

    public static void encodeSave(String s, String file) throws IOException, ClassNotFoundException {
        HashMap<Character, Integer> freq = huff.frequencies(s);
        Huffman.Node root = huff.huffmanTree(freq);

        HashMap<Character, String> encoding = huff.generateCodes(freq.keySet(), root);

        String encodedString = huff.encodeMessage(encoding, s);

        huff.serializeTree(root, file);

        huff.serializeString(encodedString, file);

        Huffman.Node decodeRoot = huff.deserializeTree(file);

        huff.decoder(decodeRoot, file);
    }

    public static void decodeFile(String file) throws IOException, ClassNotFoundException {
        Huffman.Node root = huff.deserializeTree(file);
        huff.decoder(root, file);
    }
}
