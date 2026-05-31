package alboran;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {

        int opcion = 0;
        String tipoAceituna = "";
        int tamanioAceituna = 0;
        double kg = 0;

        do {

            try {
                opcion = Funciones.pedirOpcion();
                // Si continua mi programa por aquí significa
                // que no hay excepción NumberFormat
                switch (opcion) {
                    case 1 -> {
                        // Vender
                        // Pedir tipo
                        tipoAceituna = Funciones.pedirTipoAceituna();
                        System.out.println("El tipo es: " + tipoAceituna);
                        // Pedir tamaño
                        // Devuelve un int indicando 1 gruesa y 2 fina
                        tamanioAceituna = Funciones.pedirTamanioAceituna();
                        System.out.println("El tamaño es " + pasarTamanioAceituna(tamanioAceituna));
                        // Pedir kg
                        kg = Funciones.pedirKg();
                        System.out.println("Los kg son " + kg);
                        // calcular
                    }
                    case 2 -> {
                        JOptionPane.showMessageDialog(null, "Hasta pronto");
                    }
                    default -> {
                        JOptionPane.showMessageDialog(null, "Opción incorrecta");

                    }

                }

            } catch (NumberFormatException nfe) {
                // Repite el bucle al saltar la excepción
                opcion = 0;
            }

        } while (opcion != 2);
    }

    public static String pasarTamanioAceituna(int numero){
        String tamanio;
        tamanio = numero == 1?Funciones.GRUESA:Funciones.FINA;
        return tamanio;
    }
}