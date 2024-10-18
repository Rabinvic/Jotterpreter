package nodes;
import java.util.ArrayList;

import provided.*;

public class ElseNode implements Body_StmtNode{
    private BodyNode body;

    public ElseNode(BodyNode body){
        this.body = body;
    }

    public static ElseNode parseElseNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        // empty string
        if(!tokens.get(0).getToken().equals("Else")) {
            return new ElseNode(null);
        }

        // Else{< body >}

        // Else
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD || !tokens.get(0).getToken().equals("Else")) {
            System.err.println("Syntax Error:\n missing ''Else'\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // {
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            System.err.println("Syntax Error:\n missing ''{' in Else Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < body >
        BodyNode body = BodyNode.parseBodyNode(tokens);
        if(body == null) {
            return null;
        }

        // }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            System.err.println("Syntax Error:\n missing ''}' in Else Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);


        return new ElseNode(body);
    }

    public String convertToJott(){
        // Else{< body >}
        return "Else{" + body.convertToJott() + "}";
    }
    public boolean validateTree() {
        return true;
    }
    public void execute(){

    }
}