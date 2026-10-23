import java.util.HashMap;
import java.util.Map;

/**
 * Least Recently Used (LRU) cache with average O(1) get and put operations.
 *
 * The HashMap provides direct access to nodes by key. The doubly linked list
 * keeps the most recently used node near the head and the least recently used
 * node near the tail.
 */
public class LRUCache {
    private static final class Node {
        int key;
        int value;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<Integer, Node> nodes;
    private final Node head; // Most recently used nodes follow head.
    private final Node tail; // Least recently used nodes precede tail.

    public LRUCache(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative.");
        }

        this.capacity = capacity;
        this.nodes = new HashMap<>();
        this.head = new Node(-1, -1);
        this.tail = new Node(-1, -1);

        head.next = tail;
        tail.prev = head;
    }

    /**
     * Returns the value for key and marks the entry as recently used.
     * Returns -1 if key is not present.
     */
    public int get(int key) {
        Node node = nodes.get(key);
        if (node == null) {
            return -1;
        }

        moveToFront(node);
        return node.value;
    }

    /**
     * Inserts or updates an entry. If capacity is exceeded, evicts the
     * least recently used entry. A zero-capacity cache stores no entries.
     */
    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }

        Node existing = nodes.get(key);
        if (existing != null) {
            existing.value = value;
            moveToFront(existing);
            return;
        }

        Node node = new Node(key, value);
        nodes.put(key, node);
        addAfterHead(node);

        if (nodes.size() > capacity) {
            Node leastRecentlyUsed = tail.prev;
            removeNode(leastRecentlyUsed);
            nodes.remove(leastRecentlyUsed.key);
        }
    }

    private void moveToFront(Node node) {
        removeNode(node);
        addAfterHead(node);
    }

    private void addAfterHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}
