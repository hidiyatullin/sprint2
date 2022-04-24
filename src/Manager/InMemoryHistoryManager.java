package Manager;

import Model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
        Map<Integer, Node<Task>> history = new HashMap<>();

    Node<Task> first;
    Node<Task> last;

        private static class Node <Task> {
            Task item;
            Node<Task> next;
            Node<Task> prev;

            Node(Node<Task> prev, Task element, Node<Task> next) {
                this.item = element;
                this.next = next;
                this.prev = prev;
            }
        }

    void linkLast(Task task) {
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
        System.out.println(history.values());
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
    public void remove(Task task) {
        final Node<Task> node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        history.remove(task.getId());
    }

    private void removeNode(Node<Task> node) {
            if (node != null) {
                Node nodePrev = node.prev;
                Node nodeNext = node.next;
                if (nodePrev == null) {
                    first = nodeNext;
//                    nodeNext.prev = null;
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
    }
