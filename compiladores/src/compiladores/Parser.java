/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

public class Parser {

    private Scanner scanner;
    private Token token; //token atual
    boolean aux = false;
    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public void nextToken() {
        token = scanner.nextToken();
    }

    //<programa>       ::=   int main"("")" <bloco>
    public void program() {
        nextToken();
        if (token.getType() == 32) { //TK_RESERVED_WORD_INT
            nextToken();
            if (token.getType() == 27) { //TK_RESERVED_WORD_MAIN
                nextToken();
                if (token.getType() == 19) {// (SPECIAL_CHARACTER_ABREPARENTESES
                    nextToken();
                    if (token.getType() == 20) {// )SPECIAL_CHARACTER_FECHAPARENTESES
                        nextToken();
                        bloco();
                        nextToken();
                    } else {
                        System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: PROGRAM invalid.");
                        System.out.println("" + token.getText() + " invalid");
                        System.out.println("SPECIAL_CHARACTER_FECHAPARENTESES expected");
                        System.exit(0);
                    }
                } else {
                    System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: PROGRAM invalid.");
                    System.out.println("" + token.getText() + " invalid");
                    System.out.println("SPECIAL_CHARACTER_ABREPARENTESES expected");
                    System.exit(0);
                }
            } else {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: PROGRAM invalid.");
                System.out.println("" + token.getText() + " invalid");
                System.out.println("TK_RESERVED_WORD_MAIN expected");
                System.exit(0);
            }
        } else {
            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: PROGRAM invalid.");
            System.out.println("" + token.getText() + " invalid");
            System.out.println("TK_RESERVED_WORD_INT expected");
            System.exit(0);
            throw new SyntaxException("Tipo INT esperado na declaração do método.");
        }
    }

    //<bloco>          ::=   “{“ {<decl_var>}* {<comando>}* “}”
    public void bloco() {
        if (token.getType() == 21) // { SPECIAL_CHARACTER_ABRECHAVE
        {
            nextToken();
            while (decl_var() || comando()) //verificar
            {
                if(!aux)
                {
                    nextToken();
                }
                aux = false; //verificar
            }
            if (token.getType() == 22) // } SPECIAL_CHARACTER_FECHACHAVE
            { //FIM DO CODIGO
                System.out.println("Good Job!");
                System.exit(0);
            } else {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: BLOCO inválido.");
                System.out.println("" + token.getText() + " invalid");
                System.out.println("SPECIAL_CHARACTER_FECHACHAVE expected");
                System.exit(1);
            }
        } else {
            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: BLOCO inválido.");
            System.out.println("" + token.getText() + " invalid");
            System.out.println("SPECIAL_CHARACTER_ABRECHAVE expected");
            System.exit(1);
        }
    }

    public void decl_var_aux() {// int x,y,z
        if (token.getType() == 1) //TK_IDENTIFIER
        {
            nextToken();
            if (token.getType() == 23) //SPECIAL_CHARACTER_VIRGULA
            {
                nextToken();
                decl_var_aux(); //RECURSÃO PARA {,<id>}*
            }
        }
    }
    
    //<decl_var> ::= <tipo> <id> {,<id>}* ";"
    public boolean decl_var() {
        nextToken();
        if (token.getType() == 32 || token.getType() == 33 || token.getType() == 34) //TK_RESERVED_WORD INT FLOAT CHAR
        {
            nextToken();
            //int x; ou int x,y,z;
            if (token.getType() == 2) //TK_IDENTIFIER
            {
                nextToken();
                if(token.getType() == 23) //SPECIAL_CHARACTER_VIRGULA
                {
                    nextToken();
                    decl_var_aux();//PARA {,<id>}*
                    //nextToken();
                    if (token.getType() == 24) {//SPECIAL_CHARACTER_PONTOEVIRGULA
                        nextToken();
                    } else {
                        System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: DECLARAÇÃO DE VARIAVEL inválida.");
                        System.out.println("" + token.getText() + " invalid");
                        System.out.println("SPECIAL_CHARACTER_PONTOEVIRGULA expected");
                        System.exit(0);
                    }
                }
                if(token.getType() == 24)//SPECIAL_CHARACTER_PONTOEVIRGULA
                {
                    return true; // FIM DA DECLARACAO
                }
                //ADICIONANDO A MAIS NA GRAMATICA - DECLARACAO COM ATRIBUICAO (AINDA IMPLEMENTANDO)
                else if(token.getType() == 18)//TK_ARITHMETIC_OPERATOR_ATRIBUICAO
                {
                    nextToken();
                    if (token.getType() == 1) //TK_IDENTIFIER
                    {
                        nextToken();
                        if (token.getType() == 24) //SPECIAL_CHARACTER_PONTOEVIRGULA
                        {
                            nextToken();
                        } 
                        else 
                        {
                            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: DECLARAÇÃO DE VARIAVEL inválida.");
                            System.out.println("" + token.getText() + " invalid");
                            System.out.println("SPECIAL_CHARACTER_PONTOEVIRGULA expected");
                            System.exit(0);
                        }
                    } 
                    else 
                    {
                        System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: DECLARAÇÃO DE VARIAVEL inválida.");
                        System.out.println("" + token.getText() + " invalid");
                        System.out.println("TK_IDENTIFIER expected");
                        System.exit(0);
                    }
                }
            }else {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: DECLARAÇÃO DE VARIAVEL inválida.");
                System.out.println("" + token.getText() + " invalid");
                System.out.println("TK_IDENTIFIER expected");
                System.exit(0);
            }
        }else {
            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: TK_RESERVED_WORD expected.");
            System.out.println("" + token.getText() + " invalid");
            System.out.println("TK_RESERVED_WORD expected");
            System.exit(0);
        }
        return false;
    }
    
