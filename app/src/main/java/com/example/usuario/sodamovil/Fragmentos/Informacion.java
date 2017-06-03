package com.example.usuario.sodamovil.Fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.usuario.sodamovil.BaseDeDatos.StorageDB;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.R;
import com.example.usuario.sodamovil.VariablesGlobales;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.usuario.sodamovil.R.id.container;
import static com.example.usuario.sodamovil.R.id.textView;


public class Informacion extends Fragment {

    public Informacion(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragmento_informacion, container, false);
        setRestaurante(inf);
        return inf;
    }

    public void setRestaurante(View view){
        Restaurante restaurante=VariablesGlobales.getInstance().getRestauranteActual();
        if(restaurante != null){

            TextView NombreTW = (TextView) view.findViewById(R.id.NombreRest);
            TextView DescripcionTW = (TextView) view.findViewById(R.id.DescripRest);
            TextView UbicacionTW = (TextView)view.findViewById(R.id.UbicacionRest);
            TextView HorarioTW = (TextView) view.findViewById(R.id.HorarioRest);
            ImageView imagen= (ImageView) view.findViewById(R.id.FotoRestaurante);

            StorageReference imaginesRestaurante = StorageDB.getInstance().imaginesRestaurante.child(restaurante.getCodigo());
            //-KkdQF54Mr5Xj4cCycUd
            //-KkdPFHZin_6zmo9r7va

            Glide.with(imagen.getContext())
                    .using(new FirebaseImageLoader())
                    .load(imaginesRestaurante)
                    .centerCrop()
                    .into(imagen);


            NombreTW.setText(restaurante.getNombre());
            DescripcionTW.setText(restaurante.getDescripcion());
            HorarioTW.setText(restaurante.getHorario().toString());
            UbicacionTW.setText("Aqui va la ubicacion del restaurante:"+restaurante.getCodigo());

        }
    }
}
