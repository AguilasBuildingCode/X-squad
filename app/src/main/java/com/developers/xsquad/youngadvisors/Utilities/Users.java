package com.developers.xsquad.youngadvisors.Utilities;

public class Users {
    private String Nombre, Apellidos, Telefono;
    public Users(String Nombre, String Apellidos, String Telefono){
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Telefono = Telefono;
    }

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }
}
