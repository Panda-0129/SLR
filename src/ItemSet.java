import java.util.ArrayList;

class ItemSet {
    ArrayList<Item> items = new ArrayList<>();

    Item get(int i) {
        return items.get(i);
    }

    void add(Item item) {
        items.add(item);
    }

    void add(Production production) {
        Item item = new Item(production.head, production.body, 0);
        if (contains(item)) {
            return;
        }
        items.add(item);
    }

    private boolean contains(Item item) {
        for (Item tmpItem : items) {
            if (tmpItem.head.equals(item.head)
                    && tmpItem.body.equals(item.body) &&
                    tmpItem.dotPos == item.dotPos)
                return true;
        }

        return false;
    }
}
