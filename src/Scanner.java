import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;



public class Scanner{

    private char[] content;
    private int     estado;
    private int        pos;

    public static final String[] RESERVED_WORD = new String[] {"main","if","else","while","do","for","int","float","char"};
    public static final String MAIOR_QUE = ">";
    public static final String MENOR_QUE = "<";
    public static final String MAIOR_OU_IGUAL = ">=";
    public static final String MENOR_OU_IGUAL = "<=";
    public static final String SOMA = "+";
    public static final String SUBTRACAO = "-";
    public static final String MULTIPLICACAO = "*";
    public static final String DIVISAO = "/";
    public static final String ATRIBUICAO = "=";
    public static final String ABREPARENTESES = "(";
    public static final String FECHAPARENTESES = ")";
    public static final String ABRECHAVE = "{";
    public static final String FECHACHAVE = "}";
    public static final String VIRGULA = ",";
    public static final String PONTOEVIRGULA = ";";

    public Scanner(String filename) {
        try{
            String txtConteudo;            
            txtConteudo = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);            
            //System.out.println(txtConteudo);
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
                        value += currentChar;
                        token = new Token();
                        token.setType(Token.TK_ARITHMETIC_OPERATOR);
                        token.setDescription("Caracter Especial");
                        token.setText(value);                        
                        return token; 
                    }else if(isSingleQuotes(currentChar)){
                        estado = 10;
                        value += currentChar;
                        break;
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
                        setAsRelationalOperator(value, token);
                        //back();
                        return token;
                    }else if(!isEqualityOperator(currentChar) || !isRelationalOperator(currentChar)){                       
                        token = new Token();
                        setAsArithmeticOperator(value, token);
                        back();
                        return token;
                    }
                case 8:
                    if(isEqualityOperator(currentChar) || isRelationalOperator(currentChar)){
                        value += currentChar;
                        token = new Token();
                        setAsRelationalOperator(value, token);
                        //back();
                        return token;
                    }else if(isSpace(currentChar)){
                        value += currentChar;
                        token = new Token();
                        setAsRelationalOperator(value, token);
                        //back();
                        return token;
                    }
                case 9:
                    token = new Token();
                    setAsArithmeticOperator(value, token);
                    back();
                    return token;
                case 10:
                    if(isChar(currentChar) || isDigit(currentChar)){
                        estado = 11;
                        value += currentChar;
                        break;
                    }else{
                        throw new RuntimeException("Unrecognized CHAR");
                    }
                case 11:
                    if(isSingleQuotes(currentChar)){
                        value += currentChar;
                        token = new Token();
                        token.setType(Token.TK_CHAR);
                        token.setDescription("Tipo CHAR");
                        token.setText(value);                        ;
                        return token;
                    }else{
                        throw new RuntimeException("Unrecognized CHAR");
                    }

                    
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

    private Boolean isSingleQuotes(char c){
        return c == '\'';
    }

    private void setAsRelationalOperator(String value, Token token){
        if(value.equals(MAIOR_QUE)){
            token.setType(Token.TK_RELATIONAL_OPERATOR_MAIORQUE);
            token.setDescription("Operador Relacional Maior Que");
            token.setText(value);
        }
        else if(value.equals(MENOR_QUE)){
            token.setType(Token.TK_RELATIONAL_OPERATOR_MENORQUE);
            token.setDescription("Operador Relacional Menor Que");
            token.setText(value);
        }
        else if(value.equals(MAIOR_OU_IGUAL)){
            token.setType(Token.TK_RELATIONAL_OPERATOR_MAIOROUIGUAL);
            token.setDescription("Operador Relacional Maior ou Igual");
            token.setText(value);
        }
        else if(value.equals(MENOR_OU_IGUAL)){
            token.setType(Token.TK_RELATIONAL_OPERATOR_MENOROUIGUAL);
            token.setDescription("Operador Relacional Menor ou Igual");
            token.setText(value);
        }else{

        }
    }

    private void setAsArithmeticOperator(String value, Token token){
        if(value.equals(SOMA)){
            token.setType(Token.TK_ARITHMETIC_OPERATOR_SOMA);
            token.setDescription("Operador Aritmético Soma");
            token.setText(value);
        }
        else if(value.equals(SUBTRACAO)){
            token.setType(Token.TK_ARITHMETIC_OPERATOR_SUBTRACAO);
            token.setDescription("Operador Relacional Subtração");
            token.setText(value);
        }
        else if(value.equals(MULTIPLICACAO)){
            token.setType(Token.TK_ARITHMETIC_OPERATOR_MULTIPLICACAO);
            token.setDescription("Operador Relacional Multiplicação");
            token.setText(value);
        }
        else if(value.equals(DIVISAO)){
            token.setType(Token.TK_ARITHMETIC_OPERATOR_DIVISAO);
            token.setDescription("Operador Relacional Divisão");
            token.setText(value);
        }
        else if(value.equals(ATRIBUICAO)){
            token.setType(Token.TK_ARITHMETIC_OPERATOR_ATRIBUICAO);
            token.setDescription("Operador Relacional Atribuição");
            token.setText(value);
        }else{

        }
    }

    private void setAsSpecialCharacter(String value, Token token){
        if(value.equals(ABREPARENTESES)){
            token.setType(Token.SPECIAL_CHARACTER_ABREPARENTESES);
            token.setDescription("Caractere Especial Abre Parênteses");
            token.setText(value);
        }
        else if(value.equals(FECHAPARENTESES)){
            token.setType(Token.SPECIAL_CHARACTER_FECHAPARENTESES);
            token.setDescription("Caractere Especial Fecha Parênteses");
            token.setText(value);
        }
        else if(value.equals(ABRECHAVE)){
            token.setType(Token.SPECIAL_CHARACTER_ABRECHAVE);
            token.setDescription("Caractere Especial Abre Chave");
            token.setText(value);
        }
        else if(value.equals(FECHACHAVE)){
            token.setType(Token.SPECIAL_CHARACTER_FECHACHAVE);
            token.setDescription("Caractere Especial Fecha Chave");
            token.setText(value);
        }
        else if(value.equals(VIRGULA)){
            token.setType(Token.SPECIAL_CHARACTER_VIRGULA);
            token.setDescription("Caractere Especial Vírgula");
            token.setText(value);
        }
        else if(value.equals(PONTOEVIRGULA)){
            token.setType(Token.SPECIAL_CHARACTER_PONTOEVIRGULA);
            token.setDescription("Caractere Especial Ponto e Vírgula");
            token.setText(value);
        }else{

        }
    }


}
    

