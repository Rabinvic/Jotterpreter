import java.util.ArrayList;

import provided.Token;

public class ElseIf_StmtNode implements Body_StmtNode{
    private ExpressionNode expr;
    private BodyNode body;

    public ElseIf_StmtNode(ExpressionNode expr, BodyNode body){
        this.expr = expr;
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