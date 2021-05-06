/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

/**
 *
 * @author minci
 */
public class Token {
    public static final int TK_LETTER                               =0;
    public static final int TK_CHAR                                 =1;
    public static final int TK_IDENTIFIER                           =2;   
    public static final int TK_INT                                  =3;
    public static final int TK_FLOAT                                =4;
    public static final int TK_NUMBER                               =5;
    public static final int TK_ARITHMETIC_OPERATOR                  =6;
    public static final int TK_RESERVED_WORD                        =7;
    public static final int SPECIAL_CHARACTER                       =8; 
    public static final int TK_RELATIONAL_OPERATOR_IGUAL            =9;
    public static final int TK_RELATIONAL_OPERATOR_MAIORQUE         =10;
    public static final int TK_RELATIONAL_OPERATOR_MENORQUE         =11;
    public static final int TK_RELATIONAL_OPERATOR_MAIOROUIGUAL     =12;
    public static final int TK_RELATIONAL_OPERATOR_MENOROUIGUAL     =13;
    public static final int TK_ARITHMETIC_OPERATOR_SOMA             =14;
    public static final int TK_ARITHMETIC_OPERATOR_SUBTRACAO        =15;
    public static final int TK_ARITHMETIC_OPERATOR_MULTIPLICACAO    =16;
    public static final int TK_ARITHMETIC_OPERATOR_DIVISAO          =17;
    public static final int TK_ARITHMETIC_OPERATOR_ATRIBUICAO       =18;
    public static final int SPECIAL_CHARACTER_ABREPARENTESES        =19;
    public static final int SPECIAL_CHARACTER_FECHAPARENTESES       =20;
    public static final int SPECIAL_CHARACTER_ABRECHAVE             =21;
    public static final int SPECIAL_CHARACTER_FECHACHAVE            =22;
    public static final int SPECIAL_CHARACTER_VIRGULA               =23;
    public static final int SPECIAL_CHARACTER_PONTOEVIRGULA         =24;
    public static final int TK_RELATIONAL_OPERATOR_DIFERENTE        =25;
    public static final int TK_RESERVED_WORD_IF                        =26;
    public static final int TK_RESERVED_WORD_MAIN                      =27;
    public static final int TK_RESERVED_WORD_ELSE                      =28;
    public static final int TK_RESERVED_WORD_WHILE                     =29;
    public static final int TK_RESERVED_WORD_DO                        =30;
    public static final int TK_RESERVED_WORD_FOR                       =31;
    public static final int TK_RESERVED_WORD_INT                       =32;
    public static final int TK_RESERVED_WORD_FLOAT                     =33;
    public static final int TK_RESERVED_WORD_CHAR                      =34;
        

    private int     type;
    private String  text;
    private String  description;
    private int     line;
    private int     column;

    public Token(int type, String text, String description, int line, int column){
        super();
        this.type = type;
        this.text = text;
        this.description = description;
        this.line = line;
        this.column = column;
    }

    public Token(){
        super();
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    
    

    
    @Override
    public String toString(){
        return "Token [type : " + type + ", description : " +  description +", value : " + text + "line : "+ line +"column : "+ column +"]";
    }
}
