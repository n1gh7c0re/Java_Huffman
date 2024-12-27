import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Decoder {
    // Метод декодирования файла
    public void decode(String inputFile, String outputFile) throws IOException, ClassNotFoundException {
        Map<Character, Integer> freqMap;
        String encodedText;

        // Чтение закодированных данных из файла
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile))) {
            freqMap = (Map<Character, Integer>) ois.readObject();
            encodedText = (String) ois.readObject();
        }

        Node root = HuffmanTree.buildHuffmanTree(freqMap);
        String decodedText = decodeText(root, encodedText);

        // Сохранение декодированного текста
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(decodedText);
        }

        System.out.println("Файл успешно декодирован и сохранен в " + outputFile);
    }



    // Декодирование текста
    private static String decodeText(Node root, String encodedText) {
        StringBuilder decodedText = new StringBuilder();
        Node current = root;
        for (char bit : encodedText.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            if (current.isLeaf()) {
                decodedText.append(current.ch);
                current = root;
            }
        }
        return decodedText.toString();
    }
}