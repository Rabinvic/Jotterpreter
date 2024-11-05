package nodes;
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

    public String getID(){
        return this.name.getToken();
    }

    public String getFilename() {
        return name.getFilename();
    }

    public int getLineNum() {
        return name.getLineNum();
    }

    public static IDNode parseIDNode(ArrayList<Token> tokens) {
        if (tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            System.err.println("Syntax Error:\n missing id token\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        if (Character.isUpperCase(tokens.get(0).getToken().charAt(0))) {
            System.err.println("Syntax Error:\n can't parse keyword as id node\n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum() +"\n");
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
