package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node> nodes;
    private final List<Task> historyOfViewedTasks;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        nodes = new HashMap<>();
        historyOfViewedTasks = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        Node temp = head;
        while (temp != null) {
            historyOfViewedTasks.add(temp.data);
            temp = temp.next;
        }
        return historyOfViewedTasks;
    }

    @Override
    public void add(Task task) {
        if (nodes.containsKey(task.getId())) {
            removeNode(nodes.get(task.getId()));
        }
        linkLast(task);
        nodes.put(task.getId(), tail);
    }

    @Override
    public void remove(Long id) {
        Node node = nodes.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    private void linkLast(Task task) {
        Node node = new Node(task, tail, null);
        if (isEmpty()) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private class Node {
        public Task data;
        public Node prev;
        public Node next;

        public Node(Task data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
