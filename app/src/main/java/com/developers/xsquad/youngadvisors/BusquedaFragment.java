package com.developers.xsquad.youngadvisors;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.developers.xsquad.youngadvisors.Utilities.Adaptadores.AdapterDatos;
import com.developers.xsquad.youngadvisors.Utilities.Adaptadores.Extend_UFinded;
import com.developers.xsquad.youngadvisors.Utilities.Materias;
import com.developers.xsquad.youngadvisors.Utilities.Tipo_Usuarios;
import com.developers.xsquad.youngadvisors.Utilities.UsersFinded;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class BusquedaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String AuxId;

    EditText Buscar;
    Spinner Tipo;
    ArrayList<String> tipos;
    ArrayList<UsersFinded> ListFindUsers;
    ArrayList<Extend_UFinded> usersFindeds;
    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public BusquedaFragment() {
    }

    public static BusquedaFragment newInstance(String param1, String param2) {
        BusquedaFragment fragment = new BusquedaFragment();
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
        /*
        tipos = new ArrayList<String>();
        getTypeUsers();
        tipos.add("Usuario"); //RESUELVE EL ERROR DE EL SPINNER
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, tipos);
        Tipo.setAdapter(adapter);
        Tipo.setSelection(0);
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);
        Tipo = view.findViewById(R.id.spinner_FindUsers);
        Buscar = view.findViewById(R.id.ETBuscar);
        recyclerView = view.findViewById(R.id.Resultados_busqueda);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersFindeds = new ArrayList<>();
        ListFindUsers = new ArrayList<UsersFinded>();
        //llenarLista();

        tipos = new ArrayList<String>();
        getTypeUsers();
        tipos.add("Usuario"); //RESUELVE EL ERROR DE EL SPINNER
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, tipos);
        Tipo.setAdapter(adapter);
        Tipo.setSelection(0);

        Buscar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    /*
                    *
                    *       AQUI VA EL CODIGO DE EXTRACION DE USUARIOS DE FIREBASE <<<<<<<<<----------
                    *
                    */
                    final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("proyecto/db/lista/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                                // EXTRAEMOS CODIGO DE USUARIO <<<<<<<<<<<
                                mDatabase.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        try{
                                            Materias listMaterias = snapshot.getValue(Materias.class);
                                            Toast.makeText(getContext(), "Materias! \n", Toast.LENGTH_LONG).show();
                                        }catch (Exception e){
                                            Toast.makeText(getContext(), "Error /n" + e.toString(), Toast.LENGTH_LONG).show();
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
                    return true;
                }
                else{
                    return false;
                }
            }
        });
        return view;
    }

    private void llenarLista() {
        usersFindeds.add(new Extend_UFinded("dE2rnKpZO1Zw5rEYcHGz22qfu0C2", "Prueba", "Prueba", 4.0));
        AdapterDatos adapterDatos = new AdapterDatos(usersFindeds, getContext());
        recyclerView.setAdapter(adapterDatos);
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

    public void getTypeUsers(){
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
    }
}
