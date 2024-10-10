import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class ParamsNode implements JottTree{
    ExpressionNode expr;
    ArrayList<ParamsTNode> paramTs;

    public ParamsNode(ExpressionNode expr, ArrayList<ParamsTNode> paramTs){
        this.expr = expr;
        this.paramTs = paramTs;
    }

    public String convertToJott() {
        if (this.expr == null) {
            return "";
        } else if (this.paramTs == null){
            return expr.convertToJott();
        }
        String str = "";
        str += expr.convertToJott();
        for(int i = 0; i < paramTs.size(); i++){
            str += paramTs.get(i).convertToJott();
        }
        return str;
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
    
    public static ParamsNode parseParamsNode(ArrayList<Token> tokens) {
        //if there are no parameters, empty string
        if(tokens.get(0).getTokenType() == TokenType.R_BRACKET){
            return new ParamsNode(null, null);
        }
        
        // if only 1 parameter
        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);
        if (expr == null) {
            return null;
        }

        if(tokens.get(0).getTokenType() == TokenType.R_BRACKET){
            return new ParamsNode(expr, null);
        }

        // more than 1 parameter
        ArrayList<ParamsTNode> paramTs = new ArrayList<ParamsTNode>();

        while (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            ParamsTNode param = ParamsTNode.parseParamsNode(tokens);
            if (param == null) {
                return null;
            }
            paramTs.add(param);
        }

        return new ParamsNode(expr, paramTs);
    }

}
