import java.util.ArrayList;
import provided.JottTree;

import provided.Token;
import provided.TokenType;

public class FunctionDefParamsT implements JottTree{
    IDNode id;
    TypeNode type;

    public FunctionDefParamsT(IDNode id, TypeNode type){
        this.id = id;
        this.type = type;
    }

    public String convertToJott() {
        return "," + this.id.convertToJott()+":"+this.type.convertToJott();
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
    
    public static FunctionDefParamsT parseParamsNode(ArrayList<Token> tokens) {
        if(tokens.get(0).getTokenType() != TokenType.COMMA){
            System.err.println("Syntax Error:\n missing comma\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        tokens.remove(0);
        IDNode id = IDNode.parseIDNode(tokens);

        if (id == null) {
            return null;
        }

        if(tokens.get(0).getTokenType() != TokenType.COLON){
            System.err.println("Syntax Error:\n missing colon\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        tokens.remove(0);
        TypeNode type = TypeNode.parseTypeNode(tokens);

        if (id == null) {
            return null;
        }
        
        return new FunctionDefParamsT(id, type);
    }

}
