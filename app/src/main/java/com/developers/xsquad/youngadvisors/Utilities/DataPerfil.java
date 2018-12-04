package com.developers.xsquad.youngadvisors.Utilities;

public class DataPerfil {
    private String apellido, carrera, nombre, sobremi, telefono;

    public DataPerfil(String apellido, String carrera, String nombre, String sobremi, String telefono){
        this.apellido = apellido;
        this.carrera = carrera;
        this.nombre = nombre;
        this.sobremi = sobremi;
        this.telefono = telefono;
    }

    public DataPerfil(){

    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSobremi() {
        return sobremi;
    }

    public void setSobremi(String sobremi) {
        this.sobremi = sobremi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String toString(){
        return "nombre: " + nombre + "\n"
                + "apellido: " + apellido + "\n"
                + "carrera: " + carrera + "\n"
                + "telefono: " + telefono + "\n"
                + "Sobre mi: " + sobremi;
    }
}
