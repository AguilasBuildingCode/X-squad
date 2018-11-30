package com.developers.xsquad.youngadvisors.Utilities.Adaptadores;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.developers.xsquad.youngadvisors.R;
import com.developers.xsquad.youngadvisors.Utilities.UsersFinded;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;

    List<Extend_UFinded> arratList;
    Context context;

    public AdapterDatos(ArrayList<Extend_UFinded> arrayList, Context context){
        this.arratList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.itemlistfind, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos viewHolderDatos, int i) {
        viewHolderDatos.Nombre.setText(arratList.get(i).getNombre() + " " + arratList.get(i).getApellido());
        viewHolderDatos.Calificacion = (arratList.get(i).getCalificacion());

        if(arratList.get(i).getId() != null){
            cargarImagenFirebase(viewHolderDatos.Id, viewHolderDatos);
        }
        else
        {
            viewHolderDatos.Foto.setImageResource(R.drawable.usuario);
        }
        int cal = (int)(viewHolderDatos.Calificacion);
        switch (cal){
            case 1:
                viewHolderDatos.Estrellas.setImageResource(R.drawable.estrella_1);
                break;
            case 2:
                viewHolderDatos.Estrellas.setImageResource(R.drawable.estrella_2);
                break;
            case 3:
                viewHolderDatos.Estrellas.setImageResource(R.drawable.estrella_3);
                break;
            case 4:
                viewHolderDatos.Estrellas.setImageResource(R.drawable.estrella_4);
                break;
            case 5:
                viewHolderDatos.Estrellas.setImageResource(R.drawable.estrella_5);
                break;
        }
    }

    public void cargarImagenFirebase(String ruta, final ViewHolderDatos holderDatos){
        try {
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            mountainsRef = storageRef.child("fotos/" + holderDatos.Id + ".jpg");
            final File localFile = File.createTempFile(holderDatos.Id, "jpg");
            mountainsRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    holderDatos.Foto.setImageURI(Uri.parse(localFile.getPath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(context, "Error al descargar foto de peril", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(context, "No pudimos descargar tu foto de perfil", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return arratList.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        String Id;
        double Calificacion;
        TextView Nombre;
        ImageView Estrellas, Foto;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            Nombre = itemView.findViewById(R.id.EDFindNombre);
            Estrellas = itemView.findViewById(R.id.IVFindEstrellas);
            Foto = itemView.findViewById(R.id.IVFoto);
        }
    }
}
