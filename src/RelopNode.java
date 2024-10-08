import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class RelopNode  implements JottTree{
    private Token relop;

    public RelopNode(Token relop) {
        this.relop = relop;
    }

    public static RelopNode parseRelopNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() != TokenType.REL_OP) {
            System.err.println("Syntax Error:\n missing relational operation\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        return new RelopNode(tokens.get(0));
    }

    public String convertToJott() {
        return relop.getToken();
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
}
