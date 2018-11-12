package com.developers.xsquad.youngadvisors.Utilities;

public class Tipo_Usuarios {
    private String tipo;
    public Tipo_Usuarios(String tipo){
        tipo = tipo.replace("{", "");
        tipo = tipo.replace("}", "");
        tipo = tipo.replace("tipo=", "");
        this.tipo = tipo;
    }

    public Tipo_Usuarios(){

    }

    public String gettipo() {
        return tipo;
    }

    public void settipo(String tipo) {
        tipo = tipo;
    }

    @Override

    public String toString() {
        return "{tipo='"+
                tipo+
                "'}";
    }
}
