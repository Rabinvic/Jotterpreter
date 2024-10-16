import java.util.ArrayList;

import provided.*;

public class ElseIfNode implements Body_StmtNode{
    private ExpressionNode expr;
    private BodyNode body;

    public ElseIfNode(ExpressionNode expr, BodyNode body){
        this.expr = expr;
        this.body = body;
    }

    public static ElseIfNode parseElseIfNode(ArrayList<Token> tokens) {
        return new ElseIfNode(null, null);
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