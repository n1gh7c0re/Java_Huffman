import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 3) {
            System.out.println("Использование:");
            System.out.println("Для кодирования: java HuffmanCoding encode inputFile outputFile");
            System.out.println("Для декодирования: java HuffmanCoding decode inputFile outputFile");
            return;
        }
        String command = args[0];
        String inputFile = args[1];
        String outputFile = args[2];

        Huffman huffman = new Huffman();
        try{
            if (command.equalsIgnoreCase("encode")) {
                huffman.encode(inputFile, outputFile);
            } else if (command.equalsIgnoreCase("decode")) {
                huffman.decode(inputFile, outputFile);
            } else {
                System.out.println("Неверная команда. Используйте 'encode' или 'decode'.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }
}