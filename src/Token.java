public class Token {
    public static final int TK_LETTER               =0;
    public static final int TK_CHAR                 =1;
    public static final int TK_IDENTIFIER           =2;   
    public static final int TK_INT                  =3;
    public static final int TK_FLOAT                =4;
    public static final int TK_NUMBER               =5;
    public static final int TK_ARITHMETIC_OPERATOR  =6;
    public static final int TK_RELATIONAL_OPERATOR  =7;
    public static final int TK_RESERVED_WORD        =8;
    public static final int SPECIAL_CHARACTER       =9;    

    private int     type;
    private String  text;
    private String  description;

    public Token(int type, String text, String description){
        super();
        this.type = type;
        this.text = text;
        this.description = description;
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

    @Override
    public String toString(){
        return "Token [type : " + type + ", description : " +  description +", value : " + text + "]";
    }

    
}
