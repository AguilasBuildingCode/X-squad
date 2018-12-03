package com.developers.xsquad.youngadvisors.Utilities;

public class DataAlumnos {
    private String apellido, carrera, nombre, telefono;

    public DataAlumnos(String apellido, String carrera, String nombre, String telefono){
        this.apellido = apellido;
        this.carrera = carrera;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public DataAlumnos(){

    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
