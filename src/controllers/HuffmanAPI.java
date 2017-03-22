package controllers;

import models.Data;
import models.HuffmanBits;
import models.HuffmanEncode;
import utils.FileInput;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ciaranroche on 22/03/2017.
 */
public class HuffmanAPI {
    public static HuffmanEncode huff;
    public static HuffmanBits hBits;
    public static String dataString = "";

    public HuffmanAPI() {
        huff = new HuffmanEncode();

    }

    public static String prime(String file) throws Exception {
        FileInput in = new FileInput();
        List<Data> d = in.loadData(file);
        for (Data data : d) {
            dataString = dataString + data;
        }
        return dataString;
    }

    public static void encode(String s, String st) throws Exception {
        HashMap<Character, Integer> frequencies = huff.frequencies(s);
        HuffmanEncode.Node root = huff.huffmanTree(frequencies);

        HashMap<Character, String> encoded = huff.encodied(root);

        String huff = "";
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (encoded.containsKey(ch)) {
                huff = huff + encoded.get(ch);
            }
        }

        hBits = new HuffmanBits(huff.length());

        for( int i = 0; i < huff.length(); i++) {
            hBits.writeBit( huff.charAt(i) == '1');
        }

        byte[] b = hBits.toArray();
        for( byte a : b ) {
            System.out.format("%02X", a);

        }

        //System.out.println("The huffman encoded string of bits for: \n" + s + "\nis: \n" + huff);
        //System.out.println("The key for the code is: \n" + encoded);

        final OutputStream os = new FileOutputStream(st);
        final PrintStream ps = new PrintStream(os);
        ps.print(b.toString());
        ps.close();
        System.out.println("\nFile saved to: " + st);
    }

    public String getDataString() {
        return dataString;
    }

    public HuffmanEncode getHuff() {
        return huff;
    }

    public static void main(String[] args) throws Exception {
        String st = "././data/test";
        String s = "hello world";
        encode(s, st);
    }

}




