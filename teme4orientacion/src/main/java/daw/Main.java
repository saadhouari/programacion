package daw;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // calculadora
        String menu = """
                ## Bienvenido a la caluladora
                        1. -suma
                        2. -restar
                        3. -multiplicar
                        4. -dividir
                        5. -salir
                        """;
        int opcionElegida;
        boolean salir = false;// No quiero salir , se repite hasta
        // que el usuario decide salir = true
        Scanner teclado = new Scanner(System.in);
        // Repetir
        do {
            System.out.println(menu);
            System.out.println(" Introduce una opción de (1-5) :");
            opcionElegida = teclado.nextInt();

            switch (opcionElegida) {
                case 1: {
                    System.out.println(" opción sumar ");
                }
                case 2: {
                    System.out.println(" Opción restar ");
                }
                case 3: {
                    System.out.println(" opción multiplicar ");
                }
                case 4: {
                    System.out.println(" opción dividir ");
                }
                case 5: {
                    System.out.println(" has decidido salir ");
                    System.out.println(" Hasta pronto ");
                }

                    break;

            }

        } while (!salir); // mientras quieres salir

    }
}