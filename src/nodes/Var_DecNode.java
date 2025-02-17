package nodes;
import java.util.ArrayList;

import provided.*;

public class Var_DecNode implements JottTree {
    TypeNode type;
    IDNode id;
    

    public Var_DecNode(TypeNode type, IDNode id){
        this.type = type;
        this.id = id;
    }

    public static Var_DecNode parseVarDecNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        // < type > < id >;

        // < type >
        TypeNode type = TypeNode.parseTypeNode(tokens);
        

        // < id >
        IDNode id = IDNode.parseIDNode(tokens);
        

        if (type == null) {
            System.err.println("Syntax Error:\n missing type in variable declaration\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        if (id == null){
            System.err.println("Syntax Error:\n missing '';' in variable declaration\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        // ;
        if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) {
            System.err.println("Syntax Error:\n missing '';' in variable declaration\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        return new Var_DecNode(type, id);
    }

    public String convertToJott(){
        // < type >< id >;
        return type.convertToJott() + " " + id.convertToJott() + ";";
    }

    // presumably done
    public boolean validateTree() {
        if(SymbolTable.getLocalSymTable().containsKey(id.getID())) {
            System.err.println("Semantic Error:\n" + "the variable " + id.getID() + " already exists in this function\n" +
            id.getFilename() + ":" + id.getLineNum());
            return false;
        }

        // fixed, previous would try to get declared id from getLocalSymbolTable when it doesn't exist there yet
        String varType = type.convertToJott();
        SymbolTable.addLocalVar(varType, id.getID());
        return true;
    }
    public void execute(){
        SymbolTable.fbodys.get(SymbolTable.currentCalledFunc.peek()).varValues.put(id.getID(), null);
    }
}