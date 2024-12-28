import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Encoder {
    public void encode(String input, String output) throws IOException {
        FileInputStream fileIn = new FileInputStream(input);
        FileOutputStream fileOut = new FileOutputStream(output);

        HashMap<Byte, Integer> counter = readStatistic(fileIn);
        HuffmanTree tree = new HuffmanTree(counter);

        HuffmanHeader head = new HuffmanHeader();
        head.extension = getExtension(output);
        head.table = tree.getTable();
        head.calculateTablePtr();
        head.calculateDataPtr();
        head.calculateDataSize(counter);
        head.write(fileOut);

        fileIn.close();
        fileIn = new FileInputStream(input);
        writeData(fileIn, fileOut, head.table);

        fileIn.close();
        fileOut.close();
    }

    private HashMap<Byte, Integer> readStatistic(FileInputStream fis) throws IOException {
        HashMap<Byte, Integer> map = new HashMap<>();
        int byteRead;
        while ((byteRead = fis.read()) != -1) {
            byte b = (byte) byteRead;
            map.put(b, map.getOrDefault(b, 0) + 1);
        }
        return map;
    }

    private String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return (i > 0) ? fileName.substring(i + 1) : "";
    }

    private void writeData(FileInputStream fis, FileOutputStream fos, HashMap<Byte, String> table) throws IOException {
        StringBuilder data = new StringBuilder();
        int byteRead;
        while ((byteRead = fis.read()) != -1) {
            byte b = (byte) byteRead;
            data.append(table.get(b));

            // Записываем полные байты
            while (data.length() >= 8) {
                String byteString = data.substring(0, 8);
                data.delete(0, 8);
                writeBits(fos, byteString);
            }
        }

        // Записываем оставшиеся биты, дополняя нулями до полного байта
        if (data.length() > 0) {
            while (data.length() < 8) {
                data.append('0');
            }
            writeBits(fos, data.toString());
        }
    }

    private void writeBits(FileOutputStream fos, String byteString) throws IOException {
        if (byteString.length() == 0) return;
        while (byteString.length() < 8) {
            byteString += '0';
        }
        byte tmp = 0;
        for (int i = 0; i < 8; i++) {
            tmp = (byte) ((tmp << 1) | (byteString.charAt(i) - '0'));
        }
        fos.write(tmp);
    }
}
