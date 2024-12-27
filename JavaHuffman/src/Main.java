import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите команду encode/decode");
        String command = scanner.next();
        System.out.println("Введите имя входного файла");
        String inputFile = scanner.next();
        System.out.println("Введите имя выходного файла");
        String outputFile = scanner.next();

        Huffman huffman = new Huffman();
        if (command.equals("encode")) {
            huffman.encode(inputFile, outputFile);
        } else if (command.equals("decode")) {
            huffman.decode(inputFile, outputFile);
        } else {
            System.out.println("Неверная команда. Используйте 'encode' или 'decode'.");
        }

        scanner.close();
    }
}