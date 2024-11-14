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
    
    if(errorFound){
      errorFound = false;
      return null;
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

  public static void handleNumber(BufferedReader readJott, String filename, char latestChar){
    try{
      String tokenString = latestChar + "";
      boolean digitSeen = false;
      boolean dotSeen = false;

      if (latestChar == '.')
        dotSeen = true;
      else
        digitSeen = true;

      while(true) {
        readJott.mark(1);
        latestChar = (char)readJott.read();
        if (digits.contains(latestChar)) { // if its a digit
          tokenString += latestChar;
          digitSeen = true;
        } else if (latestChar == '.') { // if it's a decimal
          if(!dotSeen) { // if its the first decimal you see
            tokenString += latestChar;
            dotSeen = true;
          } else {
            errorFound = true;
            System.err.println("Numbers may only have one decimal point:" + filename + ":" + lineCount); 
            readJott.reset(); 
            return;
          }
          
        } else { // if it's not a digit or decimal
          readJott.reset();
          break;
        }
      }
      if(!digitSeen) {
        errorFound = true;
        System.err.println("Standalone decimals must have a number afterwards:" + filename + ":" + lineCount);
        return;
      }

      if(latestChar == '\n') // If the number is at the end of the line
        lineCount++;

      readJott.reset();
      tokenizerOutput.add(new Token(tokenString, filename, lineCount, TokenType.NUMBER));
    } catch(IOException e){
      System.out.println(e);
    }
  }

  public static void handleIdKeyword(BufferedReader readJott, String filename, char latestChar){
    try{
      String tokenString = latestChar + "";
      while(true) {
        readJott.mark(1);
        latestChar = (char)readJott.read();
        if (letters.contains(latestChar) || digits.contains(latestChar)) // if it's a letter or digit
          tokenString += latestChar;
        else { // if it's not a letter or digit
          readJott.reset();
          break;
        }
      }
      if(latestChar == '\n') // If the id,keyword is at the end of the line
        lineCount++;
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
        return;
      }
      if(latestChar != '\"') {
      System.err.println("Semantic Error:\nStrings may only contain letters, numbers, or spaces\n"+ filename + ":" + lineCount);
      errorFound = true;
      return;
      }
      tokenizerOutput.add(new Token(tokenString, filename, lineCount, TokenType.STRING));
    } catch(IOException e){
      System.out.println(e);
    }
  }
}