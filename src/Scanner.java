import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;



public class Scanner{

    private char[] content;
    private int     estado;
    private int        pos;

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
        if(isEOF()){
            return null;
        }
        estado = 0;
        while(true){
            currentChar = nextChar();

            switch(estado){
                case 0:
                    if(isChar(currentChar)){
                        estado = 1;
                        break;
                    }else if(isDigit(currentChar)){
                        estado = 3;
                        break;

                    }else if(isSpace(currentChar)){
                        estado = 0;
                    }else if(isOperator(currentChar)){
                        estado = 5;
                    }else{
                        throw new RuntimeException("Unrecognized SYMBOL");
                    }
                    break;
                case 1:
                    if(isChar(currentChar) || isDigit(currentChar)){
                        estado = 1;
                    }else{
                        estado = 2;
                    }
                case 2:
                    Token token = new Token();
                    token.setType(Token.TK_IDENTIFIER);
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

    private Boolean isOperator(char c){
        return c == '>' || c == '<' || c == '=' || c == '!';
    }

    private Boolean isSpace(char c){
        return c== ' ' || c == '\t' || c == '\n' || c == '\r';
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
    

