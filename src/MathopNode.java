import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public class MathopNode  implements ExpressionNode{
    private Token math;
    private OperandNode leftOperand;
    private OperandNode rightOperand;

    public MathopNode(OperandNode leftOperand, Token math, OperandNode rightOperand){
        this.leftOperand = leftOperand;
        this.math = math;
        this.rightOperand = rightOperand;
    }

    public static MathopNode parseMathopNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        OperandNode leftOperand = OperandNode.parseOperand(tokens);
        if(leftOperand == null) {
            return null;
        }

        if(tokens.get(0).getTokenType() != TokenType.MATH_OP) {
            System.err.println("Syntax Error:\n missing math operator\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        Token math = tokens.remove(0);
        OperandNode rightOperandNode = OperandNode.parseOperand(tokens);
        if(rightOperandNode == null) {
            return null;
        }

        return new MathopNode(leftOperand, math, rightOperandNode);
    }

    public String convertToJott() {
        return leftOperand.convertToJott() + math.getToken() + rightOperand.convertToJott();
    }

    public boolean validateTree(){
        return true;
    }

    public void execute() {

    }

}
