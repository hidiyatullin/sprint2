package Manager;

import Model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node<Task>> history = new HashMap<>();

    private Node<Task> first;
    private Node<Task> last;

    private static class Node<Task> {
        private Task item;
        private Node<Task> next;
        private Node<Task> prev;

        Node(Node<Task> prev, Task element, Node<Task> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private void linkLast(Task task) {
        final Node<Task> last = this.last;
        final Node<Task> newNode = new Node<>(last, task, null);
        this.last = newNode;
        if (last == null)
            first = newNode;
        else
            last.next = newNode;
        history.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> list = new ArrayList<>();
        Node<Task> node = first;
        while (node != null) {
            list.add(node.item);
            node = node.next;
        }
        return list;
    }

    @Override
    public void add(Task task) {
        final Node<Task> node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        final Node<Task> node = history.get(id);
        if (node != null) {
            removeNode(node);
        }
        history.remove(id);
    }

    private void removeNode(Node<Task> node) {
        Node nodePrev = node.prev;
        Node nodeNext = node.next;
        if ((nodeNext == null) && (nodePrev == null)) {
            first = null;
            last = null;
        } else if (nodePrev == null) {
            first = nodeNext;
        } else if (nodeNext == null) {
            last = nodePrev;
            last.next = null;
        } else {
            nodeNext.prev = nodePrev;
            nodePrev.next = nodeNext;
            node.next = null;
            node.prev = null;
        }
    }
}
