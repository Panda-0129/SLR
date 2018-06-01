class Production {
    String head;
    String body;

    Production(String str) {
        String tmp[] = str.split("->");
        head = tmp[0];
        body = tmp[1];
    }

    Production(String head, String body) {
        this.head = head;
        this.body = body;
    }

    @Override
    public String toString() {
        return head + "->" + body;
    }
}
