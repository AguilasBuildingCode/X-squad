package com.developers.xsquad.youngadvisors.Utilities;

import android.graphics.Bitmap;

public class Users {
    private String Nombre, Apellidos, Telefono, Tipo;
    public Users(String Nombre, String Apellidos, String Telefono, String Tipo){
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Telefono = Telefono;
        this.Tipo = Tipo;
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

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }
}
