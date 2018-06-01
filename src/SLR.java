import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class SLR {

    private static String VT[] = new String[64];
    private static String VN[] = new String[64];

    public static void main (String args[]) throws IOException {
        readFromFile();
        addVTAndVN();
        outputCurrentProduction();
        outputFollowSet();
        ItemSets.generateItemSets();
        ItemSets.buildItemSet(0);
        ItemSets.outputItemSets();
        AnalysisTable.generateTable();
        outputAnalysisTable();
        Analyze.analyze();
    }

    private static void outputFollowSet () {
        for (String vn : Config.VN) {
            System.out.print(vn + ": ");
//            FirstSet.getFirst(vn).outputSet();
            FollowSet.getFollow(vn).outputSet();
        }
    }

    private static void outputCurrentProduction () {
        for (Production production : Config.productions) {
            System.out.println(production.head + "->" + production.body);
        }
    }

    private static void outputAnalysisTable () {
        for (int i = 0; i < Config.analysisTable.size(); i++) {
            AnalysisItem analysisItem = Config.analysisTable.get(i);
            if (Config.VN.contains(analysisItem.actionOrGoto)) {
                System.out.println("状态：" + analysisItem.stateNum + "  GOTO " + "{" + analysisItem.actionOrGoto + ", " + analysisItem.content + "}");
            } else {
                System.out.println("状态：" + analysisItem.stateNum + "  Action " + "{" + analysisItem.actionOrGoto + ", " + analysisItem.content + "}");
            }

        }
    }

    private static void readFromFile() throws IOException {
        int i = 0;
        String line;
        String pathname = "test2.txt";
        File fp = new File(pathname);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fp));
        BufferedReader br = new BufferedReader(reader);
        while ((line = br.readLine()) != null) {
            if(i == 0) {
                VN = line.split(",");
            } else if (i == 1) {
                VT = line.split(",");
            } else {
                Config.productions.add(new Production(line.replace(" ", "")));
            }
            i++;
        }
    }

    private static void addVTAndVN() {
        Collections.addAll(Config.VN, VN);
        Collections.addAll(Config.VT, VT);
    }

}
