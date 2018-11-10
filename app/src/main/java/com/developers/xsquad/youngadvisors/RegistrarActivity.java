package com.developers.xsquad.youngadvisors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.xsquad.youngadvisors.Utilities.Users;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RegistrarActivity extends AppCompatActivity {

    String UserId;
    EditText Nombre, Apellidos, Telefono;
    FirebaseUser firebaseUser;
    Bitmap Foto;
    ImageView UFoto;

    private static final int COD_SELECCIONADA = 10;
    private static final int COD_FOTO = 20;
    private static final String CARPETA_IMAGEN = "imagenes";
    private static final String CARPETA_PRINCIPAL = "misImagenesApp";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
    private String path;
    private Uri miPath, downloadUri;
    private File file;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Nombre = findViewById(R.id.ETNombre);
        Apellidos = findViewById(R.id.ETApellidos);
        Telefono = findViewById(R.id.ETTelefono);
        UFoto = findViewById(R.id.IVRegistrarU);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
    }

    public void Registrar(View view){
        progressDialog.setMessage("Ingresando...");
        progressDialog.show();
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();
            // Create a reference to "mountains.jpg"
            StorageReference mountainsRef = storageRef.child("fotos/" + firebaseUser.getUid() + ".jpg");
            // Create a reference to 'images/mountains.jpg'
            final StorageReference mountainImagesRef = storageRef.child("fotos/"+ firebaseUser.getUid() +".jpg");
            // While the file names are the same, the references point to different files
            mountainsRef.getName().equals(mountainImagesRef.getName());    // true
            mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

            // Get the data from an ImageView as bytes
            UFoto.setDrawingCacheEnabled(true);
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
                    Toast.makeText(RegistrarActivity.this, "Error al almacenar la foto" + exception.toString(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Toast.makeText(RegistrarActivity.this, "La foto fue almacenada", Toast.LENGTH_LONG).show();
                    adduserpart2();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(RegistrarActivity.this, "No se pudo ingresar la foto" + e.toString(), Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
    }

    public void adduserpart2(){
        try{
            //ver url
            /*
            *
            * ---------------------------------------- NO ENCUENTRA EL ARCHIVO Y NO PUEDE EXTRAER LA URL ------------------------------------
            *
            * */
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference mountainsRef = storageRef.child("fotos/");
            mountainsRef.child(firebaseUser.getUid() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    downloadUri = uri;
                    Toast.makeText(RegistrarActivity.this, "url consegido \n" + downloadUri, Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(RegistrarActivity.this, "url no encontrado \n" + exception, Toast.LENGTH_LONG).show();
                }
            });
            //Para ingresar los datos a la db
            UserId = firebaseUser.getUid();
            Users user = new Users(Nombre.getText().toString(), Apellidos.getText().toString(), Telefono.getText().toString());
            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
            mDatabase.child("proyecto/db/usuarios").child(UserId).setValue(user);
            //UFoto.setImageResource(getResources((R.drawable.usuario)));
            Toast.makeText(this, "Nodo insertado con exito", Toast.LENGTH_SHORT).show();

            //Para ingresar los datos de usuario creado
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(Nombre.getText().toString().trim() + " " + Apellidos.getText().toString().trim())
                    .setPhotoUri(downloadUri)
                    .build();
            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Todo salio bien
                                Toast.makeText(RegistrarActivity.this, "Perfil actualizado", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            Nombre.setText("");
            Apellidos.setText("");
            Telefono.setText("");
        }
        catch (Exception x){
            Toast.makeText(RegistrarActivity.this, "Hubo problema en la inserción \n" + x.toString(), Toast.LENGTH_SHORT).show();
        }
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
