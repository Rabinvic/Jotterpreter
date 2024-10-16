import java.util.ArrayList;

import provided.*;

public class ElseNode implements Body_StmtNode{
    private BodyNode body;

    public ElseNode(ExpressionNode expr, BodyNode body){
        this.body = body;
    }

    public static ElseNode parseElseNode(ArrayList<Token> tokens) {
        return new ElseNode(null, null);
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