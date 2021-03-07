public class Main {
    public static void main(String[] args){
        
       Scanner sc = new Scanner("src/entrada1.txt");
       Token token = null;


       do{
           token = sc.nextToken();
           if(token != null){
               System.out.println(token);
           }


       }while(token != null);
    }
    
}
