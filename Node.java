public class Node{
    byte symbol;
    byte HTsymbol;
    int HTsymbolLength;
    int count;
    Node left;
    Node right;

    public void Node(byte symbol, int count, Node left, Node right,byte HTsymbol, int HTsymbolLength){
        this.symbol = symbol;
        this.count = count;
        this.left = left;
        this.right = right;
        this.HTsymbol = HTsymbol;
        this.HTsymbolLength = HTsymbolLength;
    }
}