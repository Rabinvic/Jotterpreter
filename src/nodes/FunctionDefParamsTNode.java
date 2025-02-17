package nodes;
import java.util.ArrayList;

import provided.JottTree;
import provided.SymbolTable;
import provided.Token;
import provided.TokenType;

public class FunctionDefParamsTNode implements JottTree{
    IDNode id;
    TypeNode type;

    public FunctionDefParamsTNode(IDNode id, TypeNode type){
        this.id = id;
        this.type = type;
    }
    
    public static FunctionDefParamsTNode parseFunctionDefParamsTNode(ArrayList<Token> tokens) {
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

        if (type == null) {
            return null;
        }
        
        return new FunctionDefParamsTNode(id, type);
    }

    public String convertToJott() {
        return "," + this.id.convertToJott()+":"+this.type.convertToJott();
    }

    // presumably done
    public boolean validateTree() {
        if(!id.validateTree() || !type.validateTree()) {
            return false;
        }
        if(SymbolTable.getLocalSymTable().containsKey(id.getID())) {
            System.err.println("Semantic Error:\n" + "the variable " + id.getID() + " already exists in this function\n" +
            id.getFilename() + ":" + id.getLineNum());
            return false;
        }
        SymbolTable.addLocalVar(type.convertToJott(), id.getID());
        return true;
    }

    public void execute() {

    }

    public String returnParamName() {
        return id.getID();
    }
}
