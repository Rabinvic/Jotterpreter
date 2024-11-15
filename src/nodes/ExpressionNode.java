package nodes;
import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

public interface ExpressionNode extends JottTree{
    public static ExpressionNode parseExpressionNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        // < operand > |
        // < operand > < relop > < operand > |
        // < operand > < mathop > < operand > |
        // < string_literal > |
        // < bool >


        // < string_literal >
        if(tokens.get(0).getTokenType() == TokenType.STRING) {
            String_literalNode tempStr = String_literalNode.parseString_literalNode(tokens);
            if(tempStr == null) {
                return null;
            }
            return tempStr;
        }
        // < bool >
        else if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && 
        (tokens.get(0).getToken().equals("True") || tokens.get(0).getToken().equals("False"))) {
            BoolNode tempBool = BoolNode.parseBool(tokens);
            if(tempBool == null) {
                return null;
            }
            return tempBool;
        }
        else if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD ||
        tokens.get(0).getTokenType() == TokenType.NUMBER ||
        tokens.get(0).getTokenType() == TokenType.FC_HEADER ||
            (tokens.get(0).getTokenType() == TokenType.MATH_OP &&
            tokens.get(0).getToken().equals("-") &&
            tokens.get(1).getTokenType() == TokenType.NUMBER)) {

                ArrayList<Token> tokenCopy = new ArrayList<>(tokens);

                OperandNode.parseOperand(tokenCopy);

                //< operand > < mathop > < operand >
                if(tokenCopy.get(0).getTokenType() == TokenType.MATH_OP) {
                    MathopNode tempMath = MathopNode.parseMathopNode(tokens);
                    if(tempMath == null) {
                        return null;
                    }
                    return tempMath;
                } 
                // < operand > < relop > < operand >
                else if(tokenCopy.get(0).getTokenType() == TokenType.REL_OP) {
                    RelopNode tempRel = RelopNode.parseRelopNode(tokens);
                    if(tempRel == null) {
                        return null;
                    }
                    return tempRel;
                } 
                // < operand >
                else {
                    OperandNode tempOp = OperandNode.parseOperand(tokens);
                    if(tempOp == null) {
                        return null;
                    }
                    return tempOp;
                }   
            } else {
                System.err.println("Syntax Error:\n not an expression\n" + tokens.get(0).getFilename() + ":" + 
                tokens.get(0).getLineNum() + "\n");
                return null;
            }   
    }

    // this is done
    public boolean validateTree();
    public String convertToJott();
    public void execute();

    public String getFilename();
    public int getLineNum();
}
