package nodes;
import java.util.ArrayList;

import provided.*;

public class If_StmtNode implements Body_StmtNode{
    private ExpressionNode expr;
    private BodyNode body;
    private ArrayList<ElseIfNode> elseIfs;
    private ElseNode els;

    public If_StmtNode(ExpressionNode expr, BodyNode body, ArrayList<ElseIfNode> elseIfs, ElseNode els){
        this.expr = expr;
        this.body = body;
        this.elseIfs = elseIfs;
        this.els = els;
    }

    public static If_StmtNode parseIfStmtNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        // If[< expr >]{< body >}< elseif_lst >*< else >

        // If
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD || !tokens.get(0).getToken().equals("If")) {
            System.err.println("Syntax Error:\n missing ''If'\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // [
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            System.err.println("Syntax Error:\n missing ''[' in If Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < expr >
        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);
        if (expr == null) {
            return null;
        }

        // ]
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            System.err.println("Syntax Error:\n missing '']' in If Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // {
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            System.err.println("Syntax Error:\n missing ''{' in If Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < body >
        BodyNode body = BodyNode.parseBodyNode(tokens);
        if (body == null) {
            return null;
        }

        // }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            System.err.println("Syntax Error:\n missing ''}' in If Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < elseif_lst >
        ArrayList<ElseIfNode> elseIfs = new ArrayList<ElseIfNode>();
        while(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken().equals("Elseif")) {
            System.err.println("ElseIf found!!!");
            ElseIfNode elseIf = ElseIfNode.parseElseIfNode(tokens);
            if(elseIf == null) {
                return null;
            }
            elseIfs.add(elseIf);
        }

        // < else >
        ElseNode els = ElseNode.parseElseNode(tokens);

        return new If_StmtNode(expr, body, elseIfs, els);
    }

    public String convertToJott(){
        // If[< expr >]{< body >}< elseif_lst >*< else >

        String elseIfList = "";
        for(ElseIfNode elseIf : elseIfs) {
            elseIfList += elseIf.convertToJott();
        }
        return "If[" + expr.convertToJott() + "]{" + body.convertToJott() + "}" + elseIfList + els.convertToJott();
    }
    public boolean validateTree() {
        return true;
    }
    public void execute(){

    }
}
