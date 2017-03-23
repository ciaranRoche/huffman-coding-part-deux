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
    public static String dataString = "";
    public static Scanner scanner;

    public HuffmanAPI(){
        huff = new Huffman();
        scanner = new Scanner(System.in);
    }

    public static String prime(String file) throws Exception {
        FileInput in = new FileInput();
        List<Data> d = in.loadData(file);
        for(Data data : d){
            dataString = dataString + data;
        }
        return dataString;
    }

    public static void encodeSave(String s, String file) throws IOException {
        HashMap<Character, Integer> freq = huff.frequencies(s);
        Huffman.Node root = huff.huffmanTree(freq);

        HashMap<Character, String> encoding = huff.generateCodes(freq.keySet(), root);

        String encodedString = huff.encodeMessage(encoding, s);

        huff.serializeTree(root, file);

        huff.serializeString(encodedString, file);

    }
}
