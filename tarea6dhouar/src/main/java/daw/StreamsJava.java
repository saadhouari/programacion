package daw;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamsJava {

    public static List<Vehicle> getFirstFiveVehicles(List<Vehicle> vehicles) {
        return vehicles.stream()
                .limit(5)
                .toList();
    }

    public static void printVehiclesFrom2012(List<Vehicle> vehicles) {
        vehicles.stream()
                .filter(vehicle -> vehicle.getYear() == 2012)
                .forEach(System.out::println);
    }

    public static Set<String> getDistinctBrands(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(Vehicle::getMake)
                .collect(Collectors.toSet());
    }

    public static List<Vehicle> getVehiclesSortedByYear(List<Vehicle> vehicles) {
        return vehicles.stream()
                .sorted(Comparator.comparingInt(Vehicle::getYear))
                .toList();
    }

    public static List<Vehicle> skipTenAndTakeFive(List<Vehicle> vehicles) {
        return vehicles.stream()
                .skip(10)
                .limit(5)
                .toList();
    }

    public static boolean existsGreenVehicle(List<Vehicle> vehicles) {
        return vehicles.stream()
                .anyMatch(vehicle -> "Green".equalsIgnoreCase(vehicle.getColor()));
    }

    public static boolean areAllVehiclesFrom1990OrLater(List<Vehicle> vehicles) {
        return vehicles.stream()
                .allMatch(vehicle -> vehicle.getYear() >= 1990);
    }

    public static long countFordVehicles(List<Vehicle> vehicles) {
        return vehicles.stream()
                .filter(vehicle -> "Ford".equalsIgnoreCase(vehicle.getMake()))
                .count();
    }

    public static int getSmallestYear(List<Vehicle> vehicles) {
        return vehicles.stream()
                .mapToInt(Vehicle::getYear)
                .min()
                .orElse(-1);
    }

    public static Set<String> getUniqueColorsAfter2010(List<Vehicle> vehicles) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getYear() > 2010)
                .map(Vehicle::getColor)
                .collect(Collectors.toSet());
    }

    public static Map<String, Long> countVehiclesByColor(List<Vehicle> vehicles) {
        return vehicles.stream()
                .collect(Collectors.groupingBy(Vehicle::getColor, Collectors.counting()));
    }

    public static Map<String, List<Vehicle>> groupVehiclesByLicensePlate(List<Vehicle> vehicles) {
        return vehicles.stream()
                .collect(Collectors.groupingBy(Vehicle::getLicensePlate));
    }
}
