import java.io.IOException;

public class Huffman {
    private Encoder encoder = new Encoder();
    private Decoder decoder = new Decoder();

    public void encode(String input, String output) throws IOException {
        encoder.encode(input, output);
    }

    public void decode(String input, String output) throws IOException {
        decoder.decode(input, output);
    }
}