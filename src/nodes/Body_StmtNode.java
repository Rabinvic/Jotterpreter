package nodes;
import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

public interface Body_StmtNode  extends JottTree{
    public String convertToJott();
    public boolean validateTree();
    public void execute();

    public static Body_StmtNode parseBodyStmtNode(ArrayList<Token> tokens) {
        if(tokens.size() == 0) {
            System.err.print("Syntax Error:\n no tokens to parse\n");
            return null;
        }
        if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken().equals("If")){
            If_StmtNode tempIf = If_StmtNode.parseIfStmtNode(tokens);
            if(tempIf == null) {
                return null;
            }
            return tempIf;
        } else if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken().equals("While")) {
            WhileLoopNode tempWhile = WhileLoopNode.parseWhileLoopNode(tokens);
            if(tempWhile == null) {
                return null;
            }
            return tempWhile;
        } else if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && 
        Character.isLowerCase(tokens.get(0).getToken().charAt(0))) {
            AsmtNode tempAs = AsmtNode.parseAsmtNode(tokens);
            if(tempAs == null) {
                return null;
            }
            return tempAs;
        } else if(tokens.get(0).getTokenType() == TokenType.FC_HEADER) {
            FunctionCallNode temp =FunctionCallNode.parseFunctionCallNode(tokens);
            if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) {
                System.err.println("Syntax Error:\n missing semicolon\n" + tokens.get(0).getFilename() + ":" + 
                tokens.get(0).getLineNum() + "\n");
                return null;
            }
            tokens.remove(0);
            if(temp == null) {
                return null;
            }
            return temp;
        } else {
            System.err.println("Syntax Error:\n not a body statement\n" + tokens.get(0).getFilename() + ":" + 
            tokens.get(0).getLineNum() + "\n");
            return null;
        }
    }
}
