package com.developers.xsquad.youngadvisors;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.RouteInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developers.xsquad.youngadvisors.Utilities.Adaptadores.AdapterComentarios;
import com.developers.xsquad.youngadvisors.Utilities.Adaptadores.AdapterDatos;
import com.developers.xsquad.youngadvisors.Utilities.Adaptadores.Extend_UFinded;
import com.developers.xsquad.youngadvisors.Utilities.Calificador;
import com.developers.xsquad.youngadvisors.Utilities.Tipo_Usuarios;
import com.developers.xsquad.youngadvisors.Utilities.UsersFinded;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class BusquedaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String AuxId;

    EditText Buscar;
    Button btnBuscar;
    ArrayList<Extend_UFinded> extend_uFindeds;
    RecyclerView RecyclerAlumnos;
    DatabaseReference mDatabase;
    FragmentTransaction fragmentTransaction;
    ProgressDialog progressDialog;

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
        final View view = inflater.inflate(R.layout.fragment_busqueda, container, false);
        progressDialog = new ProgressDialog(getContext());
        mDatabase= FirebaseDatabase.getInstance().getReference();
        Buscar = view.findViewById(R.id.ETBuscar);
        btnBuscar = view.findViewById(R.id.BtnBusAlumnos);
        RecyclerAlumnos = view.findViewById(R.id.Resultados_busqueda);
        RecyclerAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));
        extend_uFindeds = new ArrayList<Extend_UFinded>();
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Buscar.getText().toString().isEmpty()){
                    /*
                     *
                     *       AQUI SE BUSCARA EL USUARIO POR NOMBRE <<<<<<<<<---------- "VA A SER UN PEDO :("
                     *
                     */

                    extend_uFindeds.clear();
                    progressDialog.setMessage("Buscando...");
                    progressDialog.show();
                    final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("proyecto/db/alumnos/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                                mDatabase.child("alumnos/").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        UsersFinded UF = snapshot.getValue(UsersFinded.class);
                                        if(UF.getNombre().contains(Buscar.getText().toString().trim()) ||
                                                UF.getApellido().contains(Buscar.getText().toString().trim())){
                                            extend_uFindeds.add(new Extend_UFinded(snapshot.getKey(), UF));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            AdapterDatos adapterDatos = new AdapterDatos(extend_uFindeds, getContext());
                            adapterDatos.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        //Cambiamos de fragment al de perfil ---------------
                                        //PerfilFragment perfilFragment = new PerfilFragment();
                                        PerfilUsuariosFragment perfilUsuariosFragment = new PerfilUsuariosFragment();
                                        fragmentTransaction = getFragmentManager().beginTransaction();
                                        Bundle args = new Bundle();
                                        args.putString("UI", extend_uFindeds.get(RecyclerAlumnos.getChildAdapterPosition(v)).getId());
                                        perfilUsuariosFragment.setArguments(args);
                                        fragmentTransaction.replace(R.id.fragment, perfilUsuariosFragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();

                                    }catch (Exception e){
                                        Toast.makeText(getContext(), "Error: \n" + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            RecyclerAlumnos.setAdapter(adapterDatos);
                            progressDialog.dismiss();

                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                }
                else{
                }
            }
        });
        /*
        Buscar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && !Buscar.getText().toString().isEmpty()){

                    extend_uFindeds.clear();
                    progressDialog.setMessage("Buscando...");
                    progressDialog.show();
                    final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("proyecto/db/alumnos/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                                mDatabase.child("alumnos/").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        UsersFinded UF = snapshot.getValue(UsersFinded.class);
                                        if(UF.getNombre().contains(Buscar.getText().toString().trim()) ||
                                                UF.getApellido().contains(Buscar.getText().toString().trim())){
                                            extend_uFindeds.add(new Extend_UFinded(snapshot.getKey(), UF));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            AdapterDatos adapterDatos = new AdapterDatos(extend_uFindeds, getContext());
                            adapterDatos.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        //Cambiamos de fragment al de perfil ---------------
                                        //PerfilFragment perfilFragment = new PerfilFragment();
                                        PerfilUsuariosFragment perfilUsuariosFragment = new PerfilUsuariosFragment();
                                        fragmentTransaction = getFragmentManager().beginTransaction();
                                        Bundle args = new Bundle();
                                        args.putString("UI", extend_uFindeds.get(RecyclerAlumnos.getChildAdapterPosition(v)).getId());
                                        perfilUsuariosFragment.setArguments(args);
                                        fragmentTransaction.replace(R.id.fragment, perfilUsuariosFragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();

                                    }catch (Exception e){
                                        Toast.makeText(getContext(), "Error: \n" + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            RecyclerAlumnos.setAdapter(adapterDatos);
                            progressDialog.dismiss();

                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });

                    return true;
                }
                else{
                    return false;
                }
            }
        });
        */
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
