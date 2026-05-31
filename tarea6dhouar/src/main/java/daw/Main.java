package daw;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            List<Vehicle> vehicles = new VehicleCsvReader().readVehicles(Path.of("vehiculos.csv"));

            System.out.println("Primeros 5 vehiculos:");
            StreamsJava.getFirstFiveVehicles(vehicles).forEach(System.out::println);

            System.out.println("\nVehiculos del anio 2012:");
            StreamsJava.printVehiclesFrom2012(vehicles);

            System.out.println("\nMarcas distintas:");
            System.out.println(StreamsJava.getDistinctBrands(vehicles));

            System.out.println("\nVehiculos ordenados por anio:");
            StreamsJava.getVehiclesSortedByYear(vehicles).forEach(System.out::println);

            System.out.println("\nVehiculos saltando 10 y tomando 5:");
            StreamsJava.skipTenAndTakeFive(vehicles).forEach(System.out::println);

            System.out.println("\nExiste algun vehiculo Green: " + StreamsJava.existsGreenVehicle(vehicles));
            System.out.println("Todos los vehiculos son de 1990 o superior: " + StreamsJava.areAllVehiclesFrom1990OrLater(vehicles));
            System.out.println("Cantidad de vehiculos Ford: " + StreamsJava.countFordVehicles(vehicles));
            System.out.println("Anio mas pequeno: " + StreamsJava.getSmallestYear(vehicles));

            System.out.println("\nColores unicos despues de 2010:");
            System.out.println(StreamsJava.getUniqueColorsAfter2010(vehicles));

            System.out.println("\nCantidad de vehiculos por color:");
            System.out.println(StreamsJava.countVehiclesByColor(vehicles));

            System.out.println("\nVehiculos agrupados por matricula:");
            System.out.println(StreamsJava.groupVehiclesByLicensePlate(vehicles));
        } catch (IOException exception) {
            System.err.println("No se pudo leer el archivo CSV: " + exception.getMessage());
        }
    }
}
