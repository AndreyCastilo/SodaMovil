package com.example.usuario.sodamovil.Fragmentos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.usuario.sodamovil.BaseDeDatos.StorageDB;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.Manifest;
import com.example.usuario.sodamovil.R;
import com.example.usuario.sodamovil.VariablesGlobales;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import retrofit2.http.HEAD;

import static com.example.usuario.sodamovil.R.id.container;



public class FragmentoContacto extends Fragment {
    String numero="";
    String latitud="";
    String longitud="";
    public FragmentoContacto() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragmento_contacto, container, false);
        View view = inflater.inflate(R.layout.fragmento_contacto, container, false);
        setRestaurante(view);
        OnclickDelImageView(view.findViewById(R.id.ButtonLlamada));
        OnclickDelImageView(view.findViewById(R.id.ButtonIr));
        return view; // debe comentar el otro return

    }



    public void OnclickDelImageView(View view) {
        // Ejemplo  OnclickDelImageView(R.id.MiImageView);

        ImageView miImageView = (ImageView) view;
        //  final String msg = miImageView.getText().toString();       // 2.  Programar el evento onclick
        miImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(msg.equals("Texto")){Mensaje("Texto en el botón ");};
                switch (v.getId()) {

                    case R.id.ButtonLlamada:
                        MarcarNumero(numero);

                        break;

                    case R.id.ButtonIr:
                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse("geo:"+latitud+","+longitud));
                        startActivity(i);

                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });


    }


    public void setRestaurante(View view){
        Restaurante restaurante= VariablesGlobales.getInstance().getRestauranteActual();
        if(restaurante != null){
            numero=restaurante.getTelefono();
            latitud=String.valueOf(restaurante.getLatitudesH());
            longitud=String.valueOf(restaurante.getLatitudesV());

        }
    }

    public void MarcarNumero (String numero){
        Intent i = new
                Intent(android.content.Intent.ACTION_DIAL,
                Uri.parse("tel:" + numero));
        startActivity(i);
    }
    ;

}

