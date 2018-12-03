package com.developers.xsquad.youngadvisors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.developers.xsquad.youngadvisors.Utilities.Alumnos;
import com.developers.xsquad.youngadvisors.Utilities.Asesores;
import com.developers.xsquad.youngadvisors.Utilities.Carreras;
import com.developers.xsquad.youngadvisors.Utilities.Insert_Carreras;
import com.developers.xsquad.youngadvisors.Utilities.Tipo_Usuarios;
import com.developers.xsquad.youngadvisors.Utilities.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class RegistrarActivity extends AppCompatActivity {
    AnimationDrawable  animacion;
    String UserId, varNombre, varApellido, varTelefono, varTipoUsuario, varCarrera, varCotizacion;
    EditText Nombre, Apellidos, Telefono, Cotizacion;
    FirebaseUser firebaseUser;
    Bitmap Foto;
    ImageView UFoto;
    Spinner type_user, type_carriers;

    private static final int COD_SELECCIONADA = 10;
    private static final int COD_FOTO = 20;
    private static final String CARPETA_IMAGEN = "imagenes";
    private static final String CARPETA_PRINCIPAL = "misImagenesApp";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
    private String path;
    private Uri miPath, downloadUri;
    //private File file;
    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;

    ArrayList<String> tipos, tipos_carreras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animacion = (AnimationDrawable)getResources().getDrawable( R.drawable.animacion);

        ImageView vista = new ImageView(this);
        vista.setBackgroundColor(Color.WHITE);
        vista.setImageDrawable(animacion);
        vista.setOnClickListener(new View.OnClickListener()
        {public void onClick(View view) { animacion.start(); } });
        setContentView(vista);


    setContentView(R.layout.activity_registrar);

        Nombre = findViewById(R.id.ETNombre);
        Apellidos = findViewById(R.id.ETApellidos);
        Telefono = findViewById(R.id.ETTelefono);
        Cotizacion = findViewById(R.id.ETCotizacion);
        UFoto = findViewById(R.id.IVRegistrarU);
        type_user = findViewById(R.id.Tipos_spinner);
        type_carriers = findViewById(R.id.spinner_carrera);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        tipos = new ArrayList<String>();
        tipos_carreras = new ArrayList<>();
        getTypesUsers();
        tipos_carreras.add("Selecciona una carrera");
        tipos.add("Seleccione un tipo de usuario"); //RESUELVE EL ERROR DE EL SPINNER
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrarActivity.this,
                android.R.layout.simple_spinner_item, tipos);
        type_user.setAdapter(adapter);
        type_user.setSelection(0);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(RegistrarActivity.this,
                android.R.layout.simple_spinner_item, tipos_carreras);
        type_carriers.setAdapter(adapter2);
        type_carriers.setSelection(0);
        type_carriers.setEnabled(false);
        Cotizacion.setEnabled(false);

        type_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    type_carriers.setEnabled(true);
                    if(position == 2){
                        Cotizacion.setEnabled(true);
                    }
                    else {
                        Cotizacion.setEnabled(false);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getTypesUsers() {
        /*
        *
        *           Codigo para actualizar los datos en tiempo real
        *
        * */
        /*
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("proyecto/db/Type_User/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tipos.clear();
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Type_User/").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Tipo_Usuarios ob = new Tipo_Usuarios(snapshot.getValue().toString());
                            //    tipos.add(ob.gettipo());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

        /*
        *
        *
        *           Codigo para consultar solo una vez los datos de la base de datos
        *
        * */
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("proyecto/db/Type_User/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Type_User/").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Tipo_Usuarios ob = new Tipo_Usuarios(snapshot.getValue().toString());
                            tipos.add(ob.gettipo());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDatabase2= FirebaseDatabase.getInstance().getReference();
        mDatabase2.child("proyecto/db/carrera/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase2.child("carrera/").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Carreras obCarreras = snapshot.getValue(Carreras.class);
                            tipos_carreras.add(obCarreras.getTipo());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void Registrar(View view){
        if(TextUtils.isEmpty(Nombre.getText().toString())){
            Nombre.requestFocus();
            Toast.makeText(this, "Falta su Nombre", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Apellidos.getText().toString())){
            Apellidos.requestFocus();
            Toast.makeText(this, "Falta sus Apellidos", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Telefono.getText().toString()))
        {
            Telefono.requestFocus();
            Toast.makeText(this , "Falta su numero de Telefono", Toast.LENGTH_LONG).show();
            return;
        }
        if(type_user.getSelectedItemPosition() == 0){
            type_user.setSelection(1);
            Toast.makeText(this, "Seleccione un tipo de usuario", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Cotizacion.getText().toString())){
            Cotizacion.setText("0");
        }
        if(type_carriers.getSelectedItemPosition() == 0){
            type_carriers.setSelection(1);
            Toast.makeText(this, "Seleccione una carrera", Toast.LENGTH_LONG).show();
            return;
        }

        UserId = firebaseUser.getUid();
        varNombre = Nombre.getText().toString().trim();
        varApellido = Apellidos.getText().toString().trim();
        varTelefono = Telefono.getText().toString().trim();
        varTipoUsuario = type_user.getSelectedItem().toString();
        varCarrera = type_carriers.getSelectedItem().toString();
        varCotizacion = Cotizacion.getText().toString().trim();
        varCotizacion = varCotizacion.replace(" ", "");
        try{
            float x = Float.parseFloat(varCotizacion);
        }catch (Exception e){
            Cotizacion.setText("0");
            Toast.makeText(this, "La cotizacion debe ser numerica", Toast.LENGTH_LONG).show();
            return;
        }

        Step1Registrar();
    }

    public void Step1Registrar(){
        /*
        *
        *       CODIGO PARA SUBIR LA FOTO AL SERVIDOR EN EL APARTADO DE STRONGE, EN UNA CARPETA foto/
        *
        */
        try {
            progressDialog.setMessage("Subiendo imagen...");
            progressDialog.show();
            // FirebaseStringe objet for get instance on it
            storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            storageRef = storage.getReference();
            // Create a reference to "mountains.jpg"
            mountainsRef = storageRef.child("fotos/" + UserId + ".jpg");
            // Create a reference to 'images/iduser.jpg'
            final StorageReference mountainImagesRef = storageRef.child("fotos/"+ UserId +".jpg");
            // While the file names are the same, the references point to different files
            mountainsRef.getName().equals(mountainImagesRef.getName());    // true
            mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

            // Get the data from an ImageView as bytes
            UFoto.setDrawingCacheEnabled(true); //UFoto es ImagenView del layout
            UFoto.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) UFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    progressDialog.dismiss();
                    Toast.makeText(RegistrarActivity.this, "Error al almacenar la foto" + exception.toString(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    progressDialog.dismiss();
                    Toast.makeText(RegistrarActivity.this, "La foto fue almacenada", Toast.LENGTH_LONG).show();
                    /*
                    *
                    *       MANDAMOS LLAMAR EL SIGUIENTE PROCESO SÍ TODO SALE BIEN
                    *
                    */
                    Step2Registrar();
                }
            });
        }catch (Exception e)
        {
            progressDialog.dismiss();
            Toast.makeText(RegistrarActivity.this, "No se pudo ingresar la foto" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void Step2Registrar(){
        /*
        *
        *       PROCESO PARA EXTRAER EL URL DE DESCARGA DE LA IMAGEN SUBIDA ANTERIORMENTE PARA ALMACENARLA EN EL PERFIL
        *
        * */
        try{
            progressDialog.setMessage("Espere...");
            progressDialog.show();
            //extraer url de la imagen almacenada
            mountainsRef = storageRef.child("fotos/");
            mountainsRef.child(UserId + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for the user
                    progressDialog.dismiss();
                    downloadUri = uri;
                    Toast.makeText(RegistrarActivity.this, "url consegido", Toast.LENGTH_LONG).show();
                    /*
                    *
                    *       SI EL PROCESO SALE BIEN PASA AL SIGUIENTE PROCESO
                    *
                    * */
                    Step3Registrar();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    progressDialog.dismiss();
                    Toast.makeText(RegistrarActivity.this, "url no encontrado" + exception.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(RegistrarActivity.this, "Error al conseguir el url" + e.toString(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    public void Step3Registrar(){
        /*
        *
        *          INGRESANDO LOS DATOS A LA BASE DE DATOS
        *
        * */
        try{
            progressDialog.setMessage("Ingresando sus datos...");
            progressDialog.show();
            //Para ingresar los datos a la db
            Users user = new Users(varNombre, varApellido, varTelefono, varTipoUsuario);
            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
            mDatabase.child("proyecto/db/usuarios").child(UserId).setValue(user);
            progressDialog.dismiss();
            Toast.makeText(this, "Nodo insertado con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            progressDialog.dismiss();
        }
        //      SIGUIENTE PROCESO
        Step4Registrar();
    }

    public void Step4Registrar(){
        /*
         *
         *      ACTUALIZANDO LOS DATOS DEL PERFIL DE AUTHENTICATION DE FIREBASE
         *
         * */
        try {
            progressDialog.setMessage("Actualizando perfil de usuario...");
            progressDialog.show();
            //Para ingresar los datos de usuario creado
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(varNombre + " " + varApellido)
                    .setPhotoUri(downloadUri)
                    .build();
            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Todo salio bien
                                progressDialog.dismiss();
                                Toast.makeText(RegistrarActivity.this, "Perfil actualizado", Toast.LENGTH_LONG).show();
                                Step5Registrar();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrarActivity.this, "No se puede actualizar perfil", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch (Exception e){
            Toast.makeText(RegistrarActivity.this, "Error al actualizar perfil", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    public void Step5Registrar(){
        /*
        *
        *       SE LIMPIA EL FORMULARIO
        *
        * */
        switch (type_user.getSelectedItemPosition()){
            case 1:
                //insertar datos en nodo alumnos
                Alumnos obAlumnos = new Alumnos(varNombre, varApellido, varTelefono, varCarrera);
                DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                mDatabase.child("proyecto/db/alumnos").child(UserId).setValue(obAlumnos);
                break;
            case 2:
                //insertar datos en nodo asesores
                Asesores obAsesor = new Asesores(varNombre, varApellido, varTelefono, varCarrera, varCotizacion);
                DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference();
                mDatabase2.child("proyecto/db/asesores").child(UserId).setValue(obAsesor);
                break;
                default:
                    break;
        }
        switch (type_carriers.getSelectedItemPosition()){
            case 1:
                FirebaseDatabase.getInstance().getReference().child("proyecto/db/carrera/"
                        + type_carriers.getSelectedItemPosition()).child(UserId).setValue(
                        new Insert_Carreras(varNombre, varApellido, varTelefono));
                break;
            case 2:
                FirebaseDatabase.getInstance().getReference().child("proyecto/db/carrera/"
                        + type_carriers.getSelectedItemPosition()).child(UserId).setValue(
                        new Insert_Carreras(varNombre, varApellido, varTelefono));
                break;
            case 3:
                FirebaseDatabase.getInstance().getReference().child("proyecto/db/carrera/"
                        + type_carriers.getSelectedItemPosition()).child(UserId).setValue(
                        new Insert_Carreras(varNombre, varApellido, varTelefono));
                break;
            case 4:
                FirebaseDatabase.getInstance().getReference().child("proyecto/db/carrera/"
                        + type_carriers.getSelectedItemPosition()).child(UserId).setValue(
                        new Insert_Carreras(varNombre, varApellido, varTelefono));
                break;
            case 5:
                FirebaseDatabase.getInstance().getReference().child("proyecto/db/carrera/"
                        + type_carriers.getSelectedItemPosition()).child(UserId).setValue(
                        new Insert_Carreras(varNombre, varApellido, varTelefono));
                break;
                default:
                    break;
        }
        UFoto.setImageResource(R.drawable.usuario);
        Nombre.setText("");
        Apellidos.setText("");
        Telefono.setText("");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void subirFoto(View view){
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //abriCamara();
                }else{
                    if (opciones[i].equals("Elegir de Galeria")){
                        Intent intent=new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONADA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONADA:
                miPath=data.getData();
                UFoto.setImageURI(miPath);
                try {
                    Foto=MediaStore.Images.Media.getBitmap(this.getContentResolver(),miPath);
                    UFoto.setImageBitmap(Foto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(this, new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });

                Foto= BitmapFactory.decodeFile(path);
                UFoto.setImageBitmap(Foto);
                break;
        }
    }
}
