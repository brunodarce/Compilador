/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

public class Compiladores {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Scanner sc = new Scanner("entrada2.txt");
       Parser pa = new Parser(sc);
       Token token = null;
       
       pa.program();


       do{
           token = sc.nextToken();
           if(token != null){
               System.out.println(token);
           }
       }while(token != null);
    }
    
}
