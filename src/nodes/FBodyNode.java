package nodes;
/**
 * @author Alex Roberts
**/

import java.util.ArrayList;

import provided.*;

public class FBodyNode implements JottTree {
    private ArrayList<Var_DecNode> vardecs;
    private BodyNode body;

    public FBodyNode(ArrayList<Var_DecNode> vardecs, BodyNode body) {
        this.vardecs = vardecs;
        this.body = body;
    }

    public static FBodyNode parseFBodyNode(ArrayList<Token> tokens) {
        if(0==tokens.size()) {
            System.err.println("Syntax Error:\n No tokens to parse");
            return null;
        }
        
        // <var_dec>* <body>

        // <var_dec>*
        ArrayList<Var_DecNode> vardecs = new ArrayList<Var_DecNode>();
        while(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && Character.isUpperCase(tokens.get(0).getToken().charAt(0))) {
            if (BodyNode.isbodyStartKeyword(tokens.get(0))) {
                break;
            }
            Var_DecNode vardec = Var_DecNode.parseVarDecNode(tokens);
            if(vardec == null) {
                return null;
            }
            vardecs.add(vardec);
        }

        // <body>
        BodyNode body = BodyNode.parseBodyNode(tokens);
        if(body == null) {
            return null;
        }

        return new FBodyNode(vardecs, body);
    }

    public String convertToJott() {
        String s = "";
        if (this.vardecs != null) {
            for(int i=0; i<vardecs.size(); i++) {
                s += vardecs.get(i).convertToJott();
            }
        }
        s += this.body.convertToJott();
        return s;
    }

    public boolean validateTree() {
        if (!body.validateTree()) { 
            return false;
        }

        // if the current function has a return type
        if(!SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals("Void")){
            // and there are no return statements in it
            if(!body.hasReturnStmt()) {
                System.err.println("Semantic Error:\nMissing return statement in function body.\nFunction: " + SymbolTable.currentFunc + "\n"); // handle last line of error
                return false;
            }
            return true;
        }
        // if the current function is a Void
        if(body.hasReturnStmt()) {
            System.err.println("Semantic Error:\nCannot return in a 'Void' function.\nFunction: " + SymbolTable.currentFunc + "\n"); // handle last line of error
            return false;
        }

        return true;
    }

    public void execute() {

    }
}