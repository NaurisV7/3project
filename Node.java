public class Node{
    byte symbol;
    int count;
    Node left;
    Node right;
    byte HTsymbol;
    int HTsymbolLength;

    public void Node(byte symbol, int count, byte HTsymbol, int HTsymbolLength){
        this.symbol = symbol;
        this.count = count;
        this.left = null;
        this.right = null;
        this.HTsymbol = HTsymbol;
        this.HTsymbolLength = HTsymbolLength;
    }
}