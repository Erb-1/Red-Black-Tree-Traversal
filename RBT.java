/**
 * RBT
 * Red-Black Tree Insert
 * @author Erblin Berisha (eb523)
 */
import java.util.*;
public class RBT {
    private Node root;

    public RBT() {}

    public boolean isRed(Node x) {
        if (x == null) return false;
        return x.getColor() == Node.Color.RED;
    }
    
    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(int x) {
        return nodeContainsData(root, x);
    }

    private boolean nodeContainsData(Node r, int x) {
        while (r != null) {
            if (r.getData() - x < 0) {
                r = r.getLeft();
            } else if (r.getData() - x > 0) {
                r = r.getRight();
            } else {
                return true;
            }
        }
        return false;
    }

    public List<Integer> serializeTree() {
        return serializeTree(root);
    }

    private List<Integer> serializeTree(Node r) {
        if (r == null) return new LinkedList<>();
        int data = r.getData();
        List<Integer> left = serializeTree(r.getLeft());
        List<Integer> right = serializeTree(r.getRight());
        left.add(data);
        left.addAll(right);
        return left;
    }

    public int maxHeight() {
        return maxHeight(root);
    }

    private int maxHeight(Node r) {
        if (r==null) return 0;        
        return 1 + Math.max(maxHeight(r.getLeft()), maxHeight(r.getRight()));
    }




    // ************************************************************************
    // * INSERT INTO RED-BLACK TREE
    // ************************************************************************

    public void insert(int x) {
        root = nodeInsertData(root, x);
        root.setColor(Node.Color.BLACK);
    }

    private Node nodeInsertData(Node r, int x) {

        //IF null, create a new node with the data and return it; ELSE return the node.
        //new node is red as colouring a new node this way lessens the violations of properties.
        if (r == null) {
            return new Node(x, Node.Color.RED, null, null);
        }
        
        //compare variable to compare the data of the node to the data we want to insert
        int comp = Integer.compare(x,r.getData());

        //INSERTION-------------------------------------------------------------
        /* This is where the insertion happens. We compare the data we want to insert
         * to the data of the node we are currently at. If the data we want to insert
         * is less than the data of the node we are at, we insert it into the left
         * subtree. If the data we want to insert is greater than the data of the
         * node we are at, we insert it into the right subtree. If the data we want
         * to insert is equal to the data of the node we are at, we do nothing as its
         * a duplicate and we don't want duplicates in our tree.
         */

        //IF comp < 0, redo the method with the left child as node r.
        if(comp < 0) {
            r.setLeft(nodeInsertData(r.getLeft(), x));
        }
        //IF comp > 0, redo the method with the right child as node r.
        else if (comp > 0) {
            r.setRight(nodeInsertData(r.getRight(), x));
        }
    

        //BALANCE CHECKS--------------------------------------------------------
        /* This is where we check if the tree is balanced. We check if the right 
         * child is red and the left child is black. If this is true, we rotate
         * the tree to the left. We then check if the left child is red and the
         * left child's left child is red. If this is true, we rotate the tree
         * to the right. We then check if the left child is red and the right
         * child is red. If this is true, we flip the colors of the node etc etc.
         * Incredibly confusing in type...
         */
        //CASE 2: check if both parent red and x is left child on right side
        if(isRed(r.getLeft()) && isRed(r.getRight())&& isRed(r.getRight().getLeft())) {
            flipColors(r);
        }
        //CASE 2: check if both parent red and x is right child on right side.
        if(isRed(r.getLeft()) && isRed(r.getRight())&& isRed(r.getRight().getRight())) {
            flipColors(r);
        }
        //CASE 2: check if both parent red and x is left child is on left side.
        if(isRed(r.getLeft()) && isRed(r.getRight())&& isRed(r.getLeft().getLeft())) {
            flipColors(r);
        }
        //CASE 2: check if both parent red and x is left child is on right side.
        if(isRed(r.getLeft()) && isRed(r.getRight())&& isRed(r.getLeft().getRight())) {
            flipColors(r);
        }

        //CASE 3: RIGHT TRIANGLE
        //checking for triangle on right side of tree
        if(isRed(r.getRight()) && !isRed(r.getLeft())&& isRed(r.getRight().getLeft())) {
            r.setRight(rotateRight(r.getRight()));
        }
        //LEFT TRIANGLE
        if(isRed(r.getLeft()) && !isRed(r.getRight())&& isRed(r.getLeft().getRight())) {
            r.setLeft(rotateLeft(r.getLeft()));
        }

        //CASE 4: LINE
        //checks if uncle is black and a line is formed.
        if(isRed(r.getLeft()) && isRed(r.getLeft().getLeft())) {
            r = rotateRight(r);
            r.setColor(Node.Color.BLACK);
            r.getRight().setColor(Node.Color.RED);
        }
        //LINE
        //Same as above but other side.
        if(isRed(r.getRight()) && isRed(r.getRight().getRight())) {
            r = rotateLeft(r);
            r.setColor(Node.Color.BLACK);
            r.getLeft().setColor(Node.Color.RED);
        }
        //check if left child is red and right child is red
        
        
        return r;
    }


    //TREE HELPERS-----------------------------------------------------------
    //where the magic happens
    //rotations and colour flips to help balance tree. These methods are called in the insert method

    private Node rotateRight(Node h) {
        //check that h is not null and its left child is red
        //set x to be h's left child
        Node x = h.getLeft();
        //set h's left child to be x's right child
        h.setLeft(x.getRight());
        //set x's right child to be h
        x.setRight(h);

        return x;
    }
 
    private Node rotateLeft(Node h) {
        Node x = h.getRight();
        h.setRight(x.getLeft());
        x.setLeft(h);

        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        if(h.getColor() == Node.Color.RED){
            h.setColor(Node.Color.BLACK);
        }
        else if(h.getColor() == Node.Color.BLACK){
            h.setColor(Node.Color.RED);
        }

        if(h.getLeft().getColor() == Node.Color.BLACK){
            h.getLeft().setColor(Node.Color.RED);
        }else if(h.getLeft().getColor() == Node.Color.RED) {
            h.getLeft().setColor(Node.Color.BLACK);
        }

        if(h.getRight().getColor() == Node.Color.BLACK){
            h.getRight().setColor(Node.Color.RED);
        }else if(h.getRight().getColor() == Node.Color.RED) {
            h.getRight().setColor(Node.Color.BLACK);
        }
    }
}
