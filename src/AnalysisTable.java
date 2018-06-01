class AnalysisTable {

    static void generateTable () {
//        遍历项目集规范族中的每个项目
        for (ItemSet itemSet : Config.itemSets) {
            for (Item item : itemSet.items) {
//                获取该项目集在项目集规范族中的位置，即Ii的下标i
                int indexOfI = Config.itemSets.indexOf(itemSet);
                AnalysisItem analysisItem;
//                该项目是否为A->α·形式
                if (item.dotPos != item.body.length()) {
//                    获取Ij = Go(I, X)
                    ItemSet tmpItemSet = ItemSets.Go(itemSet, String.valueOf(item.body.charAt(item.dotPos)));
//                    获取下标j
                    int indexOfJ = indexOf(tmpItemSet);
//                    若满足A->α·aß，且a为终结符，则置action[i, a]为Sj
                    if (Config.VT.contains(String.valueOf(item.body.charAt(item.dotPos)))) {
                        analysisItem = new AnalysisItem(indexOfI, String.valueOf(item.body.charAt(item.dotPos)), "S" + Integer.toString(indexOfJ));
                        if (isContainAnalysisItem(analysisItem)) {
                            Config.analysisTable.add(analysisItem);
                        }
                    }
                } else {
//                    满足A->α·，且α不为起始符号，则对FOLLOW(A)中所有的a，
//                    置action[i, a]为rj，j为A->α在文法中的位置
                    if (!item.head.equals(Config.productions.get(0).head)
                            && Config.VN.contains(item.head)) {
                        Set set = FollowSet.getFollow(item.head);
                        int indexJ = indexOfProduction(item);
                        for (String follow : set.body) {
                            analysisItem = new AnalysisItem(indexOfI, follow, "r" + Integer.toString(indexJ));
                            if (isContainAnalysisItem(analysisItem)) {
                                Config.analysisTable.add(analysisItem);
                            }
                        }
                    }
//                    对s->S·，置action[i, #]为接受
                    if (item.head.equals(Config.productions.get(0).head)
                            && item.body.equals(Config.productions.get(0).body)
                            && item.dotPos == 1) {
                        analysisItem = new AnalysisItem(indexOfI, Config.endSyntax, Config.accept);
                        if (isContainAnalysisItem(analysisItem)) {
                            Config.analysisTable.add(analysisItem);
                        }
                    }
                }
//                对所有的非终结符，使用下面的规则构造状态i的goto函数：
//                如果Go(Ii, A) = Ij，则goto[i, A] = j。
                for (String vn : Config.VN) {
                    ItemSet tmpItemSet = ItemSets.Go(itemSet, vn);
                    if (tmpItemSet.items.size() > 0) {
                        int indexJ = indexOf(tmpItemSet);
                        analysisItem = new AnalysisItem(indexOfI, vn, Integer.toString(indexJ));
                        if (isContainAnalysisItem(analysisItem)) {
                            Config.analysisTable.add(analysisItem);
                        }
                    }
                }
            }
        }
    }

    private static boolean isContainAnalysisItem (AnalysisItem analysisItem) {
        for (AnalysisItem tmp : Config.analysisTable) {
            if (tmp.actionOrGoto.equals(analysisItem.actionOrGoto)
                    && tmp.content.equals(analysisItem.content)
                    && tmp.stateNum == analysisItem.stateNum) {
                return false;
            }
        }
        return true;
    }

    private static int indexOf (ItemSet itemSet) {
        int index = -1;
        for (int i = 0; i < Config.itemSets.size(); i++) {
            ItemSet tmp = Config.itemSets.get(i);
            boolean flag = true;

            for (int j = 0; j < Math.min(tmp.items.size(), itemSet.items.size()); j++) {
                if (!(itemSet.items.get(j).head.equals(tmp.items.get(j).head)
                        && itemSet.items.get(j).body.equals(tmp.items.get(j).body)
                        && itemSet.items.get(j).dotPos == tmp.items.get(j).dotPos)) {
                    flag = false;
                }
            }
            if (flag) {
                index = i;
                break;
            }
        }
        return index;
    }

    private static int indexOfProduction (Item item) {
        int index = -1;
        for (int i = 0; i < Config.productions.size(); i++) {
            Production production = Config.productions.get(i);
            if (production.head.equals(item.head)
                    && production.body.equals(item.body)) {
                index = i;
                break;
            }
        }
        return index;
    }

    static String getContent(int stateNum, String actionOrGo) {
        for (AnalysisItem analysisItem : Config.analysisTable) {
            if (analysisItem.stateNum == stateNum
                    && analysisItem.actionOrGoto.equals(actionOrGo)) {
                return analysisItem.content;
            }
        }
        return "";
    }
}
