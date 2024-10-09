import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public class RelopNode  implements ExpressionNode{
    private OperandNode leftOperand;
    private Token relation;
    private OperandNode rightOperand;

    public RelopNode(OperandNode leftOperand, Token relation, OperandNode rightOperand) {
        this.leftOperand = leftOperand;
        this.relation = relation;
        this.rightOperand = rightOperand;
    }

    public static RelopNode parseRelopNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        OperandNode leftOperand = OperandNode.parseOperand(tokens);

        if(tokens.get(0).getTokenType() != TokenType.REL_OP) {
            System.err.println("Syntax Error:\n missing relational operator\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        Token relation = tokens.remove(0);

        return new RelopNode(leftOperand, relation, OperandNode.parseOperand(tokens));
    }

    public String convertToJott() {
        return leftOperand.convertToJott() + relation.getToken() + rightOperand.convertToJott();
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {

    }
}
