package com.developers.xsquad.youngadvisors;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.developers.xsquad.youngadvisors.Utilities.ListaMaterias;
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
    ArrayList<UsersFinded> usersFindeds;
    ArrayList<Extend_UFinded> extend_uFindeds;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        Buscar = view.findViewById(R.id.ETBuscar);
        recyclerView = view.findViewById(R.id.Resultados_busqueda);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersFindeds = new ArrayList<UsersFinded>();
        extend_uFindeds = new ArrayList<Extend_UFinded>();
        Buscar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    /*
                    *
                    *       AQUI SE BUSCARA EL USUARIO POR NOMBRE <<<<<<<<<---------- "VA A SER UN PEDO :("
                    *
                    */

                    mDatabase.child("proyecto/db/alumnos/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Toast.makeText(getContext(), snapshot.getValue().toString(), Toast.LENGTH_LONG).show();
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
}
