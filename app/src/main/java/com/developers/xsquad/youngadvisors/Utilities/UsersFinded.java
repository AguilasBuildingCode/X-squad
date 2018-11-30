package com.developers.xsquad.youngadvisors.Utilities;

import java.io.File;

public class UsersFinded {
    private String Nombre, Id, Apellido, Calificacion;
    public UsersFinded(String Id, String Nombre, String Apellido, String Calificacion){
        this.Id = Id;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Calificacion = Calificacion;
    }

    public UsersFinded(){

    }

    public String getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(String calificacion) {
        Calificacion = calificacion;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
