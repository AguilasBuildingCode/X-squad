package com.developers.xsquad.youngadvisors;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.xsquad.youngadvisors.Utilities.DataPerfil;
import com.developers.xsquad.youngadvisors.Utilities.Tipo_Usuarios;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class PerfilFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;
    ProgressDialog progressDialog;

    ImageView Foto;
    TextView Nombre, Correo;
    Bundle bundle;
    String UI;
    DataPerfil DP;

    public PerfilFragment() {

    }

    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        progressDialog = new ProgressDialog(getContext());
        bundle = getArguments();
        UI = bundle.getString("UI");
        DescargarImagen(UI);

        Nombre = vista.findViewById(R.id.TVPerfilNombre);
        Correo = vista.findViewById(R.id.TVPerfilCorreo);
        Foto = vista.findViewById(R.id.IVPerfilView);

        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

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

    public void DescargarImagen(final String UI) {
        try {
            progressDialog.setMessage("Descargando foto de perfil...");
            progressDialog.show();
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            mountainsRef = storageRef.child("fotos/" + UI + ".jpg");
            final File localFile = File.createTempFile(UI, "jpg");
            mountainsRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Foto.setImageURI(Uri.parse(localFile.getPath()));
                    localFile.delete();
                    Toast.makeText(getContext(), "Exito!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Info(UI);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error al descargar foto de peril", Toast.LENGTH_LONG).show();
                    Info(UI);
                }
            });
        }catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(getContext(), "No pudimos descargar tu foto de perfil", Toast.LENGTH_LONG).show();
            Info(UI);
        }
    }

    public void Info(final String UI){
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("proyecto/db/perfir/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mDatabase.child("perfir/" + snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    if(snapshot.getKey().equals(UI)) {
                                        DP = snapshot.getValue(DataPerfil.class);
                                        Toast.makeText(getContext(), DP.toString(), Toast.LENGTH_LONG).show();
                                        Nombre.setText(DP.getNombre());
                                    }
                                } catch (Exception e) {
                                    //Toast.makeText(getContext(), "Error \n" + e.toString(), Toast.LENGTH_LONG).show();
                                }
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
}
