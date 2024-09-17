package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author Analucia Macias Marin
 * @author Ethan Battaglia
 * @author Victor Rabinovich
 * @author Alex Roberts
 **/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException; 

public class JottTokenizer {

    private static ArrayList<Token> tokenizerOutput;
    private static int lineCount;
    private static Set<Character> letters = new HashSet<>();
    private static Set<Character> digits = new HashSet<>();
    private static Boolean errorFound = false;
    // Fill up the alphabet and digits sets
    static {
      for(char c = 'a'; c <= 'z'; c++)
        letters.add(c);
      for(char c = 'A'; c <= 'Z'; c++)
        letters.add(c);
      for(char c = '0'; c <= '9'; c++)
        digits.add(c);
    }
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
        int firstInt;
        char first;
        FileReader JottFile = new FileReader(filename);
        BufferedReader readJott = new  BufferedReader(JottFile);
        while(((firstInt = readJott.read()) != -1) && !errorFound){
          first = (char)firstInt;
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
          } else if(first == ';'){
            oneCharacter(";", TokenType.SEMICOLON, filename);
          } else if(first == '/'|| first == '+' || first == '-' || first == '*'){
            oneCharacter(first + "", TokenType.MATH_OP, filename);
          } else if(first == ':'){
            readJott.mark(1);
            if ((char)readJott.read() == ':') {
              tokenizerOutput.add(new Token("::", filename, lineCount,  TokenType.FC_HEADER));
            } else {
              readJott.reset();
              oneCharacter(":", TokenType.COLON, filename);
            }
          } else if(first == '=') {
            readJott.mark(1);
            if((char)readJott.read() == '=') {
              tokenizerOutput.add(new Token("==", filename, lineCount, TokenType.REL_OP));
            } else {
              readJott.reset();
              oneCharacter("=", TokenType.ASSIGN, filename);
            }
          } else if(first == '!') {
            if((char)readJott.read() == '=') {
              tokenizerOutput.add(new Token("!=", filename, lineCount, TokenType.REL_OP));
            } else {
              errorFound = true;
              System.err.println("Syntax Error\nInvalid token \"!\". \"!\" expects following \"=\".\n" + filename + ":" + lineCount + "\n");
            }
          } else if(first == '<' || first == '>') {
            readJott.mark(1);
            if((char)readJott.read() == '=') {
              tokenizerOutput.add(new Token(first + "=", filename, lineCount, TokenType.REL_OP));
            } else {
              readJott.reset();
              oneCharacter(first + "", TokenType.REL_OP, filename);
            }
          } else if(first == '"'){
            handleString(readJott, filename, first);
          } else if(digits.contains(first) || first == '.'){
            handleNumber(readJott, filename, first);
          } else if(letters.contains(first)){
            handleIdKeyword(readJott, filename, first);
          }
        }
        readJott.close();
      }catch(IOException e){
        System.out.println(e);
      }
    
    if(errorFound) return null;
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

  public static void handleNumber(BufferedReader readJott, String filename, char latestChar){
    try{
      String tokenString = latestChar + "";
      if(latestChar != '.'){ // If the first char is a digit, continually read until a non-digit is found
        do {
          latestChar = (char)readJott.read();
          tokenString += latestChar;
        } while(digits.contains(latestChar));
      }
      if(latestChar == '.'){ // If the number starts with or has a decimal point
        do {
        latestChar = (char)readJott.read();
        tokenString += latestChar;
        } while(digits.contains(latestChar));
      }
      if(latestChar == '\n') // If the number is at the end of the line
        lineCount++;
      tokenString = tokenString.substring(0, tokenString.length() - 1);
      tokenizerOutput.add(new Token(tokenString, filename, lineCount, TokenType.NUMBER));
    } catch(IOException e){
      System.out.println(e);
    }
  }

  public static void handleIdKeyword(BufferedReader readJott, String filename, char latestChar){
    try{
      String tokenString = latestChar + "";
      do { // Continually read until a non-letter or non-digit is found
        latestChar = (char)readJott.read();
        tokenString += latestChar;
      } while(letters.contains(latestChar) || digits.contains(latestChar)); 
      if(latestChar == '\n') // If the id,keyword is at the end of the line
        lineCount++;
      tokenString = tokenString.substring(0, tokenString.length() - 1);
      tokenizerOutput.add(new Token(tokenString, filename, lineCount, TokenType.ID_KEYWORD));
    } catch(IOException e){
      System.out.println(e);
    }
  }

  public static void handleString(BufferedReader readJott, String filename, char latestChar){
    try{
      String tokenString = latestChar + "";
      do{
        latestChar = (char)readJott.read();
        tokenString += latestChar;
      } while((digits.contains(latestChar) || letters.contains(latestChar) || latestChar == ' ' || latestChar == '\t') && latestChar != '\n' && latestChar != '"'); //keeps looking until new line or " are seen, adds all characters seen between " "
      if (latestChar == '\n') { //if new line is found it is invalid syntax and an error is printed
        errorFound = true;
        System.err.println("Strings must be one line, expecting \" at end of line \n" + filename + ":" + lineCount);  
              
      } else {
        tokenizerOutput.add(new Token(tokenString, filename, lineCount, TokenType.STRING)); 
      }
    } catch(IOException e){
      System.out.println(e);
    }
  }
}