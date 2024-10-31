package nodes;
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
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() != TokenType.NUMBER) {
            System.err.println("Syntax Error:\n missing number\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
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
    public Boolean isInteger(){
        if(Integer.parseInt(name.getToken()) % 1 == 0) {
            return true;
        }
        return false;
    }
}
