package com.developers.xsquad.youngadvisors.Utilities;

public class Alumnos {
    private String nombre, apellido, telefono, carrera;

    public Alumnos(String apellido, String carrera, String nombre, String telefono){
        this.nombre = nombre;
        this.apellido = apellido;
        this.carrera = carrera;
        this.telefono = telefono;
    }

    public Alumnos(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
}
