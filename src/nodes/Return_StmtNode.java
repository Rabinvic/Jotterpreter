package nodes;
import java.util.ArrayList;

import provided.JottTree;
import provided.SymbolTable;
import provided.Token;
import provided.TokenType;

public class Return_StmtNode implements JottTree{
    ExpressionNode exp;

    public Return_StmtNode(ExpressionNode exp) {
        this.exp = exp;
    }

    public static Return_StmtNode parseReturn_StmtNode (ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

         // Return< expr >; | ε

         // ε
        if(tokens.get(0).getTokenType() == TokenType.R_BRACE) {
            return new Return_StmtNode(null);
        }

        // Return
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD && !tokens.get(0).getToken().equals("Return")) {
            System.err.println("Syntax Error:\n missing Return \n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum() +"\n");
            return null;
        }
        tokens.remove(0);

        // < expr >
        ExpressionNode exp = ExpressionNode.parseExpressionNode(tokens);
        if(exp == null) {
            return null;
        }

        // ;
        if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) {
            System.err.println("Syntax Error:\n missing semicolon \n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum() +"\n");
            return null;
        }
        tokens.remove(0);
        return new Return_StmtNode(exp);
    }

    public String convertToJott() {
        if(exp == null) {
            return "";
        }
        return "Return " + exp.convertToJott() + ";";
    }

    // presumably done (NOT DONE??)
    public boolean validateTree() {
        if(exp == null && SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals("Void")) {
            return true;
        } else if(exp == null && !SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals("Void")) {
            return true;
        }

        if(!exp.validateTree()) {
            return false;
        }
        if(exp instanceof RelopNode || exp instanceof BoolNode) {
            if(!SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals("Boolean")) {
                System.err.println("Semantic Error:\n" + "Return value doesn't match function return\n" +
                                       exp.getFilename() + ":" + exp.getLineNum());
                return false;
            }
        } else if(exp instanceof String_literalNode) {
            if(!SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals("String")) {
                System.err.println("Semantic Error:\n" + "Return value doesn't match function return\n" +
                                       exp.getFilename() + ":" + exp.getLineNum());
                return false;
            }
        } else if(exp instanceof MathopNode) {
            if(!SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals(((MathopNode)exp).MathopType())) {
                System.err.println("Semantic Error:\n" + "Return value doesn't match function return\n" +
                                       exp.getFilename() + ":" + exp.getLineNum());
                return false;
            }
        } else {
            if(exp instanceof IDNode) {
                if(!SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals(SymbolTable.getLocalSymTable().get(((IDNode)exp).getID()))) {
                    System.err.println("Semantic Error:\n" + "Return value doesn't match function return\n" +
                                       exp.getFilename() + ":" + exp.getLineNum());
                    return false;
                }
            } else if(exp instanceof FunctionCallNode) {
                if(!SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals(SymbolTable.getFunctionReturn(((FunctionCallNode)exp).getFuncName()))) {
                    System.err.println("Semantic Error:\n" + "Return value for called function does not match current function\n" +
                                       exp.getFilename() + ":" + exp.getLineNum());
                    return false;
                }
            } else {
                String numType;
                if(((NumberNode)exp).isInteger()) {
                    numType = "Integer";
                } else {
                    numType = "Double";
                }
                if(!SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals(numType)) {
                    System.err.println("Semantic Error:\n" + "Return value does not match current function\n" +
                                       exp.getFilename() + ":" + exp.getLineNum());
                    return false;
                }
            }
        }
        return true;
    }

    public boolean returnExists() {
        if(exp != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean notFinished() {
        if(exp == null && !SymbolTable.getFunctionReturn(SymbolTable.currentFunc).equals("Void")) {
            return true;
        } else {
            return false;
        }
    }

    public void execute() {
        if(exp == null) {
            return;
        }
        exp.execute();
        if(exp instanceof RelopNode) {
            SymbolTable.vals.put(this, SymbolTable.vals.get((RelopNode)exp));
        } else if(exp instanceof MathopNode) {
            SymbolTable.vals.put(this, SymbolTable.vals.get((MathopNode)exp));
        } else if(exp instanceof String_literalNode) {
            SymbolTable.vals.put(this, SymbolTable.vals.get((String_literalNode)exp));
        } else if(exp instanceof BoolNode) {
            SymbolTable.vals.put(this, SymbolTable.vals.get((BoolNode)exp));
        } else if(exp instanceof FunctionCallNode) {
            SymbolTable.vals.put(this, SymbolTable.vals.get((FunctionCallNode)exp));
        } else if(exp instanceof NumberNode) {
            SymbolTable.vals.put(this, SymbolTable.vals.get((NumberNode)exp));
        } else {
            if(SymbolTable.fbodys.get(SymbolTable.currentCalledFunc.peek()).varValues.get(((IDNode)exp).getID()) != null) {
                SymbolTable.vals.put(this, SymbolTable.fbodys.get(SymbolTable.currentCalledFunc.peek()).varValues.get(((IDNode)exp).getID()));
            } else {
                System.out.println("runtime error variable used before being assigned a value");
            }
        }
    }
}
