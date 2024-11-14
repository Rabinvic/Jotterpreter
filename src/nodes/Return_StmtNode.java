package nodes;
import java.util.ArrayList;

import provided.JottTree;
import provided.SymbolTable;
import provided.Token;
import provided.TokenType;

public class Return_StmtNode implements JottTree{
    ExpressionNode exp;

    public Return_StmtNode(ExpressionNode exp) {
        this.exp = exp;
    }

    public static Return_StmtNode parseReturn_StmtNode (ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

         // Return< expr >; | ε

         // ε
        if(tokens.get(0).getTokenType() == TokenType.R_BRACE) {
            return new Return_StmtNode(null);
        }

        // Return
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD && !tokens.get(0).getToken().equals("Return")) {
            System.err.println("Syntax Error:\n missing Return \n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum() +"\n");
            return null;
        }
        tokens.remove(0);

        // < expr >
        ExpressionNode exp = ExpressionNode.parseExpressionNode(tokens);
        if(exp == null) {
            return null;
        }

        // ;
        if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) {
            System.err.println("Syntax Error:\n missing semicolon \n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum() +"\n");
            return null;
        }
        tokens.remove(0);
        return new Return_StmtNode(exp);
    }

    public String convertToJott() {
        if(exp == null) {
            return "";
        }
        return "Return " + exp.convertToJott() + ";";
    }

    // presumably done (NOT DONE??)
    public boolean validateTree() {
        if(exp == null) {
            return true;
        }
        if(!exp.validateTree()) {
            return false;
        }
        if(!SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals("Void")) {

        }
        return true;
    }

    public void execute() {

    }
}
