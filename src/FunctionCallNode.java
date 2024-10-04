import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

public class FunctionCallNode implements OperandNode{
    IDNode name;
    ParamsNode params;

    public FunctionCallNode(IDNode name, ParamsNode params) {
        this.name = name;
        this.params = params;
    }

    public String convertToJott() {
        return "::" + name.convertToJott() + "[" + params.convertToJott() + "]";
    }

    public static FunctionCallNode parseFunctionCallNode(ArrayList<Token> tokens){
        if (tokens.size() == 0) {
            System.err.println("can't parse if no tokens");
            return null;
        }
        if (tokens.get(0).getTokenType() != TokenType.FC_HEADER) {
            System.err.println("missing function header");
            return null;
        }
        tokens.remove(0);
        IDNode id = IDNode.parseIDNode(tokens);
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            System.err.println("missing left bracket");
            return null;
        }
        tokens.remove(0);
        ParamsNode params = ParamsNode.parseParamsNode(tokens);
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            System.err.println("missing right bracket");
            return null;
        }
        tokens.remove(0);
        return new FunctionCallNode(id, params);
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {
        
    }
}
