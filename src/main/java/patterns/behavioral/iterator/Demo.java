package patterns.behavioral.iterator;

import java.util.Iterator;

class Node<T> {
  public T value;
  public Node<T> left, right, parent;

  public Node(T value) {
    this.value = value;
  }

  public Node(T value, Node<T> left, Node<T> right) {
    this.value = value;
    this.left = left;
    this.right = right;
    left.parent = right.parent = this;
  }
}

class InOrderIterator<T> implements Iterator<T> {
  private Node<T> current, root;
  private boolean yieldedStartingElement;

  public InOrderIterator(Node<T> root) {
    this.root = current = root;
    while (current.left != null) current = current.left;
  }

  private boolean hasRightmostParent(Node<T> node) {
    if (node.parent == null) {
      return false;
    } else {
      return (node == node.parent.left) || hasRightmostParent(node.parent);
    }
  }

  @Override
  public boolean hasNext() {
    return current.left != null || current.right != null || hasRightmostParent(current);
  }

  @Override
  public T next() {
    if (!yieldedStartingElement) {
      yieldedStartingElement = true;
      return current.value;
    }

    if (current.right != null) {
      current = current.right;
      while (current.left != null) {
        current = current.left;
      }
      return current.value;
    } else {
      Node<T> p = current.parent;
      while (p != null && current == p.right) {
        current = p;
        p = p.parent;
      }
      current = p;
      return current.value;
    }
  }
}

// Iterator is just a helper object
// that tells you how to traverse a specific data structure

public class Demo {
  public static void main(String[] args) {}
}
