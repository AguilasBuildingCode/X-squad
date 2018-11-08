package com.developers.xsquad.youngadvisors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class RegistrarActivity extends AppCompatActivity {

    EditText Nombre, Apellidos, Correo, Telefono, Usuario, Password, Respuesta1, Respuesta2;
    Spinner Pregunta1, Pregunta2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
    }

    public void Registrar(View view){
        Intent intent = new Intent(this, PerfilRegistroActivity.class);
        startActivity(intent);
    }
}
