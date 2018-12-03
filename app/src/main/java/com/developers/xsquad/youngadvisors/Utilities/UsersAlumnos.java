package com.developers.xsquad.youngadvisors.Utilities;

public class UsersAlumnos extends DataAlumnos{
    private String Id;

    public UsersAlumnos(String Id, String Apellido, String Carrera, String Nombre, String Telefono){
        super(Apellido, Carrera, Nombre, Telefono);
        this.Id = Id;
    }

    public UsersAlumnos(){

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