    //<tipo> ::= int | float | char
    public boolean tipo()
    {
        return (token.getType() == 32 || token.getType() == 33 ||token.getType() == 34);
        //TK_RESERVED_WORD_INT TK_RESERVED_WORD_FLOAT TK_RESERVED_WORD_CHAR
    }

    //<comando>        ::=   <comando_básico> | <iteração> | if "("<expr_relacional>")" <comando> {else <comando>}?
    public boolean comando() {
        if(comando_basico() || iteracao())
        {
            return true;
        }
        if(token.getType() == 26) //TK_RESERVED_WORD_IF
        {
            nextToken();
            if(token.getType() == 19)//SPECIAL_CHARACTER_ABREPARENTESES
            {
                nextToken();
                expr_relacional();
                if(token.getType() == 20)//SPECIAL_CHARACTER_FECHAPARENTESES
                {
                    nextToken();
                    if(comando())
                    {
                        nextToken();
                        if(token.getType() == 28)//TK_RESERVED_WORD_ELSE
                        {
                            nextToken();
                            if(!comando())
                            {
                                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: COMANDO invalid.");
                                System.out.println("" + token.getText() + " invalid");
                                System.out.println("COMANDO expected");
                                System.exit(0);
                            }
                            return true;
                        }
                        aux = true;
                        return true;
                    }
                    else
                    {
                        System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: COMANDO invalid.");
                        System.out.println("" + token.getText() + " invalid");
                        System.out.println("COMANDO expected");
                        System.exit(0);
                    }
                }
                else
                {
                    System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: INTERATOR invalid.");
                    System.out.println("" + token.getText() + " invalid");
                    System.out.println("SPECIAL_CHARACTER_FECHAPARENTESES expected");
                    System.exit(0);
                }
            }
            else
            {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: INTERATOR invalid.");
                System.out.println("" + token.getText() + " invalid");
                System.out.println("SPECIAL_CHARACTER_ABREPARENTESES expected");
                System.exit(0);
            }
        }
        return false;
    }

    //<comando_básico> ::=   <atribuição> | <bloco>
    private boolean comando_basico() {
        if(atribuicao())
        {
            return true;
        }
        else if(token.getType() == 29) //SPECIAL_CHARACTER_ABRECHAVE
        {
            bloco();
            return true;
        }
        else
        {
            return false;
        }
    }

    //<iteração>       ::=   while "("<expr_relacional>")" <comando> | do <comando> while "("<expr_relacional>")"";"
    private boolean iteracao() 
    {
        //PARA O WHILE
        if(token.getType() == 29) //TK_RESERVED_WORD_WHILE
        {
            nextToken();
            if(token.getType() == 19)//SPECIAL_CHARACTER_ABREPARENTESES
            {
                nextToken();
                expr_relacional();
                if(token.getType() == 20)//SPECIAL_CHARACTER_FECHAPARENTESES
                {
                    nextToken();
                    return comando();
                }
                else
                {
                    System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: INTERATOR invalid.");
                    System.out.println("" + token.getText() + " invalid");
                    System.out.println("SPECIAL_CHARACTER_FECHAPARENTESES expected");
                    System.exit(0);
                }
            }
            else
            {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: INTERATOR invalid.");
                System.out.println("" + token.getText() + " invalid");
                System.out.println("SPECIAL_CHARACTER_ABREPARENTESES expected");
                System.exit(0);
            }
        }
        //PARA O DO WHILE
        else if(token.getType() == 30) //TK_RESERVED_WORD_DO
        {
            nextToken();
            if(comando())
            {
                if(token.getType() == 29) //TK_RESERVED_WORD_WHILE
                {
                    nextToken();
                    if(token.getType() == 19)//SPECIAL_CHARACTER_ABREPARENTESES
                    {
                        nextToken();
                        expr_relacional();
                        if(token.getType() == 20)//SPECIAL_CHARACTER_FECHAPARENTESES
                        {
                            nextToken();//EM DO WHILE NÃO HÁ COMANDO NESTES PARENTESES
                            if(token.getType() == 24) //SPECIAL_CHARACTER_PONTOEVIRGULA
                            {
                                return true; //COMANDO DO WHILE OK
                            }
                            else
                            {
                                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: INTERATOR invalid.");
                                System.out.println("" + token.getText() + " invalid");
                                System.out.println("SPECIAL_CHARACTER_PONTOEVIRGULA expected");
                                System.exit(0);
                            }
                        }
                        else
                        {
                            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: INTERATOR invalid.");
                            System.out.println("" + token.getText() + " invalid");
                            System.out.println("SPECIAL_CHARACTER_FECHAPARENTESES expected");
                            System.exit(0);
                        }
                    }
                    else
                    {
                        System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: INTERATOR invalid.");
                        System.out.println("" + token.getText() + " invalid");
                        System.out.println("SPECIAL_CHARACTER_ABREPARENTESES expected");
                        System.exit(0);
                    }
                }
                else
                {
                    System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: INTERATOR invalid.");
                    System.out.println("" + token.getText() + " invalid");
                    System.out.println("TK_RESERVED_WORD_WHILE expected");
                    System.exit(0);
                }
            }
            else
            {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: INTERATOR invalid.");
                System.out.println("" + token.getText() + " invalid");
                System.out.println("COMANDO FOR DO WHILE expected");
                System.exit(0);
            }
        }
        return false;
    }

