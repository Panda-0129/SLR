import java.util.ArrayList;

class FollowSet {

    static Set getFollow(String str) {
        ArrayList<String> follow = new ArrayList<>();
        Set set = new Set(str);

//        str是终结符
        if (Config.VT.contains(str)) {
            return set;
        } else {
            follow.add(str);
        }

//        将#加入FOLLOW(文法开始符号)
        if (Config.productions.get(0).head.equals(str)) {
            set.addSet(Config.endSyntax);
        }

        for (Production production : Config.productions) {
            if(production.body.contains(str)) {
//                获取该产生式中包含当前str的产生式
                String contain = production.body;
                int index = contain.indexOf(str);
//                    对A->αB(且ε∈FIRST(β))
//                    则FOLLOW(A)中的全部元素加入FOLLOW(B)
                if (index == contain.length() - 1) {
                    if (!follow.contains(production.head)) {
                        set.addSet(getFollow(production.head));
                    }
                } else {
//                        获取当前str的下一个字符
                    String nextCh = String.valueOf(contain.charAt(index + 1));
//                        终结符，则直接加入Follow集中
                    if (Config.VT.contains(nextCh)) {
                        set.addSet(nextCh);
                    }
//                        非终结符
                    else {
//                            获取该字符的First集
                        Set subSet = FirstSet.getFirst(nextCh);
                        if (subSet != null) {
                            if (subSet.contains(Config.nullstr) &&
                                    !follow.contains(subSet.head)) {
                                set.addSet(getFollow(subSet.head));
                            }

                            subSet.remove(Config.nullstr);
                            set.addSet(subSet);

                        }
                    }
                }
            }
        }
        return set;
    }
}
