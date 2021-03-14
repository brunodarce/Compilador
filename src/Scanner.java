import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;



public class Scanner{

    private char[] content;
    private int     estado;
    private int        pos;

    public static final String[] RESERVED_WORD = new String[] {"main","if","else","while","do","for","int","float","char"};

    public Scanner(String filename) {
        try{
            String txtConteudo;            
            txtConteudo = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);            
            System.out.println(txtConteudo);
            content = txtConteudo.toCharArray();

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public Token nextToken(){
        char currentChar;
        String value = "";
        Token token;
        if(isEOF()){
            return null;
        }
        estado = 0;
        while(true){
            currentChar = nextChar(); 
            switch(estado){
                case 0:
                    if(isChar(currentChar)){
                        value += currentChar;
                        estado = 1;
                        break;
                    }else if(isDigit(currentChar)){
                        estado = 3;
                        value += currentChar;
                        break;
                    }else if(isSpace(currentChar)){
                        estado = 0;
                    }else if(isEqualityOperator(currentChar)){
                        estado = 7;
                        value += currentChar;
                        break;
                    }else if(isRelationalOperator(currentChar)){
                        estado = 8;
                        value += currentChar;
                        break;                    
                    }else if(isArithmeticOperator(currentChar)){
                        estado = 9;
                        value += currentChar;
                        break;                     
                    }else if(isSpecialCharacter(currentChar)){
                        token = new Token();
                        token.setType(Token.TK_ARITHMETIC_OPERATOR);
                        token.setDescription("Caracter Especial");
                        token.setText(value);                        
                        return token; 
                    }else{
                        throw new RuntimeException("Unrecognized SYMBOL");
                    }
                    break;
                case 1:
                    if(isChar(currentChar) || isDigit(currentChar)){
                        estado = 1;
                        value += currentChar;
                        break;
                    }else{
                        estado = 2;
                    }
                case 2:
                    back();
                    if(Arrays.asList(RESERVED_WORD).contains(value)){
                        token = new Token();
                        token.setType(Token.TK_RESERVED_WORD);
                        token.setDescription("Palavra Reservada");
                        token.setText(value);
                        return token;
                    }else{
                        token = new Token();
                        token.setType(Token.TK_IDENTIFIER);
                        token.setDescription("Identificador");
                        token.setText(value);
                        return token;
                    }
                    
                case 3:
                    if(isDigit(currentChar)){
                        estado = 3;
                        value += currentChar;
                        break;
                    }else if(!isChar(currentChar) && !isDot(currentChar)){
                        estado = 4;
                    }else if(isDot(currentChar)){
                        estado = 5;
                        value += currentChar;
                        break;
                    }else{
                        throw new RuntimeException("Unrecognized NUMBER");
                    }
                    break;
                case 4:
                    token = new Token();
                    token.setType(Token.TK_INT);
                    token.setDescription("Inteiro");
                    token.setText(value);
                    back();
                    return token;
                case 5:
                    if(isDigit(currentChar)){
                        estado = 5;
                        value += currentChar;
                        break;
                    }else if(!isChar(currentChar)){
                        estado = 6;
                    }
                case 6:
                    token = new Token();
                    token.setType(Token.TK_FLOAT);
                    token.setDescription("Float");
                    token.setText(value);
                    back();
                    return token;
                case 7:
                    if(isEqualityOperator(currentChar) || isRelationalOperator(currentChar)){
                        value += currentChar;
                        token = new Token();
                        token.setType(Token.TK_RELATIONAL_OPERATOR);
                        token.setDescription("Operador Relacional");
                        token.setText(value);
                        //back();
                        return token;
                    }else if(!isEqualityOperator(currentChar) || !isRelationalOperator(currentChar)){                       
                        token = new Token();
                        token.setType(Token.TK_ARITHMETIC_OPERATOR);
                        token.setDescription("Operador Artimético");
                        token.setText(value);
                        back();
                        return token;
                    }
                case 8:
                    if(isEqualityOperator(currentChar) || isRelationalOperator(currentChar)){
                        value += currentChar;
                        token = new Token();
                        token.setType(Token.TK_RELATIONAL_OPERATOR);
                        token.setDescription("Operador Relacional");
                        token.setText(value);
                        //back();
                        return token;
                    }else if(isSpace(currentChar)){
                        value += currentChar;
                        token = new Token();
                        token.setType(Token.TK_RELATIONAL_OPERATOR);
                        token.setDescription("Operador Relacional");
                        token.setText(value);
                        //back();
                        return token;
                    }
                case 9:
                    token = new Token();
                    token.setType(Token.TK_ARITHMETIC_OPERATOR);
                    token.setDescription("Operador Artimético");
                    token.setText(value);
                    back();
                    return token;

                    
            }
        }

    }

    private Boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    private Boolean isChar(char c){
       return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private Boolean isRelationalOperator(char c){
        return c == '>' || c == '<' || c == '!';
    }

    private Boolean isArithmeticOperator(char c){
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '=';
    }

    private Boolean isEqualityOperator(char c){
        return c == '=';
    }
    
    private Boolean isSpecialCharacter(char c){
        return c == '(' || c == ')' || c == '{' || c == '}' || c == ',' || c == ';';
    }

    private Boolean isSpace(char c){
        return c== ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private Boolean isDot(char c){
        return c== '.';
    }

    private char nextChar(){
        return content[pos++];
    }

    private void back(){
        pos--;
    }

    private Boolean isEOF(){
        return pos == content.length;
    }


}
    

