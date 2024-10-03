import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class IDNode implements OperandNode{
    private Token name;

    public IDNode(Token name) {
        this.name = name;
    }

    public String convertToJott() {
        return name.getToken();
    }

    public static IDNode parseIDNode(ArrayList<Token> tokens) {
        if (tokens.size() == 0) {
            throw new IllegalArgumentException();
        }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            throw new IllegalArgumentException();
        }
        if (Character.isUpperCase(tokens.get(0).getToken().charAt(0))) {
            throw new IllegalArgumentException();
        }
        return new IDNode(tokens.remove(0));
    }

    public boolean validateTree() {
        return true;
    }

    public void execute(){

    }

}
