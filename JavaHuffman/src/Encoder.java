import java.io.*;
import java.util.Map;

public class Encoder {
    // Метод кодирования файла
    public void encode(String inputFile, String outputFile) throws IOException {
        String text = readFile(inputFile);
        Map<Character, Integer> freqMap = HuffmanTree.buildFrequencyMap(text);
        Node root = HuffmanTree.buildHuffmanTree(freqMap);
        Map<Character, String> huffmanCodes = HuffmanTree.buildCodeTable(root);

        // Кодирование текста
        StringBuilder encodedText = new StringBuilder();
        for (char ch : text.toCharArray()) {
            encodedText.append(huffmanCodes.get(ch));
        }

        // Сохранение закодированных данных в файл
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            oos.writeObject(freqMap); // Сохраняем частоты для декодирования
            oos.writeObject(encodedText.toString());
        }

        System.out.println("Файл успешно закодирован и сохранен в " + outputFile);
    }

    // Чтение файла и возврат его содержимого в виде строки
    private static String readFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
        }
        return sb.toString();
    }
}