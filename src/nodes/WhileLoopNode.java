package nodes;
import java.util.ArrayList;

import provided.SymbolTable;
import provided.Token;
import provided.TokenType;

public class WhileLoopNode implements Body_StmtNode{
    private ExpressionNode expr;
    private BodyNode body;

    public WhileLoopNode(ExpressionNode expr, BodyNode body){
        this.expr = expr;
        this.body = body;
    }

    public static WhileLoopNode parseWhileLoopNode(ArrayList<Token> tokens){
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        // While [ < expr >]{ < body >}

        // While
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD || !tokens.get(0).getToken().equals("While")) {
            System.err.println("Syntax Error:\n missing ''While' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // [
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            System.err.println("Syntax Error:\n missing ''[' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < expr >
        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);
        if(expr == null) {
            return null;
        }

        // ]
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            System.err.println("Syntax Error:\n missing '']' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // {
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            System.err.println("Syntax Error:\n missing ''{' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < body >
        BodyNode body = BodyNode.parseBodyNode(tokens);
        if(body == null) {
            return null;
        }

        // }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            System.err.println("Syntax Error:\n missing ''}' in While Loop\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        return new WhileLoopNode(expr, body);
    }

    
    public String convertToJott() {
        // While [ < expr >]{ < body >}
        return "While[" + expr.convertToJott() + "]{" + body.convertToJott() + "}";
    }

    // presumably done
    public boolean validateTree() {
        if(!expr.validateTree() || !body.validateTree()) {
            return false;
        }
        if(expr instanceof MathopNode || expr instanceof String_literalNode) {
            if(expr instanceof MathopNode){
                System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
            ((MathopNode)expr).getFilename() + ":" + ((MathopNode)expr).getLineNum() + "\n");
            } else {
                System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
                ((String_literalNode)expr).getFilename() + ":" + ((String_literalNode)expr).getLineNum() + "\n");
            }
            return false;
        } else if(expr instanceof OperandNode) {
            if(expr instanceof IDNode) {
                if(!SymbolTable.getLocalSymTable().get(((IDNode)expr).getID()).equals("Boolean")) {
                    System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
                    ((IDNode)expr).getFilename() + ":" + ((IDNode)expr).getLineNum() + "\n");
                    return false;
                }
            } else if(expr instanceof FunctionCallNode) {
                if(!SymbolTable.getFunctionReturn(((FunctionCallNode)expr).getFuncName()).equals("Boolean")) {
                    System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
                    ((FunctionCallNode)expr).getFilename() + ":" + ((FunctionCallNode)expr).getLineNum() + "\n");
                    return false;
                }
            } else {
                System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
                ((NumberNode)expr).getFilename() + ":" + ((NumberNode)expr).getLineNum() + "\n");
                return false;
            }
        }
        return true;
    }
    public void execute() {
        
    }
}
