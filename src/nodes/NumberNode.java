package nodes;
import java.util.ArrayList;

import provided.SymbolTable;
import provided.Token;
import provided.TokenType;

public class NumberNode implements OperandNode{
    private Token value;

    public NumberNode(Token value) {
        this.value = value;
    }
    public String getFilename() {
        return value.getFilename();
    }

    public int getLineNum() {
        return value.getLineNum();
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
        return value.getToken();
    }

    // this is done
    public boolean validateTree(){
        return true;
    }

    public void execute() {
        SymbolTable.vals.put(this, value.getToken());
    }

    public Boolean isInteger(){
        if(value.getToken().contains(".")) {
            return false;
        }
        return true;
    }
}
