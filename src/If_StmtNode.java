import java.util.ArrayList;

import provided.Token;

public class If_StmtNode implements Body_StmtNode{
    private ExpressionNode expr;
    private BodyNode body;
    private ArrayList<ElseIf_StmtNode> elseIfStmts;
    private Else_StmtNode els;

    public If_StmtNode(ExpressionNode expr, BodyNode body, ArrayList<ElseIf_StmtNode> elseIfStmts, Else_StmtNode els){
        this.expr = expr;
        this.body = body;
        this.elseIfStmts = elseIfStmts;
        this.els = els;
    }

    public static If_StmtNode parseIfStmtNode(ArrayList<Token> tokens) {
        return new If_StmtNode(null,null,null,null);
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