    //<atribuição>     ::=   <id> "=" <expr_arit> ";"
    private boolean atribuicao() {
        if(token.getType() == 2) //TK_IDENTIFIER 
        {
            nextToken();
            if(token.getType() == 18) //TK_ARITHMETIC_OPERATOR_ATRIBUICAO
            {
                nextToken();
                expr_arit();
                if(token.getType() == 24)//SPECIAL_CHARACTER_PONTOEVIRGULA
                {
                    return true; //a atribuição está correta
                }
                else
                {
                   System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: ATRIBUITION invalid.");
                   System.out.println("" + token.getText() + " invalid");
                   System.out.println("SPECIAL_CHARACTER_PONTOEVIRGULA expected");
                   System.exit(0);
                }
            }
            else
            {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: TK_RELATIONAL_OPERATOR expected.");
                System.out.println("" + token.getText() + " invalid");
                System.out.println("TK_ARITHMETIC_OPERATOR_ATRIBUICAO expected");
                System.exit(0);
            }
        }
        else
        {
            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: TK_RELATIONAL_OPERATOR expected.");
            System.out.println("" + token.getText() + " invalid");
            System.out.println("TK_IDENTIFIER expected");
            System.exit(0);
        }
        return false;
    }
    
    //auxiliar do expr_relacional
    //aqui adicionamos as validações de operações relacionais
    public void op_relacional(){
        if(token.getType() == 25 || token.getType() == 9 || token.getType() == 10 || token.getType() == 11 || token.getType() == 12 || token.getType() == 13)
        {
            nextToken();
        }else
        { 
            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: TK_RELATIONAL_OPERATOR expected.");
            System.out.println("" + token.getText() + " invalid");
            System.exit(0);
        }
    }
    
    //<expr_relacional> ::=   <expr_arit> <op_relacional> <expr_arit>
    private void expr_relacional() {
        expr_arit();
        op_relacional();
        expr_arit();
    }

    //<expr_arit> ::=   <expr_arit> "+" <termo>   | <expr_arit> "-" <termo> | <termo>
    private void expr_arit() 
    {
        termo();
        if (token.getType() == 14 || token.getType() == 15) 
        {//SOMA OU SUBTRAÇÃO
            nextToken();
            expr_arit();//recursão
        }
    }

    //<termo> ::=   <termo> "*" <fator> | <termo> “/” <fator> | <fator>
    public void termo() 
    {
        fator();
        if (token.getType() == 16 || token.getType() == 17)//DIVISAO OU MULTIPLICACAO
        {   
            nextToken();
            termo();//recursão
        }
    }

    //<fator> ::=   “(“ <expr_arit> “)” | <id> | <float> | <inteiro> | <char>
    public void fator() 
    {
        switch (token.getType()) 
        {
            case 19: //SPECIAL_CHARACTER_ABREPARENTESES
                nextToken();
                expr_arit();
                if (token.getType() == 20) 
                {// ) //SPECIAL_CHARACTER_FECHAPARENTESES
                    nextToken();
                } else 
                {
                    //AJEITAR ESSA MENSAGEM DE ERRO
                    System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_FECHAPARENTESES expected.");
                    System.out.println("" + token.getText() + " invalid");
                    System.exit(0);
                }
                break;
            case 1: //TK_CHAR
            case 2: //TK_IDENTIFIER
            case 3: //TK_INT
            case 4: //TK_FLOAT
                nextToken();
                break;
            default:
                //AJEITAR ESSA MENSAGEM DE ERRO
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_ABREPARENTESES expected.");
                System.out.println("" + token.getText() + " invalid");
                System.exit(0);
        }
    }
}
