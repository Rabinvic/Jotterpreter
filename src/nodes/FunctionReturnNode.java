package nodes;
import java.util.ArrayList;

import provided.*;

public class FunctionReturnNode implements JottTree {
    private TypeNode type;
    private boolean isVoid;

    public FunctionReturnNode(TypeNode type, boolean isVoid){
        this.type = type;
        this.isVoid = isVoid;
    }

    public static FunctionReturnNode parseFunctionReturnNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        // < type > | Void
        if(tokens.get(0).getToken().equals("Void")) {
            tokens.remove(0);
            return new FunctionReturnNode(null, true);
        } else {
            TypeNode type = TypeNode.parseTypeNode(tokens);
            if (type == null) {
                return null;
            }
            return new FunctionReturnNode(type, false);
        }
    }

    public String convertToJott(){
        // < type > | Void
        if(isVoid) {
            return "Void";
        } else {
            return type.convertToJott();
        } 
    }
    public boolean validateTree() {
        return true;
    }
    public void execute(){

    }
}