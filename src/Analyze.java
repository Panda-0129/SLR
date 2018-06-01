import java.util.Scanner;
import java.util.Stack;

class Analyze {

    private static String inputStr;
//    用于存放分析过程的栈
    private static Stack<String> stAnalysis = new Stack<>();
//    用于存放输入串的栈，输入串也可以按算法定义成一个动态数组
    private static Stack<String> stInputStr = new Stack<>();

    static void analyze () {
//        init()主要工作是从键盘获取输入串，初始化分析栈以及输入串栈
        init();
        while ( true ) {
            String s = stAnalysis.peek();
            String a = stInputStr.peek();
//            获取分析表中[s, a]项内容
            String content = AnalysisTable.getContent(Integer.valueOf(s), a);
            /*如果content内容是接收，则说明分析成功
            如果content为空，则说明分析过程出错*/
            if (content.equals(Config.accept)) {
                outputStack(content);
                System.out.println("success");
                return;
            } else if (!content.equals("")) {
//                获取S或者r的stateNum
                int stateNum = Integer.parseInt(content.substring(1, content.length()));

//                移动进栈状态时，根据算法中的步骤执行
                if (content.charAt(0) == 'S') {
                    outputStack(content);
                    stAnalysis.push(a);
                    stAnalysis.push(String.valueOf(stateNum));
                    stInputStr.pop();
                }

//                按文法产生式归约的情况
                else if (content.charAt(0) == 'r') {
                    outputStack(content);
                    for (int i = 0; i < 2 * Config.productions.get(stateNum).body.length(); i++) {
                        stAnalysis.pop();
                    }
                    int tmp = Integer.valueOf(stAnalysis.peek());
                    stAnalysis.push(Config.productions.get(stateNum).head);
                    stAnalysis.push(AnalysisTable.getContent(tmp, Config.productions.get(stateNum).head));
                }
            } else {
                System.out.println("error");
                return;
            }
        }
    }

    private static void init() {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            inputStr = scanner.next();
            System.out.println(inputStr);
        }

        scanner.close();
        int index = inputStr.length();
        while ( index > 0 ) {
            stInputStr.push(String.valueOf(inputStr.charAt(index - 1)));
            index--;
        }
        stAnalysis.push("0");
        System.out.printf("%-32s%-32s%-32s","Stack","Input","Action");
    }

    private static void outputStack(String action) {
        StringBuilder tmpAna = new StringBuilder();
        StringBuilder tmpIn = new StringBuilder();
        for (String aStAnalysis : stAnalysis) {
            tmpAna.append(aStAnalysis);
        }

        System.out.printf("%-32s", tmpAna);

        for (int i = stInputStr.size(); i > 0; i-- ) {
            tmpIn.append(stInputStr.get(i - 1));
        }

        System.out.printf("%-32s", tmpIn);

        System.out.print(action);
        System.out.println();
    }

}
