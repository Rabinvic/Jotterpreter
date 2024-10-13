import java.util.ArrayList;

import provided.Token;

public class WhileLoopNode implements Body_StmtNode{

    public static WhileLoopNode parseWhileLoopNode(ArrayList<Token> tokens){
        return new WhileLoopNode();
    }
    public String convertToJott() {
        return "";
    }
    public boolean validateTree() {
        return true;
    }
    public void execute() {
        
    }
}
