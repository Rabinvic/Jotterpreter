package nodes;
import java.util.ArrayList;
import provided.JottTree;

import provided.Token;
import provided.TokenType;

public class ParamsTNode implements JottTree{
    ExpressionNode expr;

    public ParamsTNode(ExpressionNode expr){
        this.expr = expr;
    }

    public String convertToJott() {
        return "," + this.expr.convertToJott();
    }

    public boolean validateTree() {
        return true;
    }

    public ExpressionNode paramsTHelper() {
        return expr;
    }

    public void execute() {

    }
    
    public static ParamsTNode parseParamsTNode(ArrayList<Token> tokens) {
        if(tokens.get(0).getTokenType() != TokenType.COMMA){
            System.err.println("Syntax Error:\n missing comma\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        tokens.remove(0);
        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);

        if (expr == null) {
            return null;
        }
        
        return new ParamsTNode(expr);
    }

}
