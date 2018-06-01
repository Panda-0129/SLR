import java.util.ArrayList;

class ItemSets {

    private static ArrayList<Production> getProductionByVn(String Vn) {
        ArrayList<Production> productions = new ArrayList<>();

        for (Production production : Config.productions) {
            if (production.head.equals(Vn))
                productions.add(production);
        }

        return productions;
    }

    //    构造I0
    static void generateItemSets () {
        ItemSet itemSet = new ItemSet();
        itemSet.add(Config.productions.get(0));

//        *后跟非终结符
        if (Config.VN.contains(itemSet.get(0).body.substring(0, 1))) {
            ArrayList<Production> productions = getProductionByVn(itemSet.get(0).body);
            for (Production production : productions) {
                itemSet.add(production);
                generateItemSetsByProduction(itemSet, production);
            }
        }

        Config.itemSets.add(itemSet);
    }

    private static void generateItemSetsByProduction (ItemSet itemSet, Production production) {
        String ch = String.valueOf(production.body.charAt(0));
        if (Config.VN.contains(ch)) {
            ArrayList<Production> productions = getProductionByVn(ch);
            for (Production production1 : productions) {
                itemSet.add(production1);
            }
        }
    }

    static ItemSet Go (ItemSet itemSet, String X) {
        ItemSet result = new ItemSet();
        ArrayList<String> tmpVN = new ArrayList<>();
        ItemSet tmpResult = new ItemSet();
        while ( tmpResult.items.size() > 0 ) {
            tmpResult = new ItemSet();

        }
        for (Item item : itemSet.items) {
            Item tmp;
            if (item.dotPos != item.body.length()
                    && String.valueOf(item.body.charAt(item.dotPos)).equals(X)) {
                tmp = new Item(item.head, item.body, item.dotPos + 1);
                result.add(tmp);
                if (tmp.dotPos != tmp.body.length()) {
                    if (Config.VN.contains(String.valueOf(tmp.body.charAt(tmp.dotPos)))
                            && !tmpVN.contains(String.valueOf(tmp.body.charAt(tmp.dotPos)))) {
                        ArrayList<Production> productions = getProductionByVn(String.valueOf(tmp.body.charAt(tmp.dotPos)));
                        for (Production production : productions) {
                            result.add(production);
                            generateItemSetsByProduction(result, production);
                            tmpVN.add(production.head);
                        }
                    }
                }
            }
        }

        ItemSet moreVn = new ItemSet();
        for (Item item : result.items) {
            if (item.dotPos != item.body.length()
                    && Config.VN.contains(String.valueOf(item.body.charAt(item.dotPos)))) {
                ArrayList<Production> productions = getProductionByVn(String.valueOf(item.body.charAt(item.dotPos)));
                for (Production production : productions) {
                    moreVn.add(production);
                    generateItemSetsByProduction(moreVn, production);
                    tmpVN.add(production.head);

                }
            }
        }
        for (Item item : moreVn.items) {
            if (!isContainItem(item, result))
                result.add(item);
        }
//        if (result.items.size() > 0 && !contains(result))
//            Config.itemSets.add(result);

        return result;

    }

    //    index为项目集下标
    static void buildItemSet (int index) {
        ItemSet beginItemSet = Config.itemSets.get(index);

        buildItemSetElement(beginItemSet);

        if (index < Config.itemSets.size() - 1) {
            buildItemSet(index + 1);
        }

    }

    private static void buildItemSetElement (ItemSet itemSet) {
        for (String vn : Config.VN) {
            ItemSet result = Go(itemSet, vn);
            if (result.items.size() > 0 && !contains(result))
                Config.itemSets.add(result);
        }

        for (String vt : Config.VT) {
            ItemSet result = Go(itemSet, vt);
            if (result.items.size() > 0 && !contains(result))
                Config.itemSets.add(result);
        }
    }

    private static boolean contains (ItemSet itemSet) {
        boolean flag = true;
        for (ItemSet itemSet1 : Config.itemSets) {
            if (itemSet.items.size() != itemSet1.items.size()) {
                flag = false;
            } else {
                boolean tmp = true;
                for (int i = 0; i < Math.min(itemSet1.items.size(), itemSet.items.size()); i++) {
                    if (!(itemSet.items.get(i).head.equals(itemSet1.items.get(i).head)
                            && itemSet.items.get(i).body.equals(itemSet1.items.get(i).body)
                            && itemSet.items.get(i).dotPos == itemSet1.items.get(i).dotPos)) {
                        tmp = false;
                    }
                }
                if (tmp) {
                    flag = true;
                    break;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }

    static void outputItemSets () {
        System.out.println("项目集规范族：");
        int num = 0;
        for (ItemSet itemSet : Config.itemSets) {
            System.out.println("I" + num + ": ");
            num++;
            for (Item item : itemSet.items) {
                System.out.println(item.toString());
            }
        }
    }

    static boolean isContainItem (Item item, ItemSet itemSet) {
        for (Item tmp : itemSet.items) {
            if (item.head.equals(tmp.head)
                    && item.body.equals(tmp.body)
                    && item.dotPos == tmp.dotPos)
                return true;
        }
        return false;
    }
}
