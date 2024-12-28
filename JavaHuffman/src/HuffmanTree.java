import java.util.Stack;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;

class HuffmanCompare implements Comparator<HuffmanTree> {
    @Override
    public int compare(HuffmanTree first, HuffmanTree second) {
        return first.key() - second.key();
    }
}

public class HuffmanTree {
    private Node root;

    public HuffmanTree(HashMap<Byte, Integer> map) {
        PriorityQueue<HuffmanTree> heap = new PriorityQueue<>(map.size(), new HuffmanCompare());
        map.forEach((key, value) -> heap.add(new HuffmanTree(key, value)));

        while (heap.size() > 1)
            heap.add(heap.poll().merge(heap.poll()));

        this.root = heap.poll().root;
    }

    private HuffmanTree(byte name, int key) {
        root = new Node(name, key);
    }

    public int key() {
        return root.key;
    }

    public HashMap<Byte, String> getTable() {
        HashMap<Byte, String> table = new HashMap<>();
        Stack<Entry<Node, String>> st = new Stack<>();

        st.push(new SimpleEntry<Node, String>(this.root,
                (this.root.left == null && this.root.right == null) ? "1" : ""));

        while (!st.empty()) {
            Entry<Node, String> top = st.pop();
            Node node = top.getKey();
            String value = top.getValue();

            if (node.left == null && node.right == null)
                table.put(node.name, value);

            if (node.left != null)
                st.push(new SimpleEntry<Node, String>(node.left, value + "0"));

            if (node.right != null)
                st.push(new SimpleEntry<Node, String>(node.right, value + "1"));
        }

        return table;
    }

    private HuffmanTree merge(HuffmanTree other) {
        this.root = new Node((byte) 0,
                this.root.key + other.root.key,
                this.root, other.root);

        return this;
    }
}