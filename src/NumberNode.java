import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public class NumberNode implements OperandNode{
    private Token name;

    public NumberNode(Token name) {
        this.name = name;
    }

    public static NumberNode parseNumberNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.printf("can't parse number node because no tokens left");
            return null;
        }
        if(tokens.get(0).getTokenType() != TokenType.NUMBER) {
            System.err.printf("can't parse number node if no number");
            return null;
        }
        return new NumberNode(tokens.remove(0));
    }

    public String convertToJott() {
        return name.getToken();
    }

    public boolean validateTree(){
        return true;
    }

    public void execute() {

    }
}
