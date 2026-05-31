package tarea7c;

public class Precipitacion {

    private java.util.List<Integer> fecha;
    private String estacionMeteorologica;
    private String provincia;
    private double precipitacion;

    public Precipitacion() {}

    public java.util.List<Integer> getFecha() {
        return fecha;
    }

    public void setFecha(java.util.List<Integer> fecha) {
        this.fecha = fecha;
    }

    public String getEstacionMeteorologica() {
        return estacionMeteorologica;
    }

    public void setEstacionMeteorologica(String estacionMeteorologica) {
        this.estacionMeteorologica = estacionMeteorologica;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public double getPrecipitacion() {
        return precipitacion;
    }

    public void setPrecipitacion(double precipitacion) {
        this.precipitacion = precipitacion;
    }

    public String toString() {
        return estacionMeteorologica + " - " + precipitacion;
    }
}