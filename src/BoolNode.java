import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

public interface BoolNode extends JottTree {
    public static BoolNode parseBool(ArrayList<Token> tokens){
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken() == "True") {
            return TrueNode.parseTrueNode(tokens);
        } else if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken() == "False") {
            return FalseNode.parseFalseNode(tokens);
        } else {
            System.err.println("Syntax Error:\n missing boolean\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
    }

    public String convertToJott();

    public boolean validateTree();

    public void execute();
}
