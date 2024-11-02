package nodes;
import java.util.ArrayList;

import provided.SymbolTable;
//import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class AsmtNode implements Body_StmtNode{
    private IDNode id;
    private ExpressionNode expr;

    public AsmtNode(IDNode id, ExpressionNode expr){
        this.id = id;
        this.expr = expr;
    }
    public static AsmtNode parseAsmtNode(ArrayList<Token> tokens){
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        IDNode id = IDNode.parseIDNode(tokens);
        if(id == null){             
            return null;
        }
        if(tokens.get(0).getTokenType() != TokenType.ASSIGN) {
            System.err.println("Syntax Error:\n missing ''='\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        tokens.remove(0);

        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);
        if(expr == null){             
            return null;
        }


        if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) {
            System.err.println("Syntax Error:\n missing '';'\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }

        tokens.remove(0); //remove ;

        return new AsmtNode(id, expr);

    }
    
    public String convertToJott() {
        return this.id.convertToJott() + "=" + this.expr.convertToJott() + ";";
    }
    public boolean validateTree() {
        // TODO Auto-generated method stub
        if (!id.validateTree()) {
            return false;
        }
        String varType = SymbolTable.getLocalSymTable().get(id.getID());
        
        return true;
    }
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

}
