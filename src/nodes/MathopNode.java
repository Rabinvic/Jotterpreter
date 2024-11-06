package nodes;
import java.util.ArrayList;

import provided.SymbolTable;
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

    public String MathopType() {
        if(rightOperand instanceof NumberNode) {
            if(((NumberNode)rightOperand).isInteger()) {
                return "Integer";
            } else {
                return "Double";
            }
        } else if(rightOperand instanceof IDNode) {
            return SymbolTable.getLocalSymTable().get(((IDNode)rightOperand).getID());
        } else {
            return SymbolTable.getFunctionReturn(((FunctionCallNode)rightOperand).getFuncName());
        }
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
        if (!leftOperand.validateTree() || !rightOperand.validateTree()) {
            return false;
        }

        //left operand is a number
        if (leftOperand instanceof NumberNode){
            boolean leftInt = ((NumberNode)leftOperand).isInteger();

            //right operand is a number
            if (rightOperand instanceof NumberNode) {
                return (leftInt && ((NumberNode)rightOperand).isInteger()) || (!leftInt && !((NumberNode)rightOperand).isInteger());
            } else if (rightOperand instanceof IDNode) { // right operand is an ID
                String rightType = SymbolTable.getLocalSymTable().get(((IDNode)rightOperand).getID());
                return (rightType.equals("Integer") && leftInt) || (rightType.equals("Double") && !leftInt);                
            } else { // right operand is a function call
                String rightReturn = SymbolTable.getFunctionReturn(((FunctionCallNode)rightOperand).getFuncName());
                return (rightReturn.equals("Integer") && leftInt) || (rightReturn.equals("Double") && !leftInt);
            }

        } else if (leftOperand instanceof IDNode){ // left operand is an ID
            String leftType = ((IDNode)leftOperand).getID();
            boolean leftInt = false;
            
            if (leftType.equals("String") || leftType.equals("Boolean")) {
                return false;
            }

            if (leftType.equals("Integer")) {
                leftInt = true;
            }

            //right operand is a number
            if (rightOperand instanceof NumberNode) {
                return (leftInt && ((NumberNode)rightOperand).isInteger()) || (!leftInt && !((NumberNode)rightOperand).isInteger());
            } else if (rightOperand instanceof IDNode) { // right operand is an ID
                String rightType = SymbolTable.getLocalSymTable().get(((IDNode)rightOperand).getID());
                return (rightType.equals("Integer") && leftInt) || (rightType.equals("Double") && !leftInt);                
            } else { // right operand is a function call
                String rightReturn = SymbolTable.getFunctionReturn(((FunctionCallNode)rightOperand).getFuncName());
                return (rightReturn.equals("Integer") && leftInt) || (rightReturn.equals("Double") && !leftInt);
            }

        } else { // left operand is a function call
            String leftType = SymbolTable.getFunctionReturn(((FunctionCallNode)leftOperand).getFuncName());
            boolean leftInt = false;
            
            if (leftType.equals("String") || leftType.equals("Boolean") || leftType.equals("Void")) {
                return false;
            }
            if (leftType.equals("Integer")) {
                leftInt = true;
            }

            //right operand is a number
            if (rightOperand instanceof NumberNode) {
                return (leftInt && ((NumberNode)rightOperand).isInteger()) || (!leftInt && !((NumberNode)rightOperand).isInteger());
            } else if (rightOperand instanceof IDNode) { // right operand is an ID
                String rightType = SymbolTable.getLocalSymTable().get(((IDNode)rightOperand).getID());
                return (rightType.equals("Integer") && leftInt) || (rightType.equals("Double") && !leftInt);                
            } else { // right operand is a function call
                String rightReturn = SymbolTable.getFunctionReturn(((FunctionCallNode)rightOperand).getFuncName());
                return (rightReturn.equals("Integer") && leftInt) || (rightReturn.equals("Double") && !leftInt);
            }
        }
    }

    public void execute() {

    }

}
