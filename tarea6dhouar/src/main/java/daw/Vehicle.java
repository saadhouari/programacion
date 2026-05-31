package daw;

import com.opencsv.bean.CsvBindByName;

public class Vehicle {

    @CsvBindByName(column = "vehicle_make")
    private String make;

    @CsvBindByName(column = "vehicle_model")
    private String model;

    @CsvBindByName(column = "vehicle_year")
    private int year;

    @CsvBindByName(column = "vehicle_vin")
    private String vin;

    @CsvBindByName(column = "vehicle_color")
    private String color;

    @CsvBindByName(column = "vehicle_license_plate")
    private String licensePlate;

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getVin() {
        return vin;
    }

    public String getColor() {
        return color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", vin='" + vin + '\'' +
                ", color='" + color + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                '}';
    }
}
