package com.developers.xsquad.youngadvisors.Utilities;

public class Perfil extends DataPerfil{
    private String Id;

    public Perfil(String Id, String apellido, String carrera, String correo, String nombre, String sobremi, String telefono){
        super(apellido, carrera, correo, nombre, sobremi, telefono);
        this.Id = Id;
    }

    public Perfil(){

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
