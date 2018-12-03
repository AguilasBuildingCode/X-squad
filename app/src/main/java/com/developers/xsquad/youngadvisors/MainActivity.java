package com.developers.xsquad.youngadvisors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    EditText Correo, Pass;
    public static final String USER = "usuario";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Correo = (EditText) findViewById(R.id.ETUsuario);
        Pass = (EditText) findViewById(R.id.ETPass);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        checkCurrentUser();
    }
    //obtienes la referencia del Layout.

    public void loguearUsuario(View view) {
        final String email = Correo.getText().toString().trim();
        String password = Pass.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Ingresando...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()) {
                                Toast.makeText(MainActivity.this, "Bienvenido: " + Correo.getText(), Toast.LENGTH_LONG).show();
                                Intent intencion = new Intent(getApplication(), InicioActivity.class);
                                intencion.putExtra(USER, user);
                                startActivity(intencion);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Debe verificar su email...", Toast.LENGTH_LONG).show();
                                firebaseAuth.getCurrentUser();
                                firebaseAuth.signOut();
                            }
                        }else{
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(MainActivity.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void Registrar(View view) {
        Intent intent = new Intent(this, RegistrarEPActivity.class);
        startActivity(intent);
    }

    public void ResetPass(View view){
        if (TextUtils.isEmpty(Correo.getText().toString().trim())) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.sendPasswordResetEmail(Correo.getText().toString().trim());
        Toast.makeText(this, "Restaure su contraseña mediante el link del correo electronico enviado", Toast.LENGTH_LONG).show();
        Pass.requestFocus();
    }

    public void checkCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            Intent intencion = new Intent(getApplication(), InicioActivity.class);
            intencion.putExtra(USER, user);
            startActivity(intencion);
        }
    }
}
