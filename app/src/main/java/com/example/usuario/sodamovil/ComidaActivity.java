package com.example.usuario.sodamovil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.usuario.sodamovil.BaseDeDatos.StorageDB;
import com.example.usuario.sodamovil.Entidades.Comida;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

public class ComidaActivity extends AppCompatActivity {

    Comida comidaActual;
    TextView NombreComidaActivity;
    TextView PrecioComidaActivity;
    TextView descripcionComidaActivity;
    ImageView FotoComida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);
        comidaActual=VariablesGlobales.getInstance().getComidaActual();
        NombreComidaActivity= (TextView) findViewById(R.id.NombreComidaActivity);
        PrecioComidaActivity= (TextView) findViewById(R.id.PrecioComidaActivity);
        descripcionComidaActivity= (TextView) findViewById(R.id.descripcionComidaActivity);

        FotoComida=(ImageView)findViewById(R.id.FotoComida);
        setTextViewComidas();
    }

    public void setTextViewComidas(){
        NombreComidaActivity.append(":"+comidaActual.getNombre());
        PrecioComidaActivity.append(":₡"+Float.toString(comidaActual.getPrecio()));
        descripcionComidaActivity.append(":"+comidaActual.getDescripcion());
        StorageReference imaginesRestaurante = StorageDB.getInstance().imaginesComidas.child(comidaActual.getIdFirebase());
        Glide.with(FotoComida.getContext())
                .using(new FirebaseImageLoader())
                .load(imaginesRestaurante)
                .centerCrop()
                .into(FotoComida);
    }
}
