import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public class AsmtNode {
    private IDNode id;
    private ExpressionNode expr;

    public AsmtNode(IDNode id, ExpressionNode expr){
        this.id = id;
        this.expr = expr;
    }
    public static AsmtNode parseBool(ArrayList<Token> tokens){
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        IDNode id = IDNode.parseIDNode(tokens);
        if(tokens.get(0).getTokenType() != TokenType.ASSIGN) {
            System.err.println("Syntax Error:\n missing ''='\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        tokens.remove(0);

        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);

        return new AsmtNode(id, expr);

    }

}
