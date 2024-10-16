import java.util.ArrayList;

import provided.*;

public class TypeNode implements JottTree {
    public Token type;

    public TypeNode(Token type) {
        this.type = type;
    }

    public static TypeNode parseTypeNode(ArrayList<Token> tokens){
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD &&
        (tokens.get(0).getToken() == "Double" || tokens.get(0).getToken() == "Integer" ||
        tokens.get(0).getToken() == "String" || tokens.get(0).getToken() == "Boolean")) {
            return new TypeNode(tokens.remove(0));
        } else {
            System.err.println("Syntax Error:\n missing type\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
    }

    public String convertToJott() {
        return type.getToken();
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
}
