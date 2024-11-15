package nodes;
import java.util.ArrayList;

import provided.SymbolTable;
import provided.Token;
import provided.TokenType;

public class FunctionCallNode implements OperandNode, Body_StmtNode{
    IDNode name;
    ParamsNode params;

    public FunctionCallNode(IDNode name, ParamsNode params) {
        this.name = name;
        this.params = params;
    }

    public String getFilename() {
        return name.getFilename();
    }

    public int getLineNum() {
        return name.getLineNum();
    }

    public String getFuncName(){
        return this.name.getID();
    }
    
    public String convertToJott() {
        return "::" + name.convertToJott() + "[" + params.convertToJott() + "]";
    }

    public String convertToJottAlone(){
        return "";
    }



    public static FunctionCallNode parseFunctionCallNode(ArrayList<Token> tokens){
        if (tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if (tokens.get(0).getTokenType() != TokenType.FC_HEADER) {
            System.err.println("Syntax Error:\n missing function header\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);
        IDNode id = IDNode.parseIDNode(tokens);
        if(id == null) {
            return null;
        }
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            System.err.println("Syntax Error:\n missing left bracket\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);
        ParamsNode params = ParamsNode.parseParamsNode(tokens);
        if(params == null) {
            return null;
        }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            System.err.println("Syntax Error:\n missing right bracket\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);
        return new FunctionCallNode(id, params);
    }

    // presumably done
    public boolean validateTree() {
        if(!params.validateTree()) {
            return false;
        }

        if(!SymbolTable.funcTypes.containsKey(name.getID()) && (!name.getID().equals("print") || !name.getID().equals("concat") || !name.getID().equals("length"))) {
            System.err.println("Semantic Error:\n" + "attempting to call function that hasn't been defined\n" +
                    name.getFilename() + ":" + name.getLineNum() + "\n");
            return false;
        }

        //params from function call
        ArrayList<ExpressionNode> parameters = params.paramsHelper();
        //params from function declaration
        ArrayList<String> declaredParams= SymbolTable.getFunctionParameters(name.getID());
        if(parameters == null) {
            if(declaredParams.size() != 0) {
                System.err.println("Semantic Error:\n" + "passed in no parameters when parameters are needed\n" +
                    name.getFilename() + ":" + name.getLineNum() + "\n");
                return false;
            }
            return true;
        }

        if(parameters.size() != declaredParams.size()) {
            System.err.println("Semantic Error:\n" + "function call has incorrect number of parameters\n" +
                    name.getFilename() + ":" + name.getLineNum() + "\n");
            return false;
        }

        for(int i = 0; i < parameters.size(); i++) {
            if(parameters.get(i) instanceof BoolNode || parameters.get(i) instanceof RelopNode) {
                if(!declaredParams.get(i).equals("Boolean")) {
                    System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                    name.getFilename() + ":" + name.getLineNum() + "\n");
                    return false;
                }
            } else if(parameters.get(i) instanceof MathopNode) {
                if(!declaredParams.get(i).equals(((MathopNode)parameters.get(i)).MathopType())) {
                    System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                    name.getFilename() + ":" + name.getLineNum() + "\n");
                    return false;
                }
            } else if(parameters.get(i) instanceof String_literalNode) {
                if(!declaredParams.get(i).equals("String")) {
                    System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                    name.getFilename() + ":" + name.getLineNum() + "\n");
                    return false;
                }
            } else {
                if(parameters.get(i) instanceof IDNode) {
                    if(!declaredParams.get(i).equals(SymbolTable.getLocalSymTable().get(((IDNode)parameters.get(i)).getID()))) {
                        System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                        name.getFilename() + ":" + name.getLineNum() + "\n");
                        return false;
                    }
                } else if(parameters.get(i) instanceof FunctionCallNode) {
                    if(!declaredParams.get(i).equals(SymbolTable.getFunctionReturn(((FunctionCallNode)parameters.get(i)).getFuncName()))) {
                        System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                        name.getFilename() + ":" + name.getLineNum() + "\n");
                        return false;
                    }
                } else {
                    String parameterType;
                    if(((NumberNode)parameters.get(i)).isInteger()) {
                        parameterType = "Integer";
                    } else {
                        parameterType = "Double";
                    }
                    if(!declaredParams.get(i).equals(parameterType)) {
                        System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                        name.getFilename() + ":" + name.getLineNum() + "\n");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void execute() {
        
    }
}
