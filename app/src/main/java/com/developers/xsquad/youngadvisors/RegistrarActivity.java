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

        Nombre = findViewById(R.id.ETNombre);
        Apellidos = findViewById(R.id.ETApellidos);
        Correo = findViewById(R.id.ETCorreo);
        Telefono = findViewById(R.id.ETTelefono);
        Usuario = findViewById(R.id.ETUsuario);
        Password = findViewById(R.id.ETPass);
        Pregunta1 = findViewById(R.id.spinnerPregunta1);
        Respuesta1 = findViewById(R.id.ETRespuesta1);
        Pregunta2 = findViewById(R.id.spinnerPregunta2);
        Respuesta2 = findViewById(R.id.ETRespuesta2);
    }

    public void Registrar(View view){
        Intent intent = new Intent(this, PerfilRegistroActivity.class);
        startActivity(intent);
    }
}
