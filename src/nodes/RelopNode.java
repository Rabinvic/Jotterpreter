package nodes;
import java.util.ArrayList;

import provided.SymbolTable;
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
        if(leftOperand == null) {
            return null;
        }

        if(tokens.get(0).getTokenType() != TokenType.REL_OP) {
            System.err.println("Syntax Error:\n missing relational operator\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        Token relation = tokens.remove(0);
        OperandNode rightOperand = OperandNode.parseOperand(tokens);
        if(rightOperand == null) {
            return null;
        }
        return new RelopNode(leftOperand, relation, rightOperand);
    }

    public String convertToJott() {
        return leftOperand.convertToJott() + relation.getToken() + rightOperand.convertToJott();
    }

    // presumably done
    public boolean validateTree() {
        if(!leftOperand.validateTree() || !rightOperand.validateTree()) {
            return false;
        }

        if(leftOperand instanceof NumberNode) {
            boolean left = ((NumberNode)leftOperand).isInteger();
            
            if(rightOperand instanceof NumberNode) {
                boolean answer = (left && ((NumberNode)rightOperand).isInteger()) || (!left && !((NumberNode)rightOperand).isInteger());
                if(!answer) {
                    System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                relation.getFilename() + ":" + relation.getLineNum());
                }
                return answer;
            } else if(rightOperand instanceof IDNode) {
                String right = SymbolTable.getLocalSymTable().get(((IDNode)rightOperand).getID());
                boolean answer = (right.equals("Integer") && left) || (right.equals("Double") && !left);
                if(!answer) {
                    System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                relation.getFilename() + ":" + relation.getLineNum());
                }
                return answer;
            } else {
                String right = SymbolTable.getFunctionReturn(((FunctionCallNode)rightOperand).getFuncName());
                boolean answer = (right.equals("Integer") && left) || (right.equals("Double") && !left);
                if(!answer) {
                    System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                relation.getFilename() + ":" + relation.getLineNum());
                }
                return answer;
            }
        } else if(leftOperand instanceof IDNode) {
            String left = SymbolTable.getLocalSymTable().get(((IDNode)leftOperand).getID());

            if(rightOperand instanceof NumberNode) {
                boolean answer = (left.equals("Integer") && ((NumberNode)rightOperand).isInteger()) ||
                (left.equals("Double") && !((NumberNode)rightOperand).isInteger());
                if(!answer) {
                    System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                relation.getFilename() + ":" + relation.getLineNum());
                }
                return answer;
            } else if(rightOperand instanceof IDNode) {
                String right = SymbolTable.getLocalSymTable().get(((IDNode)rightOperand).getID());
                boolean answer = (right.equals("Integer") && left.equals("Integer")) ||
                (right.equals("Double") && left.equals("Double"));
                if(!answer) {
                    System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                relation.getFilename() + ":" + relation.getLineNum());
                }
                return answer;
            } else {
                String right = SymbolTable.getFunctionReturn(((FunctionCallNode)rightOperand).getFuncName());
                boolean answer = (right.equals("Integer") && left.equals("Integer")) || 
                (right.equals("Double") && left.equals("Double"));
                if(!answer) {
                    System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                relation.getFilename() + ":" + relation.getLineNum());
                }
                return answer;
            }
        } else {
            String left = SymbolTable.getFunctionReturn(((FunctionCallNode)leftOperand).getFuncName());

            if(rightOperand instanceof NumberNode) {
                boolean answer = (left.equals("Integer") && ((NumberNode)rightOperand).isInteger()) ||
                (left.equals("Double") && !((NumberNode)rightOperand).isInteger());
                if(!answer) {
                    System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                relation.getFilename() + ":" + relation.getLineNum());
                }
                return answer;
            } else if(rightOperand instanceof IDNode) {
                String right = SymbolTable.getLocalSymTable().get(((IDNode)rightOperand).getID());
                boolean answer = (right.equals("Integer") && left.equals("Integer")) ||
                (right.equals("Double") && left.equals("Double"));
                if(!answer) {
                    System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                relation.getFilename() + ":" + relation.getLineNum());
                }
                return answer;
            } else {
                String right = SymbolTable.getFunctionReturn(((FunctionCallNode)rightOperand).getFuncName());
                boolean answer = (right.equals("Integer") && left.equals("Integer")) || 
                (right.equals("Double") && left.equals("Double"));
                if(!answer) {
                    System.err.println("Semantic Error:\n" + "both operands are not of the same type\n" +
                                                relation.getFilename() + ":" + relation.getLineNum());
                }
                return answer;
            }
        }
    }

    public void execute() {
        leftOperand.execute();
        rightOperand.execute();

        String left;
        if(leftOperand instanceof IDNode) {
            String currentFunc = SymbolTable.currentCalledFunc.peek();
            String idOfOperand = ((IDNode)leftOperand).getID();
            left = SymbolTable.fbodys.get(currentFunc).varValues.get(idOfOperand);
        } else {
            left = SymbolTable.vals.get(leftOperand);
        }
        String right;
        if(rightOperand instanceof IDNode) {
            String currentFunc = SymbolTable.currentCalledFunc.peek();
            String idOfOperand = ((IDNode)rightOperand).getID();
            right = SymbolTable.fbodys.get(currentFunc).varValues.get(idOfOperand);
        } else {
            right = SymbolTable.vals.get(rightOperand);
        }

        Boolean result = null;
        switch (relation.getToken()) {
            case "!=":
                result = Double.parseDouble(left) != Double.parseDouble(right);
                break;
            case "==":
                result = Double.parseDouble(left) == Double.parseDouble(right);
                break;
            case "<=":
                result = Double.parseDouble(left) <= Double.parseDouble(right);
                break;
            case ">=":
                result = Double.parseDouble(left) >= Double.parseDouble(right);
                break;
            case "<":
                result = Double.parseDouble(left) < Double.parseDouble(right);
                break;
            case ">":
                result = Double.parseDouble(left) > Double.parseDouble(right);
                break;
        }
        SymbolTable.vals.put(this, result.toString());
    }

    public String getFilename() {
        return relation.getFilename();
    }
    public int getLineNum() {
        return relation.getLineNum();
    }
}
