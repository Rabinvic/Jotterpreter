package nodes;
/**
 * @author Alex Roberts
**/

import java.util.ArrayList;
import provided.*;


public class FunctionDefNode implements JottTree {
    private IDNode id;
    private FunctionDefParamsNode fdp;
    private FunctionReturnNode fr;
    private FBodyNode fbody;

    public FunctionDefNode(IDNode id, FunctionDefParamsNode fdp, FunctionReturnNode fr, FBodyNode fbody) {
        this.id = id;
        this.fdp = fdp;
        this.fr = fr;
        this.fbody = fbody;
    }

    public String getName() {
        return id.getID();
    }

    public static FunctionDefNode parseFunctionDefNode(ArrayList<Token> tokens) {
        if(0==tokens.size()) {
            System.err.println("Syntax Error:\n No Tokens");
            return null;
        }
        
        // Def <id> [ <func_def_params> ] : <function_return> { <f_body> }

        // Def
        //made change to allow def and Def
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD || (!tokens.get(0).getToken().equals("Def") || !tokens.get(0).getToken().equals("def"))) {
            System.err.println("Syntax Error:\n Missing Keyword 'Def' in Function Definition\n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum());
            return null;
        }
        tokens.remove(0);

        // <id>
        IDNode id = IDNode.parseIDNode(tokens);
        if(id == null) {
            return null;
        } 
        
        // [
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            System.err.println("Syntax Error:\n Missing Left Bracket in Function Definition\n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum());
            return null;
        }
        tokens.remove(0);

        // <func_def_params>
        FunctionDefParamsNode fdp = FunctionDefParamsNode.parseParamsNode(tokens);
        if(fdp == null) {
            return null;
        }

        // ]
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            System.err.println("Syntax Error:\n Missing Right Bracket in Function Definition\n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum());
            return null;
        }
        tokens.remove(0);

        // :
        if(tokens.get(0).getTokenType() != TokenType.COLON) {
            System.err.println("Syntax Error:\n Missing Colon in Function Definition\n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum());
            return null;
        }
        tokens.remove(0);

        // <function_return>
        FunctionReturnNode fr = FunctionReturnNode.parseFunctionReturnNode(tokens);
        if(fr == null) {
            return null;
        }

        // {
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            System.err.println("Syntax Error:\n Missing Left Brace\n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum());
            return null;
        }
        tokens.remove(0);

        // <f_body>
        FBodyNode fbody = FBodyNode.parseFBodyNode(tokens);
        if(fbody == null) {
            return null;
        }

        // }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE || tokens.isEmpty()) {
            System.err.println("Syntax Error:\n Missing Right Brace in Function Definition\n" + tokens.get(0).getFilename() + 
            ":" + tokens.get(0).getLineNum());
            return null;
        }
        tokens.remove(0);

        return new FunctionDefNode(id, fdp, fr, fbody);
    }

    public String convertToJott() {
        String s = "Def ";
        s += this.id.convertToJott();
        s += "[";
        s += this.fdp.convertToJott();
        s += "] :";
        s += this.fr.convertToJott();
        s += "{";
        s += this.fbody.convertToJott();
        s += "}";
        return s;
    }

    // presumably done
    public boolean validateTree() {
        String name = id.getID();
        if(name.equals("print")) {
            System.err.println("Semantic Error:\nFunction name overlaps builtin function 'print'.\n"+id.getFilename()+":"+id.getLineNum());
            return false;
        }
        if(name.equals("concat")) {
            System.err.println("Semantic Error:\nFunction name overlaps builtin function 'concat'.\n"+id.getFilename()+":"+id.getLineNum());
            return false;
        }
        if(name.equals("length")) {
            System.err.println("Semantic Error:\nFunction name overlaps builtin function 'length'.\n"+id.getFilename()+":"+id.getLineNum());
            return false;
        }
        if(SymbolTable.funcTypes.containsKey(name)) {
            System.err.println("Semantic Error:\nFunction already defined.\n"+id.getFilename()+":"+id.getLineNum());
            return false;
        }
        if(name.equals("main")) {
            if(!fdp.convertToJott().equals("")) {
                System.err.println("Semantic Error:\nMain has parameters.\n"+id.getFilename()+":"+id.getLineNum());
                return false;
            }
            if(!fr.convertToJott().equals("Void")) {
                System.err.println("Semantic Error:\nMain has a return type other than 'Void'.\n"+id.getFilename()+":"+id.getLineNum());
                return false;
            }
        }
        ArrayList<String> paramTypes = new ArrayList<>();
        if (fdp != null && fdp.id != null) {
            paramTypes.add(fdp.type.convertToJott());
            if (fdp.paramTs != null) {
                for (FunctionDefParamsTNode paramT : fdp.paramTs) {
                    paramTypes.add(paramT.type.convertToJott());
                }
            }
        }
        SymbolTable.addFunction(name, fr.convertToJott(), paramTypes, this.id.getFilename(), this.id.getLineNum());

        if(!fdp.validateTree() || !fr.validateTree() || !fbody.validateTree()) {
            return false;
        }
        return true;
    }

    public void execute() {

    }
}