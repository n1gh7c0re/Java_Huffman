import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Decoder {
    public void decode(String input, String output) throws IOException {
        FileInputStream fileIn = new FileInputStream(input);
        HuffmanHeader head = new HuffmanHeader();
        if (!head.read(fileIn))
            return;

        FileOutputStream fileOut = new FileOutputStream(output);
        HashMap<String, Byte> detable = new HashMap<>();
        head.table.forEach((key, value) -> detable.put(value, key));

        int count = 0;
        String key = "";
        byte[] buffer = new byte[1024];
        while ((count = fileIn.read(buffer)) != -1) {
            byte[] result = new byte[8 * buffer.length];
            int index = 0;

            for (int i = 0; i < count; i++) {
                for (int j = 7; j >= 0; --j) {
                    key += (char) ('0' + ((buffer[i] >> j) & 1));
                    if (detable.containsKey(key)) {
                        result[index++] = detable.get(key);
                        key = "";
                    }
                }
            }

            fileOut.write(result, 0, index);
        }

        fileIn.close();
        fileOut.close();
    }
}