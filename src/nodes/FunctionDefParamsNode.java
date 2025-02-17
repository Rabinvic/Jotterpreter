package nodes;
import java.util.ArrayList;

import provided.JottTree;
import provided.SymbolTable;
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

    public void execute() {

    }

    public ArrayList<String> returnParamNames() {
        if(type == null && id == null && paramTs == null) {
            return null;
        }
        ArrayList<String> paramNames = new ArrayList<String>();
        paramNames.add(id.getID());
        if(paramTs == null) {
            return paramNames;
        }
        for (FunctionDefParamsTNode defParam : paramTs) {
            paramNames.add(defParam.returnParamName());
        }
        return paramNames;
    }
    
    public static FunctionDefParamsNode parseParamsNode(ArrayList<Token> tokens) {
        // < id >:< type >< function_def_params_t >⋆ | ε
        
        //if there are no parameters, empty string
        if(tokens.get(0).getTokenType() == TokenType.R_BRACKET){
            return new FunctionDefParamsNode(null, null,null);
        }
        
        // if only 1 parameter
        // < id >
        IDNode id = IDNode.parseIDNode(tokens);
        if (id == null) {
            return null;
        }

        // :
        if(tokens.get(0).getTokenType() != TokenType.COLON){
            System.err.println("Syntax Error:\n missing colon\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < type >
        TypeNode type = TypeNode.parseTypeNode(tokens);
        if (type == null) {
            return null;
        }

        // shortcut if only one parameter
        if(tokens.get(0).getTokenType() == TokenType.R_BRACKET){
            return new FunctionDefParamsNode(id, type, null);
        }

        // more than 1 parameter
        // < function_def_params_t >⋆
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

    // presumably done
    public boolean validateTree() {
        if(id == null) {
            return true;
        }
        if(!id.validateTree() || !type.validateTree()) {
            return false;
        }
        SymbolTable.addLocalVar(type.convertToJott(), id.getID());
        if (this.paramTs == null){
            return true;
        }
        for(FunctionDefParamsTNode fdpt: paramTs) {
            if(!fdpt.validateTree()) {
                return false;
            }
        }
        return true;
    }
}
