package nodes;
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

    public ArrayList<ExpressionNode> paramsHelper() {
        ArrayList<ExpressionNode> params = new ArrayList<ExpressionNode>();
        if (expr == null){
            return null;
        }
        if(paramTs == null) {
            params.add(expr);
            return params;
        }
        for(ParamsTNode param: paramTs) {
            params.add(param.paramsTHelper());
        }
        return params;
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
        if(expr == null) {
            return true;
        }
        if(!expr.validateTree()) {
            return false;
        }
        if(paramTs == null) {
            return true;
        }
        for(ParamsTNode param: paramTs) {
            if(!param.validateTree()) {
                return false;
            }
        }
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

        while (!tokens.isEmpty() && tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            ParamsTNode param = ParamsTNode.parseParamsTNode(tokens);
            if (param == null) {
                return null;
            }
            paramTs.add(param);
        }

        return new ParamsNode(expr, paramTs);
    }

}
