package com.developers.xsquad.youngadvisors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.xsquad.youngadvisors.Utilities.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrarActivity extends AppCompatActivity {

    String UserId;
    EditText Nombre, Apellidos, Telefono;
    TextView tvMensaje;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Nombre = findViewById(R.id.ETNombre);
        Apellidos = findViewById(R.id.ETApellidos);
        Telefono = findViewById(R.id.ETTelefono);
        tvMensaje = findViewById(R.id.TVMensaje);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void Registrar(View view){

        try{
            UserId = user.getUid();
            Users user = new Users(Nombre.getText().toString(), Apellidos.getText().toString(), Telefono.getText().toString());
            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
            mDatabase.child("proyecto/db/usuarios").child(UserId).setValue(user);
            tvMensaje.setText("");
            Nombre.setText("");
            Apellidos.setText("");
            Telefono.setText("");
            Toast.makeText(this, "Nodo insertado con exito", Toast.LENGTH_SHORT).show();
        }
        catch (Exception x){
            Toast.makeText(this, "Hubo problema en la inserción \n" + x.toString(), Toast.LENGTH_SHORT).show();
        }

        //Intent intent = new Intent(this, PerfilRegistroActivity.class);
        //startActivity(intent);
    }
}
