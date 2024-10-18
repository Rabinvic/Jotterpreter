package nodes;
import java.util.ArrayList;

import provided.JottTree;
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
        
        if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken().equals("Return")) {
            Return_StmtNode returnStmt = Return_StmtNode.parseReturn_StmtNode(tokens);
            if(returnStmt == null) {
                return null;
            }
            return new BodyNode(null, returnStmt);
        }

        ArrayList<Body_StmtNode> bodyStmts = new ArrayList<Body_StmtNode>();
        while((tokens.get(0).getTokenType() == TokenType.ID_KEYWORD || tokens.get(0).getTokenType() == TokenType.FC_HEADER) 
            && !tokens.get(0).getToken().equals("Return")) {
            Body_StmtNode bodyStmt = Body_StmtNode.parseBodyStmtNode(tokens);
            if(bodyStmt == null) {
                return null;
            }
            bodyStmts.add(bodyStmt);
        }

        Return_StmtNode returnStmt = Return_StmtNode.parseReturn_StmtNode(tokens);
        if(returnStmt == null) {
            return null;
        }
        return new BodyNode(bodyStmts, returnStmt); 
    }

    public String convertToJott() {
        String str = "";
        if(bodyStmts == null) {
            return returnStmt.convertToJott();
        }
        for(int i = 0; i < bodyStmts.size(); i++ ){
            str += bodyStmts.get(i).convertToJott();
        }
        str += returnStmt.convertToJott();
        return str;
    }

    public boolean validateTree() {
        return true;
    }

    public void execute() {
        
    }
}
