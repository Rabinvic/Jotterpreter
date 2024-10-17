/**
 * @author Alex Roberts
**/

import java.util.ArrayList;
import provided.*;

public class FBodyNode implements JottTree {
    private ArrayList<Var_DecNode> vardecs;
    private BodyNode body;

    public FBodyNode() {
        this.vardecs = vardecs;
        this.body = body;
    }

    public static FBodyNode parseFBodyNode(ArrayList<Token> tokens) {
        if(0==tokens.size()) {
            System.err.print("Syntax Error:\n No tokens to parse\n");
            return null;
        }
        
    }

    public String convertToJott() {
        String s = "";
        if (this.vardecs != null) {
            for(int i=0; i<funcdefs.size(); i++) {
                s += vardecs.get(i).convertToJott();
            }
        }
        s += this.body.convertToJott();
        return s;
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
}