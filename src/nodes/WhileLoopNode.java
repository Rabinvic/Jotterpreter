package nodes;
import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public class WhileLoopNode implements Body_StmtNode{
    private ExpressionNode expr;
    private BodyNode body;

    public WhileLoopNode(ExpressionNode expr, BodyNode body){
        this.expr = expr;
        this.body = body;
    }

    public static WhileLoopNode parseWhileLoopNode(ArrayList<Token> tokens){
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        // While [ < expr >]{ < body >}

        // While
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD || !tokens.get(0).getToken().equals("While")) {
            System.err.println("Syntax Error:\n missing ''While' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // [
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            System.err.println("Syntax Error:\n missing ''[' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < expr >
        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);
        if(expr == null) {
            return null;
        }

        // ]
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            System.err.println("Syntax Error:\n missing '']' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // {
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            System.err.println("Syntax Error:\n missing ''{' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < body >
        BodyNode body = BodyNode.parseBodyNode(tokens);
        if(body == null) {
            return null;
        }

        // }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            System.err.println("Syntax Error:\n missing ''}' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        return new WhileLoopNode(expr, body);
    }

    
    public String convertToJott() {
        // While [ < expr >]{ < body >}
        return "While[" + expr.convertToJott() + "]{" + body.convertToJott() + "}";
    }
    public boolean validateTree() {
        return true;
    }
    public void execute() {
        
    }
}
