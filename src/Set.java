import java.util.ArrayList;

class Set {
    String head = "";

    ArrayList<String> body;

    Set() {
        body = new ArrayList<>();
    }

    Set(String str) {
        head = str;
        body = new ArrayList<>();
    }

    void addSet(String str) {
        if(!body.contains(str)) {
            body.add(str);
        }
    }

    private ArrayList<String> getSet() {
        return body;
    }

    void addSet(Set set) {
        for (int i = 0; i < set.getSet().size(); i++) {
            addSet(set.getSet().get(i));
        }
    }

    void remove(String str) {
        body.remove(str);
    }

    void outputSet() {
        System.out.print("{");
        for (int i = 0; i < body.size(); i++) {
            System.out.print(body.get(i));
            if(i != body.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }

    boolean contains(String str) {

        return body.contains(str);
    }
}
