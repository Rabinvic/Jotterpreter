/**
 * @author Analucia Macias Marin
**/
package nodes;
import java.util.ArrayList;
import java.util.HashMap;

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

        if(name.getID().equals("print")){

            ArrayList<ExpressionNode> parameters = params.paramsHelper();
            if(parameters == null) {
                System.err.println("Semantic Error:\n" + "passed in no parameters when parameters are needed\n" +
                name.getFilename() + ":" + name.getLineNum() + "\n");
                return false;
            }

            if(parameters.size() != 1){
                System.err.println("Semantic Error:\n" + "incorrect number of parameters\n" +
                name.getFilename() + ":" + name.getLineNum() + "\n");
                return false;
            }

            if(!(parameters.get(0) instanceof ExpressionNode)){
                System.err.println("Semantic Error:\n" + "invalid parameter passed\n" +
                name.getFilename() + ":" + name.getLineNum() + "\n");
                return false;
            } else {
                if (parameters.get(0) instanceof FunctionCallNode) {
                    FunctionCallNode func = (FunctionCallNode) parameters.get(0);
                    if(SymbolTable.getFunctionReturn(func.getFuncName()).equals("Void")){
                        System.err.println("Semantic Error:\n" + "can not print a function of type Void\n" +
                        name.getFilename() + ":" + name.getLineNum() + "\n");
                        return false;
                    }
                }
                return true;
            }
            
        } else if(name.getID().equals("concat")){
            ArrayList<ExpressionNode> parameters = params.paramsHelper();
            if(parameters == null) {
                System.err.println("Semantic Error:\n" + "passed in no parameters when parameters are needed\n" +
                name.getFilename() + ":" + name.getLineNum() + "\n");
                return false;
            }

            if(parameters.size() != 2){
                System.err.println("Semantic Error:\n" + "incorrect number of parameters\n" +
                name.getFilename() + ":" + name.getLineNum() + "\n");
                return false;
            }

            for(ExpressionNode param : parameters){
                if (param instanceof String_literalNode) {
                    continue;   
                }
                if (param instanceof FunctionCallNode) {
                    FunctionCallNode func = (FunctionCallNode) param;
                    if(!SymbolTable.getFunctionReturn(func.getFuncName()).equals("String") || !func.getFuncName().equals("concat")){
                        System.err.println("Semantic Error:\n" + "can not print a function that doesn't return a string\n" +
                        name.getFilename() + ":" + name.getLineNum() + "\n");
                        return false;
                    }
                }
                if (param instanceof IDNode) {
                    IDNode var = (IDNode) param;
                    if(!SymbolTable.getLocalSymTable().get(var.getID()).equals("String")){
                        System.err.println("Semantic Error:\n" + "can not print a non string variable\n" +
                        name.getFilename() + ":" + name.getLineNum() + "\n");
                        return false;
                    }
                }

            }

            return true;

        } else if (name.getID().equals("length")) {
            ArrayList<ExpressionNode> parameters = params.paramsHelper();
            if(parameters == null) {
                System.err.println("Semantic Error:\n" + "passed in no parameters when parameters are needed\n" +
                name.getFilename() + ":" + name.getLineNum() + "\n");
                return false;
            }

            if(parameters.size() != 1){
                System.err.println("Semantic Error:\n" + "incorrect number of parameters\n" +
                name.getFilename() + ":" + name.getLineNum() + "\n");
                return false;
            }

            ExpressionNode param = parameters.get(0);
            if (param instanceof String_literalNode) {
                return true;   
            }
            if (param instanceof FunctionCallNode) {
                FunctionCallNode func = (FunctionCallNode) param;
                if(!SymbolTable.getFunctionReturn(func.getFuncName()).equals("String") || !func.getFuncName().equals("concat")){
                    System.err.println("Semantic Error:\n" + "can not print a function that doesn't return a string\n" +
                    name.getFilename() + ":" + name.getLineNum() + "\n");
                    return false;
                }
            }
            if (param instanceof IDNode) {
                IDNode var = (IDNode) param;
                if(!SymbolTable.getLocalSymTable().get(var.getID()).equals("String")){
                    System.err.println("Semantic Error:\n" + "can not print a non string variable\n" +
                    name.getFilename() + ":" + name.getLineNum() + "\n");
                    return false;
                }
            }

            return true;
            
        } else {
            if(!SymbolTable.funcTypes.containsKey(name.getID())) {
                System.err.println("Semantic Error:\n" + "attempting to call function that hasn't been defined\n" +
                        name.getFilename() + ":" + name.getLineNum());
                return false;
            }
        }

        if(!SymbolTable.funcTypes.containsKey(name.getID()) && (!name.getID().equals("print") || !name.getID().equals("concat") || !name.getID().equals("length"))) {
            System.err.println("Semantic Error:\n" + "attempting to call function that hasn't been defined\n" +
                    name.getFilename() + ":" + name.getLineNum());
            return false;
        }

        //params from function call
        ArrayList<ExpressionNode> parameters = params.paramsHelper();
        //params from function declaration
        ArrayList<String> declaredParams= SymbolTable.getFunctionParameters(name.getID());
        if(parameters == null) {
            if(declaredParams.size() != 0) {
                System.err.println("Semantic Error:\n" + "passed in no parameters when parameters are needed\n" +
                    name.getFilename() + ":" + name.getLineNum());
                return false;
            }
            return true;
        }

        if(parameters.size() != declaredParams.size()) {
            System.err.println("Semantic Error:\n" + "function call has incorrect number of parameters\n" +
                    name.getFilename() + ":" + name.getLineNum());
            return false;
        }

        for(int i = 0; i < parameters.size(); i++) {
            if(parameters.get(i) instanceof BoolNode || parameters.get(i) instanceof RelopNode) {
                if(!declaredParams.get(i).equals("Boolean")) {
                    System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                    name.getFilename() + ":" + name.getLineNum());
                    return false;
                }
            } else if(parameters.get(i) instanceof MathopNode) {
                if(!declaredParams.get(i).equals(((MathopNode)parameters.get(i)).MathopType())) {
                    System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                    name.getFilename() + ":" + name.getLineNum());
                    return false;
                }
            } else if(parameters.get(i) instanceof String_literalNode) {
                if(!declaredParams.get(i).equals("String")) {
                    System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                    name.getFilename() + ":" + name.getLineNum());
                    return false;
                }
            } else {
                if(parameters.get(i) instanceof IDNode) {
                    if(!declaredParams.get(i).equals(SymbolTable.getLocalSymTable().get(((IDNode)parameters.get(i)).getID()))) {
                        System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                        name.getFilename() + ":" + name.getLineNum());
                        return false;
                    }
                } else if(parameters.get(i) instanceof FunctionCallNode) {
                    if(!declaredParams.get(i).equals(SymbolTable.getFunctionReturn(((FunctionCallNode)parameters.get(i)).getFuncName()))) {
                        System.err.println("Semantic Error:\n" + "function call has incorrect parameter type for parameter " + i + "\n" +
                        name.getFilename() + ":" + name.getLineNum());
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
                        name.getFilename() + ":" + name.getLineNum());
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void execute() {
        params.execute();
        name.execute();
        if(params.returnParams() != null) {
            for (int i = 0; i < SymbolTable.funcParamNames.size(); i++) {
                SymbolTable.fbodys.get(name.getID()).varValues.put(SymbolTable.funcParamNames.get(name.getID()).get(i), params.returnParams().get(i));
            }
        }
        SymbolTable.currentCalledFunc.push(name.getID());
        SymbolTable.fbodys.get(name.getID()).execute();
        SymbolTable.currentCalledFunc.pop();
        SymbolTable.fbodys.get(name.getID()).varValues = new HashMap<String, String>();
    }
}
