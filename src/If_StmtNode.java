import java.util.ArrayList;

import provided.Token;

public class If_StmtNode implements Body_StmtNode{

    public static If_StmtNode parseIfStmtNode(ArrayList<Token> tokens) {
        return new If_StmtNode();
    }

    public String convertToJott(){
        return "";
    }
    public boolean validateTree() {
        return true;
    }
    public void execute(){

    }
}
