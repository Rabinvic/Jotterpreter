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

    private static ArrayList<Token> tokenizerOutput;
    private static int lineCount;
	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename){
      try{
        tokenizerOutput = new ArrayList<Token>();
        lineCount = 1;
        char first;
        FileReader JottFile = new FileReader(filename);
        BufferedReader readJott = new  BufferedReader(JottFile);
        while((first = (char)readJott.read()) != -1){
          if(first == ','){
            oneCharacter(",", TokenType.COMMA, filename);
          }else if(first == '\n'){
            lineCount++;
          }else if(first == '#'){
            handleNewLine(readJott);
          } else if(first == '['){
            oneCharacter("[", TokenType.L_BRACKET, filename);
          } else if(first == ']'){
            oneCharacter("]", TokenType.R_BRACKET, filename);
          } else if(first == '{'){
            oneCharacter("{", TokenType.L_BRACE, filename);
          } else if(first == '}'){
            oneCharacter("}", TokenType.R_BRACE, filename);
          }
        }
      }catch(IOException e){
        System.out.println(e);
      }
      
		return tokenizerOutput;
	}
  public static void oneCharacter(String tokenString, TokenType theTokenType, String filename){
    tokenizerOutput.add(new Token(tokenString, filename, lineCount, theTokenType));
  }

  public static void handleNewLine(BufferedReader readJott){
    try{
      while(((char)readJott.read()) != '\n'){
      }
      lineCount++;
    } catch(IOException e){
      System.out.println(e);
    }
  }
}