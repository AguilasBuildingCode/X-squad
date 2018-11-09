package com.developers.xsquad.youngadvisors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrarEPActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    EditText correo, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_ep);
        correo = findViewById(R.id.ETCorreoEP);
        password = findViewById(R.id.ETPassEP);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void registrarUsuario(View view) {
        progressDialog.setMessage("Realizando consulta en linea...");
        progressDialog.show();
        if (TextUtils.isEmpty(correo.getText())) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password.getText())) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(correo.getText().toString().trim(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(RegistrarEPActivity.this, RegistrarActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegistrarEPActivity.this, "Algo anda mal :(", Toast.LENGTH_LONG).show();
                }
            }
        });
        progressDialog.dismiss();
    }
}
