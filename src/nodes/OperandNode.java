package nodes;
import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public interface OperandNode extends ExpressionNode{
    public static OperandNode parseOperand(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            IDNode id = IDNode.parseIDNode(tokens);
            if(id == null) {
                return null;
            }
            return id;
        } else if(tokens.get(0).getTokenType() == TokenType.NUMBER) {
            NumberNode num = NumberNode.parseNumberNode(tokens);
            if(num == null) {
                return null;
            }
            return num;
        } else if(tokens.get(0).getTokenType() == TokenType.FC_HEADER) {
            FunctionCallNode func = FunctionCallNode.parseFunctionCallNode(tokens);
            if(func == null) {
                return null;
            }
            return func;
        } else if(tokens.get(0).getTokenType() == TokenType.MATH_OP && tokens.get(0).getToken().equals("-") 
        && tokens.get(1).getTokenType() == TokenType.NUMBER) {
            Token update = new Token(tokens.get(0).getToken() + tokens.get(1).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum(), tokens.get(1).getTokenType());
            tokens.remove(0);
            tokens.remove(1);
            tokens.add(0, update);
            NumberNode num = NumberNode.parseNumberNode(tokens); 
            if(num == null) {
                return null;
            } 
            return num;     
        } else {
            System.err.println("Syntax Error:\n not an operand\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
    }
    // this is done
    public boolean validateTree();
    public String convertToJott();
    public void execute();
}
