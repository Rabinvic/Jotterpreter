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
    
    // presumably done
    public boolean validateTree() {
        if (!id.validateTree() || !expr.validateTree()) {
            return false;
        }
        String varType = SymbolTable.getLocalSymTable().get(id.getID());
        if(varType == null) {
            System.err.println("Semantic Error:\n" + "attempting to assign a variable that hasn't been declared\n" +
            id.getFilename() + ":" + id.getLineNum());
            return false;
        }
        if(expr instanceof String_literalNode) {
            if(!varType.equals("String")) {
                System.err.println("Semantic Error:\n" + "String variable cannot be assigned a non string value\n" +
                id.getFilename() + ":" + id.getLineNum());
                return false;
            }
        } else if(expr instanceof BoolNode || expr instanceof RelopNode) {
            if(!varType.equals("Boolean")) {
                System.err.println("Semantic Error:\n" + "Boolean variable cannot be assigned a non boolean value\n" +
                id.getFilename() + ":" + id.getLineNum());
                return false;
            }
        } else if(expr instanceof MathopNode) { 
            if(!((MathopNode)expr).MathopType().equals(varType)) {
                System.err.println("Semantic Error:\n" + "variable type doesn't match expression type\n" +
                id.getFilename() + ":" + id.getLineNum());
                return false;
            }
        }else { // operandNode
            if(expr instanceof IDNode) {
                if(!SymbolTable.getLocalSymTable().get(((IDNode)expr).getID()).equals(varType)) {
                    System.err.println("Semantic Error:\n" + "variable type doesn't match expression type\n" +
                    id.getFilename() + ":" + id.getLineNum());
                    return false;
                }
            } else if(expr instanceof FunctionCallNode) {
                if (((FunctionCallNode)expr).getFuncName().equals("length")) {
                    if (!varType.equals("Integer")){
                        System.err.println("Semantic Error:\n" + "variable type doesn't match type Integer\n" +
                        id.getFilename() + ":" + id.getLineNum());
                        return false;
                    }
                    return true;
                }

                if (((FunctionCallNode)expr).getFuncName().equals("concat")) {
                    if (!varType.equals("String")){
                        System.err.println("Semantic Error:\n" + "variable type doesn't match type String\n" +
                        id.getFilename() + ":" + id.getLineNum());
                        return false;
                    }
                    return true;                   
                }

                if(!SymbolTable.getFunctionReturn(((FunctionCallNode)expr).getFuncName()).equals(varType)) {
                    System.err.println("Semantic Error:\n" + "variable type doesn't match expression type\n" +
                    id.getFilename() + ":" + id.getLineNum());
                    return false;
                }
            } else {
                if((((NumberNode)expr).isInteger() && varType.equals("Integer")) || (!(((NumberNode)expr).isInteger()) && varType.equals("Double"))) {
                    return true;
                } else {
                    System.err.println("Semantic Error:\n" + "variable type doesn't match expression type\n" +
                    id.getFilename() + ":" + id.getLineNum());
                    return false;
                }
            }
        }
        
        return true;
    }
    public void execute() {
        // compute the value of the expression first
        expr.execute();

        String currentFunc = SymbolTable.currentCalledFunc.peek();
        String value = SymbolTable.vals.get(expr);

        SymbolTable.fbodys.get(currentFunc).varValues.put(id.getID(), value);

        // call id's execute at the end so that its value is updated into symboltable.vals
        id.execute();
    }
}
