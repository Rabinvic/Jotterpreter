package nodes;
import java.util.ArrayList;

import provided.*;

public class If_StmtNode implements Body_StmtNode{
    private ExpressionNode expr;
    private BodyNode body;
    private ArrayList<ElseIfNode> elseIfs;
    private ElseNode els;

    public If_StmtNode(ExpressionNode expr, BodyNode body, ArrayList<ElseIfNode> elseIfs, ElseNode els){
        this.expr = expr;
        this.body = body;
        this.elseIfs = elseIfs;
        this.els = els;
    }

    public static If_StmtNode parseIfStmtNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }

        // If[< expr >]{< body >}< elseif_lst >*< else >

        // If
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD || !tokens.get(0).getToken().equals("If")) {
            System.err.println("Syntax Error:\n missing ''If'\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // [
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            System.err.println("Syntax Error:\n missing ''[' in If Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < expr >
        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);
        if (expr == null) {
            return null;
        }

        // ]
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            System.err.println("Syntax Error:\n missing '']' in If Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // {
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            System.err.println("Syntax Error:\n missing ''{' in If Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < body >
        BodyNode body = BodyNode.parseBodyNode(tokens);
        if (body == null) {
            return null;
        }

        // }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            System.err.println("Syntax Error:\n missing ''}' in If Statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
        tokens.remove(0);

        // < elseif_lst >
        ArrayList<ElseIfNode> elseIfs = new ArrayList<ElseIfNode>();
        while(!tokens.isEmpty() && tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken().equals("Elseif")) {
            //System.err.println("ElseIf found!!!");
            ElseIfNode elseIf = ElseIfNode.parseElseIfNode(tokens);
            if(elseIf == null) {
                return null;
            }
            elseIfs.add(elseIf);
        }

        // < else >
        ElseNode els = ElseNode.parseElseNode(tokens);
        if(els == null){
            return null;
        }

        return new If_StmtNode(expr, body, elseIfs, els);
    }

    public String convertToJott(){
        // If[< expr >]{< body >}< elseif_lst >*< else >

        String elseIfList = "";
        for(ElseIfNode elseIf : elseIfs) {
            elseIfList += elseIf.convertToJott();
        }
        return "If[" + expr.convertToJott() + "]{" + body.convertToJott() + "}" + elseIfList + els.convertToJott();
    }

    // presumably done
    public boolean validateTree() {
        if(!expr.validateTree() || !body.validateTree() || !els.validateTree()) {
            return false;
        }
        for(ElseIfNode elseIf: elseIfs ) {
            if(!elseIf.validateTree()) {
                return false;
            }
        }
        
        if(expr instanceof MathopNode || expr instanceof String_literalNode) {
            if(expr instanceof MathopNode){
                System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
                ((MathopNode)expr).getFilename() + ":" + ((MathopNode)expr).getLineNum());
            } else {
                System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
                ((String_literalNode)expr).getFilename() + ":" + ((String_literalNode)expr).getLineNum());
            }
            return false;
        } else if(expr instanceof OperandNode) {
            if(expr instanceof IDNode) {
                if(!SymbolTable.getLocalSymTable().get(((IDNode)expr).getID()).equals("Boolean")) {
                    System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
                    ((IDNode)expr).getFilename() + ":" + ((IDNode)expr).getLineNum());
                    return false;
                }
            } else if(expr instanceof FunctionCallNode) {
                if(!SymbolTable.getFunctionReturn(((FunctionCallNode)expr).getFuncName()).equals("Boolean")) {
                    System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
                    ((FunctionCallNode)expr).getFilename() + ":" + ((FunctionCallNode)expr).getLineNum());
                    return false;
                }
            } else {
                System.err.println("Semantic Error:\n" + "attempting to use non conditional statement as condition for while loop\n" +
                ((NumberNode)expr).getFilename() + ":" + ((NumberNode)expr).getLineNum());
                return false;
            }
        }
        return true;
    }

    // erm its self documenting code basically just read it -ETHAN
    public boolean guaranteesReturn() {
        boolean ifStmtReturns = false;
        boolean allElseIfsReturn = true;
        boolean elseStmtReturns = false;

        if (body.hasReturnStmt()) {
            ifStmtReturns = true;
        }

        if (elseIfs != null) {
            for (ElseIfNode elseIfNode : elseIfs) {
                if(!elseIfNode.guaranteesReturn()) {
                    allElseIfsReturn = false;
                }
            }  
        }
        
        if (els != null) {
            if(els.guaranteesReturn()) {
                elseStmtReturns = true;
            }
        }
        
        
        return ifStmtReturns && allElseIfsReturn && elseStmtReturns;
    }

    public boolean hasReturn() {
        if(!body.containsReturn()) {
            return false;
        }
        if(elseIfs.size() >= 1) {
            for(int i = 0; i < elseIfs.size(); i++) {
                if(!elseIfs.get(i).elseIfContainsReturn()) {
                    return false;
                }
            }
        }
        if(!els.ElseContainsReturn()) {
            return false;
        }
        return true;
    }
    public void execute(){
        expr.execute();

        if (SymbolTable.vals.get(expr).equals("True")) {
            body.execute();
        } else {
            for (ElseIfNode elseIf : elseIfs) {
                elseIf.execute();
                if (SymbolTable.vals.get(elseIf).equals("True")) {
                    return; // short circuit; don't check other elseIf's
                }
            }
            // niether the if nor any elseIf's were true.
            els.execute();
        }
    }
}
