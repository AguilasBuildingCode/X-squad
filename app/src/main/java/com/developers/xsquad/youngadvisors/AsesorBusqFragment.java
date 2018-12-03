package com.developers.xsquad.youngadvisors;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.developers.xsquad.youngadvisors.Utilities.Adaptadores.AdapterDatos;
import com.developers.xsquad.youngadvisors.Utilities.Adaptadores.Extend_UFinded;
import com.developers.xsquad.youngadvisors.Utilities.ListaMaterias;
import com.developers.xsquad.youngadvisors.Utilities.UsersFinded;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AsesorBusqFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AsesorBusqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AsesorBusqFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<String> Carreras;
    ArrayList<String> Semestres;
    ArrayList<String> Materias;
    ArrayList<UsersFinded> usersFindeds;
    ArrayList<Extend_UFinded> extend_uFindeds;
    Spinner Scarrera, SSemestre, SMaterias;
    RecyclerView RecyclerAsesores;

    private OnFragmentInteractionListener mListener;

    public AsesorBusqFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AsesorBusqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AsesorBusqFragment newInstance(String param1, String param2) {
        AsesorBusqFragment fragment = new AsesorBusqFragment();
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
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_asesor_busq, container, false);
        Scarrera = view.findViewById(R.id.spinnerCarrera);
        SSemestre = view.findViewById(R.id.spinnerSemestre);
        SMaterias = view.findViewById(R.id.spinnerMateria);
        RecyclerAsesores = view.findViewById(R.id.RecyclerAsesores);
        RecyclerAsesores.setLayoutManager(new LinearLayoutManager(getContext()));
        Carreras = new ArrayList<String>();
        Semestres = new ArrayList<String>();
        Materias = new ArrayList<String>();
        usersFindeds = new ArrayList<UsersFinded>();
        extend_uFindeds = new ArrayList<Extend_UFinded>();
        Carreras.add("Carreras");
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("proyecto/db/materias/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(!Carreras.equals(snapshot.getKey())){
                        Carreras.add(snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, Carreras);
        Scarrera.setAdapter(adapter);
        Scarrera.setSelection(0);

        Scarrera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Semestres.clear();
                Semestres.add("Semestre");
                if(position != 0){
                    final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("proyecto/db/materias/" + Scarrera.getSelectedItem().toString().trim() + "/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Semestres.add(snapshot.getKey());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, Semestres);
                    SSemestre.setAdapter(adapter);
                    SSemestre.setSelection(0);

                    SSemestre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position !=0){
                                Materias.clear();
                                Materias.add("Materias");
                                final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("proyecto/db/materias/" + Scarrera.getSelectedItem().toString().trim() + "/" + SSemestre.getSelectedItem().toString().trim() + "/")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                                            Materias.add(snapshot.getValue().toString());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                        android.R.layout.simple_spinner_item, Materias);
                                SMaterias.setAdapter(adapter);
                                SMaterias.setSelection(0);

                                SMaterias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if(position !=0) {
                                            RecyclerAsesores.clearOnChildAttachStateChangeListeners();
                                            extend_uFindeds.clear();
                                            /*
                                             *
                                             *           AQUI SE DEBE RELLENAR EL RECYCLERVIEW
                                             *
                                             */

                                            mDatabase.child("proyecto/db/lista/").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                        try{
                                                            ListaMaterias LM = snapshot.getValue(ListaMaterias.class);
                                                            if(LM.getMateria1().equals(SMaterias.getSelectedItem().toString()) ||
                                                                    LM.getMateria2().equals(SMaterias.getSelectedItem().toString()) ||
                                                                    LM.getMateria3().equals(SMaterias.getSelectedItem().toString()) ||
                                                                    LM.getMateria4().equals(SMaterias.getSelectedItem().toString()) ||
                                                                    LM.getMateria5().equals(SMaterias.getSelectedItem().toString()) ||
                                                                    LM.getMateria6().equals(SMaterias.getSelectedItem().toString()) ||
                                                                    LM.getMateria7().equals(SMaterias.getSelectedItem().toString()) ||
                                                                    LM.getMateria8().equals(SMaterias.getSelectedItem().toString())){
                                                                UsersFinded UF = snapshot.getValue(UsersFinded.class);
                                                                extend_uFindeds.add(new Extend_UFinded(snapshot.getKey(), UF.getNombre(), UF.getApellido()));
                                                            }
                                                        } catch (Exception e) {
                                                            Log.e("LM", e.toString());
                                                        }
                                                    }

                                                    AdapterDatos adapterDatos = new AdapterDatos(extend_uFindeds, getContext());
                                                    RecyclerAsesores.setAdapter(adapterDatos);

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
