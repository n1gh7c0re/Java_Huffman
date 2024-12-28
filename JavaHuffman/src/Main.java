import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Использование:");
            System.out.println("Для кодирования: java Main encode inputFile outputFile");
            System.out.println("Для декодирования: java Main decode inputFile outputFile");
            return;
        }
        Huffman huffman = new Huffman();
        String command = args[0];
        String inputFile = args[1];
        String outputFile = args[2];

        switch (command) {
            case "encode" -> {
                // Режим кодирования

                huffman.encode(inputFile, outputFile);
                System.out.println("Файл " + inputFile + " успешно закодирован в файл " + outputFile);
            }
            case "decode" -> {
                // Режим декодирования

                huffman.decode(inputFile, outputFile);
                System.out.println("Файл " + inputFile + " успешно декодирован в файл " + outputFile);
            }
            default -> {
                System.out.println("Неверный выбор. Пожалуйста, выберите от encode или decode.");
            }
        }
    }
}
