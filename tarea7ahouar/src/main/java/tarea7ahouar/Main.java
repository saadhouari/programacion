package tarea7ahouar;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Path rutaCsv = Path.of("vehiculos.csv");
        List<Vehiculo> vehiculos = leerVehiculos(rutaCsv);

        System.out.println("LISTA DE VEHICULOS");
        vehiculos.forEach(System.out::println);

        Set<String> fabricantesPink2007 = obtenerFabricantesPink2007(vehiculos);
        System.out.println("\nFABRICANTES DE COCHES PINK DE 2007");
        System.out.println(fabricantesPink2007);

        Set<String> fabricantesPink2007Streams = obtenerFabricantesPink2007ConStreams(vehiculos);
        System.out.println("\nFABRICANTES DE COCHES PINK DE 2007 CON STREAMS");
        System.out.println(fabricantesPink2007Streams);

        Map<String, Integer> totalPorColor = contarVehiculosPorColor(vehiculos);
        System.out.println("\nTOTAL DE COCHES POR COLOR");
        System.out.println(totalPorColor);

        Map<String, Integer> totalPorColorStreams = contarVehiculosPorColorConStreams(vehiculos);
        System.out.println("\nTOTAL DE COCHES POR COLOR CON STREAMS");
        System.out.println(totalPorColorStreams);

        guardarMapaEnCsv(totalPorColor, "conteo_colores.csv");
        System.out.println("\nFichero CSV generado: conteo_colores.csv");
    }

    public static List<Vehiculo> leerVehiculos(Path rutaCsv) {
        List<Vehiculo> vehiculos = new ArrayList<>();

        if (!Files.exists(rutaCsv)) {
            System.out.println("No existe el fichero: " + rutaCsv.toAbsolutePath());
            return vehiculos;
        }

        try (BufferedReader reader = Files.newBufferedReader(rutaCsv, StandardCharsets.UTF_8)) {
            String linea = reader.readLine();

            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }

                String[] campos = linea.split(",", -1);
                if (campos.length != 6) {
                    continue;
                }

                Vehiculo vehiculo = new Vehiculo(
                        campos[0].trim(),
                        campos[1].trim(),
                        Integer.parseInt(campos[2].trim()),
                        campos[3].trim(),
                        campos[4].trim(),
                        campos[5].trim()
                );
                vehiculos.add(vehiculo);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero CSV: " + e.getMessage());
        }

        return vehiculos;
    }

    public static Set<String> obtenerFabricantesPink2007(List<Vehiculo> vehiculos) {
        Set<String> fabricantes = new LinkedHashSet<>();

        for (Vehiculo vehiculo : vehiculos) {
            if ("Pink".equalsIgnoreCase(vehiculo.color()) && vehiculo.anio() == 2007) {
                fabricantes.add(vehiculo.fabricante());
            }
        }

        return fabricantes;
    }

    public static Set<String> obtenerFabricantesPink2007ConStreams(List<Vehiculo> vehiculos) {
        return vehiculos.stream()
                .filter(vehiculo -> "Pink".equalsIgnoreCase(vehiculo.color()))
                .filter(vehiculo -> vehiculo.anio() == 2007)
                .map(Vehiculo::fabricante)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Map<String, Integer> contarVehiculosPorColor(List<Vehiculo> vehiculos) {
        Map<String, Integer> totalPorColor = new TreeMap<>();

        for (Vehiculo vehiculo : vehiculos) {
            String color = vehiculo.color();
            totalPorColor.put(color, totalPorColor.getOrDefault(color, 0) + 1);
        }

        return totalPorColor;
    }

    public static Map<String, Integer> contarVehiculosPorColorConStreams(List<Vehiculo> vehiculos) {
        return vehiculos.stream()
                .collect(Collectors.groupingBy(
                        Vehiculo::color,
                        TreeMap::new,
                        Collectors.summingInt(vehiculo -> 1)
                ));
    }

    public static void guardarMapaEnCsv(Map<String, Integer> mapa, String nombreFichero) {
        List<String> lineas = new ArrayList<>();

        for (Map.Entry<String, Integer> entrada : mapa.entrySet()) {
            lineas.add(entrada.getKey() + "," + entrada.getValue());
        }

        try {
            Files.write(Path.of(nombreFichero), lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se pudo guardar el fichero CSV: " + e.getMessage());
        }
    }
}
