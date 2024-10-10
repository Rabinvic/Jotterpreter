import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

public class FunctionCallNode implements OperandNode{
    IDNode name;
    ParamsTNode params;

    public FunctionCallNode(IDNode name, ParamsTNode params) {
        this.name = name;
        this.params = params;
    }

    public String convertToJott() {
        return "::" + name.convertToJott() + "[" + params.convertToJott() + "]";
    }

    public static FunctionCallNode parseFunctionCallNode(ArrayList<Token> tokens){
        if (tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if (tokens.get(0).getTokenType() != TokenType.FC_HEADER) {
            System.err.println("Syntax Error:\n missing function header\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);
        IDNode id = IDNode.parseIDNode(tokens);
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            System.err.println("Syntax Error:\n missing left bracket\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);
        ParamsTNode params = ParamsTNode.parseParamsNode(tokens);
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            System.err.println("Syntax Error:\n missing right bracket\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
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
