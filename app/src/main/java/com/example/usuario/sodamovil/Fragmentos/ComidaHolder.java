package com.example.usuario.sodamovil.Fragmentos;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usuario.sodamovil.R;

import static com.example.usuario.sodamovil.R.id.miniatura_comida;
public   class ComidaHolder extends RecyclerView.ViewHolder {
    // Campos respectivos de un item
    View mView;
    public TextView nombre;
    public TextView precio;
    public ImageView imagen;

    public ComidaHolder(View v) {
        super(v);
        mView=v;
        nombre = (TextView) v.findViewById(R.id.nombre_comida);
        precio = (TextView) v.findViewById(R.id.precio_comida);
        imagen = (ImageView) v.findViewById(R.id.miniatura_comida);
    }


    public void setNombre(String nombre) {
        this.nombre.setText(nombre);
    }


    public void setPrecio(String precio) {
        this.precio.setText("₡"+precio);
    }

    public void setImagenUri(Uri imagen) {
        this.imagen.setImageURI(imagen);
    }
    public void setImagen(int id) {
        this.imagen.setImageResource(id);
    }
}
