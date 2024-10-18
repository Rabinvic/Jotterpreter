package nodes;
import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class FunctionDefParamsNode implements JottTree{
    IDNode id;
    TypeNode type;
    ArrayList<FunctionDefParamsTNode> paramTs;

    public FunctionDefParamsNode(IDNode id, TypeNode type, ArrayList<FunctionDefParamsTNode> paramTs){
        this.id = id;
        this.type = type;
        this.paramTs = paramTs;
    }

    public String convertToJott() {
        if (this.id == null) {
            return "";
        }

        String str = "";
        str += this.id.convertToJott();
        str += ":";
        str += this.type.convertToJott();

        if (this.paramTs != null) {
            for(int i = 0; i < paramTs.size(); i++){
                str += paramTs.get(i).convertToJott();
            }
        }
        return str;
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
    
    public static FunctionDefParamsNode parseParamsNode(ArrayList<Token> tokens) {
        //if there are no parameters, empty string
        if(tokens.get(0).getTokenType() == TokenType.R_BRACKET){
            return new FunctionDefParamsNode(null, null,null);
        }
        
        // if only 1 parameter
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
        if (type == null) {
            return null;
        }

        if(tokens.get(0).getTokenType() == TokenType.R_BRACKET){
            return new FunctionDefParamsNode(id, type, null);
        }

        // more than 1 parameter
        ArrayList<FunctionDefParamsTNode> paramTs = new ArrayList<FunctionDefParamsTNode>();

        while (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            FunctionDefParamsTNode param = FunctionDefParamsTNode.parseFunctionDefParamsTNode(tokens);
            if (param == null) {
                return null;
            }
            paramTs.add(param);
        }

        return new FunctionDefParamsNode(id, type, paramTs);
    }

}
