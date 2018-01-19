package tree;

import java.util.ArrayList;

/**
 *
 * @author lucas.burdell
 */
public class Tree<T> {

    private Node<T> root;

    public Tree(T rootData) {
        this.root = new Node<>(rootData);
    }

    /**
     * @return the root
     */
    public Node<T> getRoot() {
        return root;
    }

    public static class Node<T> {
        private T value;
        private Node<T> parent;
        private ArrayList<Node<T>> children;

        public Node() {
            this(null, null);
        }

        public Node(T value) {
            this(value, null);
        }

        public Node(T value, Node<T> parent) {
            this.value = value;
            this.parent = parent;
            this.children = new ArrayList<>();
        }

        /**
         * @return the value
         */
        public T getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(T value) {
            this.value = value;
        }

        /**
         * @return the parent
         */
        public Node<T> getParent() {
            return parent;
        }

        /**
         * @param parent the parent to set
         */
        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        /**
         * @return the children
         */
        public ArrayList<Node<T>> getChildren() {
            return children;
        }


    }
}
