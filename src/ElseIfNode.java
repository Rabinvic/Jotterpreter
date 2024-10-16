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
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        // Elseif[< expr >]{< body >}

        // Elseif
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD || tokens.get(0).getToken() != "Elseif") {
            System.err.println("Syntax Error:\n missing ''Elseif'\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // [
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            System.err.println("Syntax Error:\n missing ''[' in Elseif\n" + tokens.get(0).getFilename() + ":" + 
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
            System.err.println("Syntax Error:\n missing '']' in Elseif\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        // {
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            System.err.println("Syntax Error:\n missing ''{' in Elseif\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        // < body >
        BodyNode body = BodyNode.parseBodyNode(tokens);
        if (body == null) {
            return null;
        }

        // }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            System.err.println("Syntax Error:\n missing ''}' in Elseif\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        return new ElseIfNode(expr, body);
    }

    public String convertToJott(){
        // Elseif[< expr >]{< body >}
        return "Elseif[" + expr.convertToJott() + "]{" + body.convertToJott() + "}";
    }
    public boolean validateTree() {
        return true;
    }
    public void execute(){

    }
}