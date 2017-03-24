package controllers;

import asg.cliche.Command;
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
        System.out.println("   \\\\  //  wWw  wWw             \\\\\\    ///       \\\\\\  ///\n" +
                "   (o)(o)  (O)  (O)   wWw   wWw ((O)  (O))   /)  ((O)(O)) \n" +
                "   ||  ||  / )  ( \\   (O)_  (O)_ | \\  / |  (o)(O) | \\ ||  \n" +
                "   |(__)| / /    \\ \\ .' __).' __)||\\\\//||   //\\\\  ||\\\\||  \n" +
                "   /.--.\\ | \\____/ |(  _) (  _)  || \\/ ||  |(__)| || \\ |  \n" +
                "  -'    `-'. `--' .` )/    )/    ||    ||  /,-. | ||  ||  \n" +
                "            `-..-'  (     (     (_/    \\_)-'   ''(_/  \\_) ");
        Shell shell = ShellFactory.createConsoleShell("Huff",
                "<<<<------------------------------------------------->>>>\n             " +
                        "Welcome to the Huffman Generator\n<<<<------------------------------------------------->>>>" +
                           "\n- ?help for instructions\n- ?list for commands",client);
        shell.commandLoop();
    }

    @Command(description = "Load External File")
    public void load() throws Exception{
        System.out.println("Please enter where to save the file: ");
        String save = scanner.nextLine();

        System.out.println("Please give path to file to be encoded: ");
        String file = scanner.nextLine();
        String data = huffAPI.prime(file);

    }

    @Command(description = "Encode a String of text to a File")
    public void encodeString() throws IOException, ClassNotFoundException {
        System.out.println("Please enter where to save the file: ");
        String save = scanner.nextLine();

        System.out.println("Please String of text to be encoded: ");
        String file = scanner.nextLine();
        huffAPI.encodeSave(file, save);

    }

    @Command(description = "Select a file to be decoded")
    public void decodeFile() throws IOException, ClassNotFoundException {
        System.out.println("Please enter location of file: ");
        String file = scanner.nextLine();
        huffAPI.decodeFile(file);
    }
}
