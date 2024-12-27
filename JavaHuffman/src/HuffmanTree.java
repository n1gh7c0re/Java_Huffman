import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree{
    // Построение карты частот символов
    public static Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char ch : text.toCharArray()) {
            freqMap.put(ch, freqMap.getOrDefault(ch, 0) + 1);
        }
        return freqMap;
    }

    // Построение дерева Хаффмана
    public static Node buildHuffmanTree(Map<Character, Integer> freqMap) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node(left.freq + right.freq, left, right);
            pq.add(parent);
        }

        return pq.poll();
    }

    // Построение таблицы кодов Хаффмана
    public static Map<Character, String> buildCodeTable(Node root) {
        Map<Character, String> codeTable = new HashMap<>();
        buildCodeHelper(root, "", codeTable);
        return codeTable;
    }

    public static void buildCodeHelper(Node node, String code, Map<Character, String> codeTable) {
        if (!node.isLeaf()) {
            buildCodeHelper(node.left, code + '0', codeTable);
            buildCodeHelper(node.right, code + '1', codeTable);
        } else {
            codeTable.put(node.ch, code);
        }
    }
}