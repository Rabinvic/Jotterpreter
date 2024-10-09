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
        if(tokens.get(0).getTokenType() == TokenType.STRING) {
            return String_literalNode.parseString_literalNode(tokens);
        } else if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && 
        (tokens.get(0).getToken() == "True" || tokens.get(0).getToken() == "False")) {
            return BoolNode.parseBool(tokens);
            //maybe get rid of
        } else if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD || tokens.get(0).getTokenType() == TokenType.NUMBER ||
        tokens.get(0).getTokenType() == TokenType.FC_HEADER || (tokens.get(0).getTokenType() == TokenType.MATH_OP && 
        tokens.get(0).getToken() == "-" && tokens.get(1).getTokenType() == TokenType.NUMBER)) {
            if(tokens.get(1).getTokenType() == TokenType.MATH_OP) {
                return MathopNode.parseMathopNode(tokens);
            } else if(tokens.get(1).getTokenType() == TokenType.REL_OP) {
                return RelopNode.parseRelopNode(tokens);
            } else {
                return OperandNode.parseOperand(tokens);
            }   
        } else {
            System.err.println("Syntax Error:\n not an expression\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
    }

    public boolean validateTree();
    public String convertToJott();
    public void execute();
}
