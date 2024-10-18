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
            System.err.println("Syntax Error:\n No tokens to parse");
            return null;
        }

        // <function_def>* <EOF>

        ArrayList<FunctionDefNode> funcdefs = new ArrayList<FunctionDefNode>();
        while(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken() == "Def") {
            FunctionDefNode funcdef = FunctionDefNode.parseFunctionDefNode(tokens);
            if(funcdef == null) {
                return null;
            }
            funcdefs.add(funcdef);
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

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
}