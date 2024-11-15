package nodes;
import java.util.ArrayList;

import provided.JottTree;
import provided.SymbolTable;
import provided.Token;
import provided.TokenType;

public class BodyNode implements JottTree{
    ArrayList<Body_StmtNode> bodyStmts;
    Return_StmtNode returnStmt;

    public BodyNode(ArrayList<Body_StmtNode> bodyStmts, Return_StmtNode returnStmt){
        this.bodyStmts = bodyStmts;
        this.returnStmt = returnStmt;
    }

    public static Boolean isbodyStartKeyword(Token t){
        return t.getToken().equals("If") || t.getToken().equals("While") ||
        t.getToken().equals("Return");
    }

    public static BodyNode parseBodyNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        // <body_stmt>⋆ <return_stmt>


        // <body_stmt>⋆
        ArrayList<Body_StmtNode> bodyStmts = new ArrayList<Body_StmtNode>();
        while(!tokens.isEmpty() && (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD || tokens.get(0).getTokenType() == TokenType.FC_HEADER) 
            && !tokens.get(0).getToken().equals("Return")) {
            Body_StmtNode bodyStmt = Body_StmtNode.parseBodyStmtNode(tokens);
            if(bodyStmt == null) {
                return null;
            }
            bodyStmts.add(bodyStmt);
        }

        // <return_stmt>
        Return_StmtNode returnStmt = Return_StmtNode.parseReturn_StmtNode(tokens);
        if(returnStmt == null) {
            return null;
        }
        return bodyStmts.isEmpty() ? new BodyNode(null, returnStmt) : new BodyNode(bodyStmts, returnStmt);
    }

    public String convertToJott() {
        String str = "";
        if(bodyStmts == null) {
            return returnStmt.convertToJott();
        }
        for(int i = 0; i < bodyStmts.size(); i++ ){
            str += bodyStmts.get(i).convertToJott();
            if(bodyStmts.get(i) instanceof FunctionCallNode){
                str += ";";
            }
        }
        str += returnStmt.convertToJott();
        return str;
    }

    public boolean validateTree() {
        if(bodyStmts != null){
            for (Body_StmtNode body_StmtNode : bodyStmts) {
                if (!body_StmtNode.validateTree()) {
                   return false;
                }
            }
        }
        if (!returnStmt.validateTree()) {
            return false;
        }
        if(returnStmt.notFinished()) {
            if (bodyStmts == null) {
                System.err.println("Semantic Error:\n" +SymbolTable.currentFunc + "missing a return statement\n" +
                                       SymbolTable.funcFile + ":" + SymbolTable.funcLine + "\n");
                return false;
            }
            for(int i = 0; i < bodyStmts.size(); i++) {
                if(bodyStmts.get(i) instanceof If_StmtNode) {
                    if(!((If_StmtNode)bodyStmts.get(i)).hasReturn()) {
                        System.err.println("Semantic Error:\n" +SymbolTable.currentFunc + "missing a return statement\n" +
                                       SymbolTable.funcFile + ":" + SymbolTable.funcLine + "\n");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean hasReturnStmt() {
        if (bodyStmts == null) {
            return false;
        }

        if (returnStmt.exp != null) {
            return true;
        }
        // first just check all statements in it
        for (Body_StmtNode body_StmtNode : bodyStmts) {
            if(body_StmtNode instanceof Return_StmtNode) {
                return true;
            }
        }

        // then check that any if statement that is found guarantees a return in all cases
        for (Body_StmtNode body_StmtNode : bodyStmts) {
            if(body_StmtNode instanceof If_StmtNode) {
                if (((If_StmtNode)body_StmtNode).guaranteesReturn()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean containsReturn() {
        if(returnStmt.returnExists()) {
            return true;
        } else {
            return false;
        }
    }

    public void execute() {
        
    }
}
