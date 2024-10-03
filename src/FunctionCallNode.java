import java.util.ArrayList;
import provided.JottTree;
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
            throw new IllegalArgumentException();
        }
        if (tokens.get(0).getTokenType() != TokenType.FC_HEADER) {
            throw new IllegalArgumentException();
        }
        tokens.remove(0);
        IDNode id = IDNode.parseIDNode(tokens);
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            throw new IllegalArgumentException();
        }
        tokens.remove(0);
        ParamsNode params = ParamsNode.parseParamsNode(tokens);
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            throw new IllegalArgumentException();
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
