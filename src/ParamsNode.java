import java.util.ArrayList;

import provided.JottTree;
import provided.Token;

public class ParamsNode implements OperandNode{
    public String convertToJott() {
        return "";
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
    
    public static ParamsNode parseParamsNode(ArrayList<Token> tokens) {
        return new ParamsNode();
    }

}
