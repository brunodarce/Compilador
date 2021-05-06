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

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public void nextToken() {
        token = scanner.nextToken();
    }

    //<programa>       ::=   int main"("")" <bloco>
    public void program() {
        nextToken();
        if (token.getType() == 32) { //INT
            nextToken();
            if (token.getType() == 27) { //MAIN
                nextToken();
                if (token.getType() == 19) {// (
                    nextToken();
                    if (token.getType() == 20) {// )
                        nextToken();
                        bloco();
                    } else {
                        System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_FECHAPARENTESES expected.");
                        System.exit(0);
                    }
                } else {
                    System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_ABREPARENTESES expected.");
                    System.exit(0);
                }
            } else {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: Reserved Word MAIN expected.");
                System.exit(0);
            }
        } else {
            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: Reserved Word INT expected.");
            System.exit(0);
            throw new SyntaxException("Tipo INT esperado na declaração do método.");
        }
    }

    //<bloco>          ::=   “{“ {<decl_var>}* {<comando>}* “}”
    private void bloco() {
        if (token.getType() == 21) {// {
            nextToken();

            if (token.getType() == 22) {// }
                nextToken();
                bloco();
            } else {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_FECHACHAVE expected.");
                System.exit(0);
            }
        } else {
            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_ABRECHAVE expected.");
            System.exit(0);
        }
    }

    public void decl_var_aux() {// int x,y,z
        if (token.getType() == 1) {//TK_IDENTIFIER
            nextToken();
            if (token.getType() == 23) {//SPECIAL_CHARACTER_VIRGULA
                nextToken();
                decl_var_aux();
            }
        }
    }

    public void decl_var() {
        if (token.getType() == 32 || token.getType() == 33 || token.getType() == 34) {//TK_RESERVED_WORD INT FLOAT CHAR
            nextToken();
            //int x; ou int x,y,z;
            if (token.getType() == 1) {//TK_IDENTIFIER
                nextToken();
                switch (token.getType()) {
                    case 23:
                        //SPECIAL_CHARACTER_VIRGULA
                        nextToken();
                        decl_var_aux();
                        nextToken();
                        if (token.getType() == 24) {//SPECIAL_CHARACTER_PONTOEVIRGULA
                            nextToken();
                        } else {
                            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_PONTOEVIRGULA expected.");
                            System.exit(0);
                        }
                        //int a;
                        break;
                    case 24:
                        //SPECIAL_CHARACTER_PONTOEVIRGULA
                        nextToken();
                        //int x = a;    
                        break;
                    case 18:
                        //TK_ARITHMETIC_OPERATOR_ATRIBUICAO
                        nextToken();
                        if (token.getType() == 1) {//TK_IDENTIFIER
                            nextToken();
                            if (token.getType() == 24) {//SPECIAL_CHARACTER_PONTOEVIRGULA
                                nextToken();
                            } else {
                                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_PONTOEVIRGULA expected.");
                                System.exit(0);
                            }
                        } else {
                            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: TK_IDENTIFIER expected.");
                            System.exit(0);
                        }   break;
                    default:
                        System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: TK_ARITHMETIC_OPERATOR_ATRIBUICAO expected.");
                        System.exit(0);
                }
            }else {
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: TK_IDENTIFIER expected.");
                System.exit(0);
            }
        }else {
            System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: TK_RESERVED_WORD expected.");
            System.exit(0);
        }
    }

    //<comando>        ::=   <comando_básico> | <iteração> | if "("<expr_relacional>")" <comando> {else <comando>}?
    public void comando() {

    }

    //<comando_básico> ::=   <atribuição> | <bloco>
    private void comando_basico() {

    }

    //<iteração>       ::=   while "("<expr_relacional>")" <comando> | do <comando> while "("<expr_relacional>")"";"
    private void iteracao() {

    }

    //<atribuição>     ::=   <id> "=" <expr_arit> ";"
    private void atribuicao() {

    }

    //<expr_relacional> ::=   <expr_arit> <op_relacional> <expr_arit>
    private void expr_relacional() {

    }

    //<expr_arit> ::=   <expr_arit> "+" <termo>   | <expr_arit> "-" <termo> | <termo>
    private void expr_arit() {
        termo();
        if (token.getType() == 14 || token.getType() == 15) {//SOMA OU SUBTRAÇÃO
            nextToken();
            expr_arit();//recursão
        }
    }

    //<termo> ::=   <termo> "*" <fator> | <termo> “/” <fator> | <fator>
    public void termo() {
        fator();
        if (token.getType() == 16 || token.getType() == 17) { //DIVISAO OU MULTIPLICACAO
            nextToken();
            termo();//recursão
        }
    }

    //<fator> ::=   “(“ <expr_arit> “)” | <id> | <float> | <inteiro> | <char>
    public void fator() {
        switch (token.getType()) {
            case 19:
                // (
                nextToken();
                expr_arit();
                if (token.getType() == 20) {// )
                    nextToken();

                } else {
                    //AJEITAR ESSE ERRO
                    System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_FECHAPARENTESES expected.");
                    System.exit(0);
                }
                break;
            case 2:
            case 4:
            case 3:
            case 1:
                //TK_IDENTIFIER id ou float ou interio ou char
                nextToken();
                break;
            default:
                //AJEITAR ESSE ERRO
                System.out.println("Error at line: " + token.getLine() + ", column: " + token.getColumn() + ", Description: SPECIAL_CHARACTER_ABREPARENTESES expected.");
                System.exit(0);
        }
    }
}
