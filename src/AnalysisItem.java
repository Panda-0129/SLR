class AnalysisItem {
    int stateNum;
    String actionOrGoto;
    String content;

    AnalysisItem(int stateNum, String actionOrGoto, String content) {
        this.stateNum = stateNum;
        this.actionOrGoto = actionOrGoto;
        this.content = content;
    }
}
