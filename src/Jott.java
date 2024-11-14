import java.util.ArrayList;

import provided.JottParser;
import provided.JottTokenizer;
import provided.Token;
import provided.JottTree;

public class Jott {
    public static void main(String[] args) throws Exception {
        if(args.length > 1){
            System.err.println("Invalid Args: Only Jott file required");
            return;
        }

        String JottFile = args[0];

        if (!JottFile.contains("jott")) {
            System.err.println("Invalid Args: Not a Jott file");
            return;
        }

        ArrayList<Token> tokens = JottTokenizer.tokenize(JottFile);

        if(tokens==null){
            return;
        }

        JottTree tokenTree = JottParser.parse(tokens);

        if (tokenTree == null){
            return;
        }

        if(!tokenTree.validateTree()){
            return;
        }
        
    }
}
