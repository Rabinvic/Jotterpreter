import java.util.ArrayList;

import provided.Token;

public class Else_StmtNode implements Body_StmtNode{
    private BodyNode body;

    public Else_StmtNode(ExpressionNode expr, BodyNode body){
        this.body = body;
    }

    public static ElseIf_StmtNode parseElseIfStmtNode(ArrayList<Token> tokens) {
        return new ElseIf_StmtNode(null, null);
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