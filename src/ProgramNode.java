/**
 * @author Alex Roberts
**/

import java.util.ArrayList;
import provided.*;

public class ProgramNode implements JottTree {
    private ArrayList<FunctionDefNode> funcdefs;

    public ProgramNode() {
        this.funcdefs = funcdefs;
    }

    public static ProgramNode parseProgramNode(ArrayList<Token> tokens) {
        if(0==tokens.size()) {
            System.err.print("Syntax Error:\n No tokens to parse\n");
            return null;
        }

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