class AnalysisTable {

    static void generateTable () {
        for (ItemSet itemSet : Config.itemSets) {
            for (Item item : itemSet.items) {
                int indexOfI = Config.itemSets.indexOf(itemSet);
                AnalysisItem analysisItem;
                if (item.dotPos != item.body.length()) {
                    ItemSet tmpItemSet = ItemSets.Go(itemSet, String.valueOf(item.body.charAt(item.dotPos)));
                    int indexOfJ = indexOf(tmpItemSet);
                    if (Config.VT.contains(String.valueOf(item.body.charAt(item.dotPos)))) {
                        analysisItem = new AnalysisItem(indexOfI, String.valueOf(item.body.charAt(item.dotPos)), "S" + Integer.toString(indexOfJ));
                        if (!isContainAnalysisItem(analysisItem)) {
                            Config.analysisTable.add(analysisItem);
                        }
                    }
//                    if (Config.VN.contains(String.valueOf(item.body.charAt(item.dotPos)))) {
//                        analysisItem = new AnalysisItem(indexOfI, String.valueOf(item.body.charAt(item.dotPos)), Integer.toString(indexOfJ));
//                        if (!isContainAnalysisItem(analysisItem)) {
//                            Config.analysisTable.add(analysisItem);
//                        }
//                    }
                } else {
                    if (!item.head.equals(Config.productions.get(0).head)
                            && Config.VN.contains(item.head)) {
                        Set set = FollowSet.getFollow(item.head);
                        int indexJ = indexOfProduction(item);
                        for (String follow : set.body) {
                            analysisItem = new AnalysisItem(indexOfI, follow, "r" + Integer.toString(indexJ));
                            if (!isContainAnalysisItem(analysisItem)) {
                                Config.analysisTable.add(analysisItem);
                            }
                        }
                    }
                    if (item.head.equals(Config.productions.get(0).head)
                            && item.body.equals(Config.productions.get(0).body)
                            && item.dotPos == 1) {
                        analysisItem = new AnalysisItem(indexOfI, Config.endSyntax, Config.accept);
                        if (!isContainAnalysisItem(analysisItem)) {
                            Config.analysisTable.add(analysisItem);
                        }
                    }
                }
                for (String vn : Config.VN) {
                    ItemSet tmpItemSet = ItemSets.Go(itemSet, vn);
                    if (tmpItemSet.items.size() > 0) {
                        int indexJ = indexOf(tmpItemSet);
                        analysisItem = new AnalysisItem(indexOfI, vn, Integer.toString(indexJ));
                        if (!isContainAnalysisItem(analysisItem)) {
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
                return true;
            }
        }
        return false;
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
