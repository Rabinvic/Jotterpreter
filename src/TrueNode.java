import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public class TrueNode implements BoolNode {

    public Token boolTrue;

    public TrueNode(Token boolTrue) {
        this.boolTrue = boolTrue;
    }

    public String convertToJott() {
        return boolTrue.getToken();
    }

    public static TrueNode parseTrueNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD && tokens.get(0).getToken() != "True") {
            System.err.println("Syntax Error:\n missing boolean\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        return new TrueNode(tokens.remove(0));
    }

    public boolean validateTree(){
        return true;
    }

    public void execute() {

    }
}
