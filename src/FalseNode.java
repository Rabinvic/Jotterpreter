import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public class FalseNode implements BoolNode{
    Token boolFalse;

    public FalseNode(Token boolFalse) {
        this.boolFalse = boolFalse;
    }

    public static FalseNode parseFalseNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD && tokens.get(0).getToken() != "False") {
            System.err.println("Syntax Error:\n missing boolean\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        return new FalseNode(tokens.remove(0));
    }

    public String convertToJott(){
        return boolFalse.getToken();
    }

    public boolean validateTree(){
        return true;
    }

    public void execute(){
        
    }
}
