package com.developers.xsquad.youngadvisors;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.developers.xsquad.youngadvisors.Utilities.Tipo_Usuarios;
import com.developers.xsquad.youngadvisors.Utilities.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


public class ModificarPerfilFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ImageView UFoto;
    Bitmap Foto;
    Button btnActualizar;
    EditText Nombre, Apellido, Telefono, Sobremi;
    DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    Users users;
    ArrayList<String > tipos;

    private OnFragmentInteractionListener mListener;
    private static final int COD_SELECCIONADA = 10;
    private static final int COD_FOTO = 20;
    private static final String CARPETA_IMAGEN = "imagenes";
    private static final String CARPETA_PRINCIPAL = "misImagenesApp";
    private String path;
    private Uri miPath;
    boolean UpdatePick = false;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;

    public ModificarPerfilFragment() {

    }

    public static ModificarPerfilFragment newInstance(String param1, String param2) {
        ModificarPerfilFragment fragment = new ModificarPerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Toast.makeText(getContext(), "Ingrese solo los datos que desea modificar", Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "De clic sobre la imagen para subir su foto", Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificar_perfil, container, false);
        Nombre = view.findViewById(R.id.ETModPerfilNombre);
        Apellido = view.findViewById(R.id.ETModPerfilApellido);
        Telefono = view.findViewById(R.id.ETModPerfilTelefono);
        Sobremi = view.findViewById(R.id.ETModPerfilSobremi);
        UFoto = view.findViewById(R.id.IVModPerfilFoto);
        btnActualizar = view.findViewById(R.id.BtnModPerfilModificar);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        tipos = new ArrayList<String>();
        ExtraerUsuario();
        ExtraerTiposUsuarios();
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Codigo para actualizar perfil...
                DefinirNoTipo();
            }
        });
        UFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    subirFoto(v);
            }
        });

        return view;
    }

    public void ExtraerUsuario(){
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("proyecto/db/usuarios/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    try {
                        if(snapshot.getKey().equals(firebaseUser.getUid())){
                            users = snapshot.getValue(Users.class);
                            //Toast.makeText(getContext(), users.getNombre(), Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void ExtraerTiposUsuarios(){
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("proyecto/db/Type_User/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Type_User/").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Tipo_Usuarios ob = new Tipo_Usuarios(snapshot.getValue().toString());
                            tipos.add(ob.gettipo());
                            //Toast.makeText(getContext(), snapshot.getValue().toString(), Toast.LENGTH_LONG).show();
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

    int NoTipo;
    public void DefinirNoTipo(){
        NoTipo = tipos.indexOf(users.getTipo()) + 1;
        InsertarCambios();
    }

    public void InsertarCambios(){
        if(!TextUtils.isEmpty(Nombre.getText().toString()))
        {
            mDatabase.child("proyecto/db/usuarios")                 .child(firebaseUser.getUid())
                    .child("/nombre").setValue(Nombre.getText().toString());
            mDatabase.child("proyecto/db/calificaciones")           .child(firebaseUser.getUid())
                    .child("/nombre").setValue(Nombre.getText().toString());
            mDatabase.child("proyecto/db/lista")                    .child(firebaseUser.getUid())
                    .child("/nombre").setValue(Nombre.getText().toString());
            mDatabase.child("proyecto/db/perfir")                   .child(firebaseUser.getUid())
                    .child("/nombre").setValue(Nombre.getText().toString());
            mDatabase.child("proyecto/db/carrera/" + NoTipo + "/")  .child(firebaseUser.getUid())
                    .child("/nombre").setValue(Nombre.getText().toString());
            mDatabase.child("proyecto/db/" + users.getTipo() + "/") .child(firebaseUser.getUid())
                    .child("/nombre").setValue(Nombre.getText().toString());
        }
        if(!TextUtils.isEmpty(Apellido.getText().toString())){
            mDatabase.child("proyecto/db/usuarios")                 .child(firebaseUser.getUid())
                    .child("/apellidos").setValue(Apellido.getText().toString());
            mDatabase.child("proyecto/db/calificaciones")           .child(firebaseUser.getUid())
                    .child("/apellido").setValue(Apellido.getText().toString());
            mDatabase.child("proyecto/db/lista")                    .child(firebaseUser.getUid())
                    .child("/apellido").setValue(Apellido.getText().toString());
            mDatabase.child("proyecto/db/perfir")                   .child(firebaseUser.getUid())
                    .child("/apellido").setValue(Apellido.getText().toString());
            mDatabase.child("proyecto/db/carrera/" + NoTipo + "/")  .child(firebaseUser.getUid())
                    .child("/apellido").setValue(Apellido.getText().toString());
            mDatabase.child("proyecto/db/" + users.getTipo() + "/") .child(firebaseUser.getUid())
                    .child("/apellido").setValue(Apellido.getText().toString());
        }
        if(!TextUtils.isEmpty(Telefono.getText().toString())){
            mDatabase.child("proyecto/db/usuarios")                 .child(firebaseUser.getUid())
                    .child("/telefono").setValue(Telefono.getText().toString());
            mDatabase.child("proyecto/db/perfir")                   .child(firebaseUser.getUid())
                    .child("/telefono").setValue(Telefono.getText().toString());
            mDatabase.child("proyecto/db/carrera/" + NoTipo + "/")  .child(firebaseUser.getUid())
                    .child("/telefono").setValue(Telefono.getText().toString());
            mDatabase.child("proyecto/db/" + users.getTipo() + "/") .child(firebaseUser.getUid())
                    .child("/telefono").setValue(Telefono.getText().toString());
        }
        if(!TextUtils.isEmpty(Sobremi.getText().toString())){
            mDatabase.child("proyecto/db/perfir")                   .child(firebaseUser.getUid())
                    .child("/sobremi").setValue(Sobremi.getText().toString());
        }
        Limpiar();
    }

    public void Limpiar(){

        if(UpdatePick){
            ActualizarFoto();
            UFoto.setImageResource(R.drawable.usuario);
        }else {
            UpdatePick = false;
        }

        Nombre.setText("");
        Apellido.setText("");
        Telefono.setText("");
        Sobremi.setText("");
        Nombre.requestFocus();
        Snackbar.make(getView(), "Se actualizaron tus datos", Snackbar.LENGTH_LONG)
                .setAction("Update", null).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void subirFoto(View view){
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
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
                    Foto=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),miPath);
                    UFoto.setImageBitmap(Foto);
                    UpdatePick = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
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

    public void ActualizarFoto(){
        // FirebaseStringe objet for get instance on it
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReference();
        // Create a reference to "mountains.jpg"
        mountainsRef = storageRef.child("fotos/" + firebaseUser.getUid() + ".jpg");
        // Create a reference to 'images/iduser.jpg'
        final StorageReference mountainImagesRef = storageRef.child("fotos/"+ firebaseUser.getUid() +".jpg");
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
                Toast.makeText(getContext(), "Error al almacenar la foto" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "La foto fue almacenada", Toast.LENGTH_LONG).show();
            }
        });
    }
}
