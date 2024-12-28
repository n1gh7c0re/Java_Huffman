import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HuffmanHeader {
    public String signature = "HUFFMAN";
    public int table_ptr;
    public int data_ptr;
    public long data_size;
    public String extension;
    public HashMap<Byte, String> table;

    public void calculateTablePtr() {
        this.table_ptr = signature.length();
        this.table_ptr += 2 * Integer.BYTES;
        this.table_ptr += Long.BYTES;
        this.table_ptr += extension.length();
    }

    public void calculateDataPtr() {
        this.data_ptr = table_ptr;
        table.forEach((key, val) -> this.data_ptr += (2 * Byte.BYTES + (val.length() + 7) / 8));
    }

    public void calculateDataSize(HashMap<Byte, Integer> count) {
        this.data_size = 0;
        table.forEach((key, val) -> this.data_size += (count.get(key) * val.length()));
    }

    public void write(FileOutputStream fos) throws IOException {
        byte[] buffer = new byte[data_ptr];
        int count = 0;

        for (int i = 0; i < signature.length(); i++)
            buffer[count++] = (byte) signature.charAt(i);

        for (int i = 0; i < Integer.BYTES; i++)
            buffer[count++] = (byte) (table_ptr >> (i * 8));

        for (int i = 0; i < Integer.BYTES; i++)
            buffer[count++] = (byte) (data_ptr >> (i * 8));

        for (int i = 0; i < Long.BYTES; i++)
            buffer[count++] = (byte) (data_size >> (i * 8));

        for (int i = 0; i < extension.length(); i++)
            buffer[count++] = (byte) extension.charAt(i);

        for (Map.Entry<Byte, String> entry : table.entrySet()) {
            buffer[count++] = entry.getKey();
            buffer[count++] = (byte) entry.getValue().length();

            byte[] value = this.stringToBytes(entry.getValue());
            for (int i = 0; i < value.length; i++)
                buffer[count++] = value[i];
        }

        fos.write(buffer, 0, count);
    }

    public boolean read(FileInputStream fis) throws IOException {
        int size = signature.length() + 2 * Integer.BYTES + Long.BYTES;
        if (!readHead(fis, size))
            return false;

        size = this.table_ptr - size;
        if (size > 0)
            if (!readExtension(fis, size))
                return false;

        size = this.data_ptr - this.table_ptr;
        if (!readTable(fis, size))
            return false;

        return true;
    }

    private boolean readHead(FileInputStream fis, int size) throws IOException {
        byte[] buffer = new byte[size];
        this.table_ptr = 0;
        this.data_ptr = 0;
        this.data_size = 0;

        if (fis.read(buffer) < size)
            return false;

        for (int i = 0; i < signature.length(); i++)
            if (buffer[i] != (byte) signature.charAt(i))
                return false;

        int index = signature.length();
        for (int i = index + Integer.BYTES - 1; i >= index; --i)
            this.table_ptr = (this.table_ptr << 8) | (buffer[i] & 0x000000ff);

        index += Integer.BYTES;
        for (int i = index + Integer.BYTES - 1; i >= index; --i)
            this.data_ptr = (this.data_ptr << 8) | (buffer[i] & 0x000000ff);

        index += Integer.BYTES;
        for (int i = index + Long.BYTES - 1; i >= index; --i)
            this.data_size = (this.data_size << 8) | (buffer[i] & 0x000000ff);

        return true;
    }

    private boolean readExtension(FileInputStream fis, int size) throws IOException {
        byte[] buffer = new byte[size];

        if (fis.read(buffer) < size)
            return false;

        this.extension = new String(buffer);

        return true;
    }

    private boolean readTable(FileInputStream fis, int size) throws IOException {
        byte[] buffer = new byte[size];

        if (fis.read(buffer) < size)
            return false;

        this.table = new HashMap<>();
        for (int i = 0; i < size;) {
            if (table.containsKey(buffer[i]))
                return false;

            table.put(buffer[i], BytesToString(buffer, i + 2, buffer[i + 1] & 0x000000ff));
            i += 2 + ((buffer[i + 1] & 0x000000ff) + 7) / 8;
        }

        return true;
    }

    private byte[] stringToBytes(String str) {
        byte[] arr = new byte[(str.length() + 7) / 8];

        for (int i = 0; i < str.length(); i += 8) {
            byte tmp = 0;
            for (int j = 0; j < 8; j++)
                tmp = (byte) ((tmp << 1) |
                        ((i + j < str.length()) ? str.charAt(i + j) - '0' : 0));
            arr[i / 8] = tmp;
        }

        return arr;
    }

    private String BytesToString(byte[] bytes, int index, int size) {
        String bits = "";

        for (int i = index; i < index + size / 8; i++)
            for (int j = 7; j >= 0; --j)
                bits += (char) ('0' + ((bytes[i] >> j) & 1));

        for (int j = 7; j >= 8 - (size % 8); --j)
            bits += (char) ('0' + ((bytes[index + size / 8] >> j) & 1));

        return bits;
    }
}