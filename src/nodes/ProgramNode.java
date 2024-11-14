package nodes;
/**
 * @author Alex Roberts
**/

import java.util.ArrayList;

import provided.*;

public class ProgramNode implements JottTree {
    private ArrayList<FunctionDefNode> funcdefs;

    public ProgramNode(ArrayList<FunctionDefNode> funcdefs) {
        this.funcdefs = funcdefs;
    }

    public static ProgramNode parseProgramNode(ArrayList<Token> tokens) {
        if(0==tokens.size()) {
            return new ProgramNode(null);
        }

        // <function_def>* <EOF>

        ArrayList<FunctionDefNode> funcdefs = new ArrayList<FunctionDefNode>();
        while(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            FunctionDefNode funcdef = FunctionDefNode.parseFunctionDefNode(tokens);
            if(funcdef == null) {
                return null;
            }
            funcdefs.add(funcdef);

            if (tokens.size()==0) {
                return new ProgramNode(funcdefs);
            }
        }
        if(funcdefs.isEmpty()){
            return null;
        }
        return new ProgramNode(funcdefs);
    }

    public String convertToJott() {
        String s = "";
        if (this.funcdefs != null) {
            for(int i=0; i<funcdefs.size(); i++) {
                s += funcdefs.get(i).convertToJott();
            }
        }
        return s;
    }

    // presumably done
    public boolean validateTree() {
        boolean containsMain = false;
        for(FunctionDefNode func: funcdefs) {
            if(!func.validateTree()) {
                return false;
            }
            if(func.getName().equals("main")) {
                containsMain = true;
            }
        }
        if(!containsMain) {
            System.err.println("Semantic Error:\n" + "Missing Main Function\n");
            return false;
        }
        return true;
    }

    public void execute() {

    }
}