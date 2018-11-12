package com.developers.xsquad.youngadvisors.Utilities;

public class Asesores {
    private String Nombre, Apellido, Telefono, Carrera, Cotizacion;

    public Asesores(String Nombre, String Apellido, String Telefono, String Carrera, String Cotizacion){
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Telefono = Telefono;
        this.Carrera = Carrera;
        this.Cotizacion = Cotizacion;
    }

    public Asesores(){

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getCarrera() {
        return Carrera;
    }

    public void setCarrera(String carrera) {
        Carrera = carrera;
    }

    public String getCotizacion() {
        return Cotizacion;
    }

    public void setCotizacion(String cotizacion) {
        Cotizacion = cotizacion;
    }
}
