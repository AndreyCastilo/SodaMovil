package com.example.usuario.sodamovil.Fragmentos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.usuario.sodamovil.Entidades.Comida;
import com.example.usuario.sodamovil.R;


public class AdaptadorMenu {
/*
    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView precio;
        public ImageView imagen;

        public MenuViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombre_comida);
            precio = (TextView) v.findViewById(R.id.precio_comida);
            imagen = (ImageView) v.findViewById(R.id.miniatura_comida);
        }
    }

    public AdaptadorMenu() {
    }

    @Override
    public int getItemCount() {
        return Comida.COMIDAS_POPULARES.size();

    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_menu, viewGroup, false);

        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder viewHolder, int i) {
       Comida item = Comida.COMIDAS_POPULARES.get(i);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getIdDrawable())
                .centerCrop()
                .into(viewHolder.imagen);
        viewHolder.nombre.setText(item.getNombre());
        viewHolder.precio.setText("₡" + item.getPrecio());

    }

*/
}
