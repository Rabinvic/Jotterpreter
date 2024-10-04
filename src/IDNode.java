import java.util.ArrayList;

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
            System.err.printf("can't parse IDNode because no tokens left");
            return null;
        }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            System.err.printf("can't parse non id token as id node");
            return null;
        }
        if (Character.isUpperCase(tokens.get(0).getToken().charAt(0))) {
            System.err.printf("can't parse keyword as id node");
            return null;
        }
        return new IDNode(tokens.remove(0));
    }

    public boolean validateTree() {
        return true;
    }

    public void execute(){

    }

}
