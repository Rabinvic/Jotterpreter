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

    public String getFilename() {
        return math.getFilename();
    }

    public int getLineNum() {
        return math.getLineNum();
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

    // presumably done
    public boolean validateTree(){
        if (!leftOperand.validateTree() || !rightOperand.validateTree()) {
            return false;
        }

        String leftType = "";
        boolean leftInt = false;

        // get the type of the left operand
        if (leftOperand instanceof NumberNode) {
            leftInt = ((NumberNode)leftOperand).isInteger();
        } else if (leftOperand instanceof IDNode) {
            leftType = SymbolTable.getLocalSymTable().get(((IDNode)leftOperand).getID());
        } else { // left operand is a function call
            if (((FunctionCallNode)leftOperand).getFuncName().equals("length")){
                leftType = "Integer";
            } else {
                leftType = SymbolTable.getFunctionReturn(((FunctionCallNode)leftOperand).getFuncName());
            }
            
        }

        // invalid if left operand is a string, boolean, or void
        if (leftType.equals("String") ||
            leftType.equals("Boolean") ||
            leftType.equals("Void")) {
            System.err.println("Semantic Error:\n" + "cannot evaluate using left operand's type\n" +
            math.getFilename() + ":" + math.getLineNum());
            return false;
        }
        // determine if left operand is an integer
        if (leftType.equals("Integer")) {
            leftInt = true;
        }

        String rightType = "";

        // invalid if right operand doesn't match left operand's type (short circuit return for number nodes)
        if (rightOperand instanceof NumberNode) {
            boolean answer = (leftInt && ((NumberNode)rightOperand).isInteger()) || (!leftInt && !((NumberNode)rightOperand).isInteger());

            // divide by zero
            if (math.getToken().equals("/")) {
                double rightValue = Double.parseDouble(((NumberNode)rightOperand).convertToJott());
                if (rightValue == 0.0) {
                    System.err.println("Semantic Error:\n" + "Division by Zero is not allowed\n" +
                                       math.getFilename() + ":" + math.getLineNum());
                    return false;
                }
            }

            if (!answer)
                System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                math.getFilename() + ":" + math.getLineNum());
            return answer;
        } else if (rightOperand instanceof IDNode) {
            rightType = SymbolTable.getLocalSymTable().get(((IDNode)rightOperand).getID());
        } else { // right operand is a function call
            rightType = SymbolTable.getFunctionReturn(((FunctionCallNode)rightOperand).getFuncName());
        }

        // invalid if right operand is a string, boolean, or void
        if (leftType.equals("String") ||
            leftType.equals("Boolean") ||
            leftType.equals("Void")) {
            System.err.println("Semantic Error:\n" + "cannot evaluate using right operand's type\n" +
            math.getFilename() + ":" + math.getLineNum());
            return false;
        }

        // invalid if right operand doesn't match left operand's type
        boolean answer = (rightType.equals("Integer") && leftInt) || (rightType.equals("Double") && !leftInt);
        if (!answer)
            System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                            math.getFilename() + ":" + math.getLineNum());
        return answer;
    }

    public void execute() {
        leftOperand.execute();
        rightOperand.execute();

        // get string representation of left operand value
        String left;
        if(leftOperand instanceof IDNode) {
            String currentFunc = SymbolTable.currentCalledFunc.peek();
            String idOfOperand = ((IDNode)leftOperand).getID();
            left = SymbolTable.fbodys.get(currentFunc).varValues.get(idOfOperand);
        } else {
            left = SymbolTable.vals.get(leftOperand);
        }
        // get string representation of right operand value
        String right;
        if(rightOperand instanceof IDNode) {
            String currentFunc = SymbolTable.currentCalledFunc.peek();
            String idOfOperand = ((IDNode)rightOperand).getID();
            right = SymbolTable.fbodys.get(currentFunc).varValues.get(idOfOperand);
        } else {
            right = SymbolTable.vals.get(rightOperand);
        }

        // MathopType is good to use here
        if (this.MathopType().equals("Integer")) {
            Integer result = null;
            switch (math.getToken()) {
                case "/": result = Integer.parseInt(left) / Integer.parseInt(right); break;
                case "*": result = Integer.parseInt(left) * Integer.parseInt(right); break;
                case "+": result = Integer.parseInt(left) + Integer.parseInt(right); break;
                case "-": result = Integer.parseInt(left) - Integer.parseInt(right); break;
            }
            SymbolTable.vals.put(this, result.toString());
        } else {
            Double result = null;
            switch (math.getToken()) {
                case "/": result = Double.parseDouble(left) / Double.parseDouble(right); break;
                case "*": result = Double.parseDouble(left) * Double.parseDouble(right); break;
                case "+": result = Double.parseDouble(left) + Double.parseDouble(right); break;
                case "-": result = Double.parseDouble(left) - Double.parseDouble(right); break;
            }
            SymbolTable.vals.put(this, result.toString());
        }
    }
}
