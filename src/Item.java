public class Item extends Production {

    int dotPos;

    Item(String head, String body, int pos) {
        super(head, body);
        this.dotPos = pos;
    }

    @Override
    public String toString () {
        String front = body.substring(0, dotPos);
        String behind = body.substring(dotPos, body.length());
        return head + "->" + front + "·" + behind;
    }
}
