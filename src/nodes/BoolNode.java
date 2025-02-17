package nodes;
import java.util.ArrayList;

import provided.SymbolTable;
import provided.Token;
import provided.TokenType;

public class BoolNode implements ExpressionNode {
    public Token bool;

    public BoolNode(Token bool) {
        this.bool = bool;
    }

    public static BoolNode parseBool(ArrayList<Token> tokens){
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && 
        (tokens.get(0).getToken().equals("True") || tokens.get(0).getToken().equals("False"))) {
            return new BoolNode(tokens.remove(0));
        } else {
            System.err.println("Syntax Error:\n missing boolean\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
    }

    public String convertToJott() {
        return bool.getToken();
    }

    // this is done
    public boolean validateTree() {
        return true;
    }

    public void execute() {
        SymbolTable.vals.put(this, bool.getToken());
    }

    public String getFilename() {
        return bool.getFilename();
    }
    public int getLineNum() {
        return bool.getLineNum();
    }
}

