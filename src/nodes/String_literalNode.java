package nodes;
import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public class String_literalNode  implements ExpressionNode{
    private Token strLit;

    public String_literalNode(Token strLit) {
        this.strLit = strLit;
    }

    public String getFilename() {
        return strLit.getFilename();
    }

    public int getLineNum() {
        return strLit.getLineNum();
    }

    public static String_literalNode parseString_literalNode(ArrayList<Token> tokens){
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() != TokenType.STRING){
            System.err.println("Syntax Error:\n missing string literal\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        return new String_literalNode(tokens.remove(0));
    }

    public String convertToJott() {
        return strLit.getToken();
    }

    // this is done
    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
}
