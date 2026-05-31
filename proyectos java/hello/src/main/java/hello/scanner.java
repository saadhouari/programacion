package hello;

import java.util.Scanner;

public class scanner {
  public static void main(String[] args) {

/* ex 1
    Scanner scanner = new Scanner(System.in ) ;

        int a ;
        int b ; 

    System.out.println("saisir a et b ") ;
    a = scanner.nextInt();
    b = scanner.nextInt();

    System.out.println("la resulat est " + (a*b));*/

    Scanner scanner= new Scanner(System.in);

        double amount ; 
        double pesetase =1.21;
    System.out.println("what is the amount you want to convert : ");
        amount = scanner.nextDouble() ;

    System.out.println("the convert amount is  "+ (amount*pesetase)+"$");




    
  }  
}
