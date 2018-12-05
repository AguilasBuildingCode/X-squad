package com.developers.xsquad.youngadvisors;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.xsquad.youngadvisors.Utilities.Adaptadores.AdapterComentarios;
import com.developers.xsquad.youngadvisors.Utilities.Calificador;
import com.developers.xsquad.youngadvisors.Utilities.CalificadorData;
import com.developers.xsquad.youngadvisors.Utilities.Carreras;
import com.developers.xsquad.youngadvisors.Utilities.DataPerfil;
import com.developers.xsquad.youngadvisors.Utilities.Tipo_Usuarios;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class PerfilUsuariosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView Nombre, Carrera, Telefono, Sobremi, Correo;
    ImageView Foto;
    Bundle bundle;
    String UI;
    ArrayList<Calificador> calificadors;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;
    DatabaseReference mDatabase;

    private OnFragmentInteractionListener mListener;

    public PerfilUsuariosFragment() {

    }

    public static PerfilUsuariosFragment newInstance(String param1, String param2) {
        PerfilUsuariosFragment fragment = new PerfilUsuariosFragment();
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
        View view = inflater.inflate(R.layout.fragment_perfil_usuarios, container, false);
        bundle = getArguments();
        UI = bundle.getString("UI");
        calificadors = new ArrayList<Calificador>();
        Foto = view.findViewById(R.id.IVPerfilFotoUsuarios);
        Nombre = view.findViewById(R.id.TVPerfilNombreUsuarios);
        Carrera = view.findViewById(R.id.TVPerfilCarreraUsuarios);
        Correo = view.findViewById(R.id.TVPerfilCorreoUsuarios);
        Telefono = view.findViewById(R.id.TVPerfilTelefonoUsuario);
        Sobremi = view.findViewById(R.id.TVPerfilSobremiUsuarios);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("proyecto/db/perfir/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase.child("perfir/").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                if(snapshot.getKey().equals(UI)) {
                                    DataPerfil dataPerfil = snapshot.getValue(DataPerfil.class);
                                    Nombre.setText(dataPerfil.getNombre() + " " + dataPerfil.getApellido());
                                    Carrera.setText(dataPerfil.getCarrera());
                                    Correo.setText(dataPerfil.getCorreo());
                                    Telefono.setText(dataPerfil.getTelefono());
                                    Sobremi.setText(dataPerfil.getSobremi());
                                }
                            }catch (Exception e){
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
        DescargarImagen(UI);
        //Cargar comentarios...

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("proyecto/db/calificaciones/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase.child(snapshot.getKey() + "/calificador/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(UI.equals(snapshot.getKey())) {
                                for (final DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    mDatabase.child(snapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(final DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                mDatabase.child(snapshot2.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        //Toast.makeText(getContext(), snapshot2.getValue().toString(), Toast.LENGTH_LONG).show();
                                                        try {
                                                            Calificador calificador = new Calificador(snapshot2.getKey(), snapshot2.getValue(CalificadorData.class));
                                                            calificadors.add(calificador);
                                                        } catch (Exception e) {
                                                            Toast.makeText(getContext(), "Error: \n" + e.toString(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                            //Recycler ------------------
                                            //Toast.makeText(getContext(), snapshot1.getValue().toString(), Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
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

        RecyclerView RComentarios = view.findViewById(R.id.ResultadoComentarios);
        ArrayList<Calificador> calificadors = new ArrayList<Calificador>();
        calificadors.add(new Calificador("eMDTRqJqzUeAamVsLvoOOqjzdUv1", "Perez", 5, "ok", "Aaron"));
        AdapterComentarios adapterComentarios = new AdapterComentarios(calificadors, getContext());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RComentarios.setLayoutManager(llm);
        RComentarios.setAdapter(adapterComentarios);

        return view;
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
        void onFragmentInteraction(Uri uri);
    }

    public void DescargarImagen(String UI) {
        try {
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            mountainsRef = storageRef.child("fotos/" + UI + ".jpg");
            final File localFile = File.createTempFile(UI, "jpg");
            mountainsRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Foto.setImageURI(Uri.parse(localFile.getPath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(getContext(), "Error al descargar foto de peril", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "No pudimos descargar tu foto de perfil", Toast.LENGTH_LONG).show();
        }
    }
}
