package tarea7c;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            // Leer JSON
            List<Precipitacion> lista = mapper.readValue(
                    new File("precipitacionesBadajoz.json"),
                    new TypeReference<List<Precipitacion>>() {}
            );

         
            System.out.println("LISTA:");
            for (Precipitacion p : lista) {
                System.out.println(p);
            }

            Map<String, Double> mapa = lista.stream()
                    .collect(Collectors.toMap(
                            p -> p.getEstacionMeteorologica(),
                            p -> p.getPrecipitacion(),
                            (a, b) -> a + b
                    ));

            System.out.println("\nMAPA:");
            mapa.forEach((k, v) -> System.out.println(k + ": " + v));

            // Guardar JSON
            mapper.writeValue(new File("resultado.json"), mapa);

            System.out.println("\nMAX:");
            Precipitacion max = lista.stream()
                    .max((a, b) -> Double.compare(a.getPrecipitacion(), b.getPrecipitacion()))
                    .get();

            System.out.println(max);

         
            long count = lista.stream()
                    .filter(p -> {
                        int dia = p.getFecha().get(2);
                        return dia >= 10 && dia <= 20;
                    })
                    .count();

            System.out.println("\nNumero estaciones: " + count);

            
            double media = lista.stream()
                    .filter(p -> {
                        int dia = p.getFecha().get(2);
                        return dia >= 10 && dia <= 20;
                    })
                    .mapToDouble(p -> p.getPrecipitacion())
                    .average()
                    .orElse(0);

            System.out.println("Media: " + media);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}