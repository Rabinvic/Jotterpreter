package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;
import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException; 

public class JottTokenizer {

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename){
      ArrayList<Token> tokenizerOutput = new ArrayList<Token>();
      int lineCount = 1;
      try{
        FileReader JottFile = new FileReader(filename);
        BufferedReader readJott = new  BufferedReader(JottFile);
        char first;
        while((first = (char) readJott.read()) != -1){
          if(first == ','){
            Token commaToken = new Token(",", filename, lineCount, TokenType.COMMA);
          }else if(first == '\n'){
            lineCount++;
          }else if(first == '#'){
            while(((char)readJott.read()) != '\n'){
            }
            lineCount++;
          }
        }
      }catch(IOException e){
        System.out.println(e);
      }
      
		return null;
	}
  public static void start(BufferedReader readJott){
    
    
  }
}