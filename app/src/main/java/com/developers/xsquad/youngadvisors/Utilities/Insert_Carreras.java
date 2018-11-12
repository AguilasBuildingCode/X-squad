package com.developers.xsquad.youngadvisors.Utilities;

public class Insert_Carreras {
    private String Nombre, Apellido, Telefono;
    public Insert_Carreras(String Nombre, String Apellido, String Telefono){
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Telefono = Telefono;
    }

    public Insert_Carreras(){

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
}
