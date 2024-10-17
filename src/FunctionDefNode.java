/**
 * @author Alex Roberts
**/

import java.util.ArrayList;
import provided.*;

public class FunctionDefNode implements JottTree {
    private ;

    public FunctionDefNode() {
        this. = ;
    }

    public static FunctionDefNode parseFunctionDefNode(ArrayList<Token> tokens) {
        if(0==tokens.size()) {
            System.err.print("Syntax Error:\n No tokens to parse\n");
            return null;
        }
        
    }

    public String convertToJott() {
        String s = "Def ";
        s += this.id.convertToJott();
        s += "[";
        s += ;
        s += "] :";
        s += ;
        s += "{";
        s += ;
        s += "}";
        return s;
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
}