class FirstSet {

    static Set getFirst(String str) {
        Set set = new Set(str);
//        如果X∈VT，则FIRST(X)={X}
        if(Config.VT.contains(str)) {
            set.addSet(str);
            return set;
        }
//        X∈VN
        for(Production production: Config.productions) {
//            当前str与产生式匹配
            if(production.head.equals(str)) {
//                    获取当前str对应第一个产生式的第一个字符
                String firstCh = String.valueOf(production.body.charAt(0));
//                    当前字符为终结字符
                if(Config.VT.contains(firstCh)) {
                    set.addSet(firstCh);
                }
//                    当前字符为空串（~）且该产生式长度为1
                else if (firstCh.equals(Config.nullstr) && production.body.length() == 1) {
                    set.addSet(production.body);
                }
//                    求该非终结符的first集并加入当前str的first集中
                else {
                    set.addSet(getFirstFromN(production.body));
                }
            }
        }
        return set;
    }

    private static Set getFirstFromN(String str) {
        boolean[] isEnd = new boolean[str.length()];
        Set set = new Set();
        for (int i = 0; i < str.length(); i++) {
            String firstCh = String.valueOf(str.charAt(i));
//          如果是终结符，则直接加入first集
            if(Config.VT.contains(firstCh)) {
                set.addSet(firstCh);
                return set;
            }

//          不是终结符，继续获取该非终结符的first集
            Set childSet = getFirst(firstCh);
/*              若Y1、Y2、……Yi-1∈Vn(2≤i≤k)
                且ε∈FIRST(Y j )
                (1≤j≤i-1)
                则把FIRST(Yi)-{ε}元素加入FIRST(x)中
 */
            if (childSet != null) {
//              当前非终结符的first集不含空串，则添加到first集中
                if(!childSet.contains(Config.nullstr)) {
                    isEnd[i] = false;
                    set.addSet(childSet);
                    return set;
                }
//              含空串，则将该first集中的空串移除
                else {
                    isEnd[i] = true;
                    childSet.remove(Config.nullstr);
                    set.addSet(childSet);
                }
            }
        }
/*
                若Y1、Y2、……Yk∈Vn
                且ε∈FIRST(Yj)
                则把ε元素加入FIRST(x)中
 */
        for (boolean anIsEnd : isEnd) {
            if (anIsEnd) {
                set.addSet(Config.nullstr);
            }
        }

        return set;
    }

}
