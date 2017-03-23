package controllers;

import asg.cliche.ShellFactory;
import models.Data;
import models.Huffman;
import asg.cliche.Shell;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by ciaranroche on 23/03/2017.
 */
public class Client {
    public Huffman huff;
    public HuffmanAPI huffAPI;
    public Data data;
    public static Scanner scanner;

    public void Client(){}

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        scanner = new Scanner(System.in);
        Shell shell = ShellFactory.createConsoleShell("Huff",
                "<<<<------------------------------------------------->>>>\n             " +
                        "Welcome to the Huffman Generator\n<<<<------------------------------------------------->>>>" +
                           "\n- ?help for instructions\n- ?list for commands",client);
        shell.commandLoop();

    }
}
