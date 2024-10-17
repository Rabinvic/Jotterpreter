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
        if(type == null) {
            return null;
        }

        // < id >
        IDNode id = IDNode.parseIDNode(tokens);
        if(id == null) {
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
    public boolean validateTree() {
        return true;
    }
    public void execute(){

    }
}