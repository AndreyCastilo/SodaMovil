package com.example.usuario.sodamovil.Fragmentos;


import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.usuario.sodamovil.AgregarComida;
import com.example.usuario.sodamovil.AgregarRestauranteActivity;
import com.example.usuario.sodamovil.BaseDeDatos.DataBase;
import com.example.usuario.sodamovil.BaseDeDatos.StorageDB;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.R;
import com.example.usuario.sodamovil.VariablesGlobales;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.usuario.sodamovil.R.id.container;
import static com.example.usuario.sodamovil.R.id.textView;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.android.gms.maps.CameraUpdateFactory.scrollBy;


public class Informacion extends Fragment {
    String numero="";
    String latitud="";
    String longitud="";
    public Informacion(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragmento_informacion, container, false);
        setRestaurante(inf);
        setHasOptionsMenu(true);
        esDuennoRestaurante(inf);
        return inf;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.menucontacto, menu);
        super.onCreateOptionsMenu(menu, inflater);
      //  return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemLlamada:
                MarcarNumero(numero);
                return false;
            case R.id.itemUbicacion:
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("geo:"+latitud+","+longitud));
                startActivity(i);
                return true;
            default:
                break;
        }

        return false;
    }




    public void setRestaurante(View view){
        final Restaurante restaurante=VariablesGlobales.getInstance().getRestauranteActual();
        if(restaurante != null){

            TextView NombreTW = (TextView) view.findViewById(R.id.NombreRest);
             TextView DescripcionTW = (TextView) view.findViewById(R.id.DescripRest);
            TextView UbicacionTW = (TextView)view.findViewById(R.id.UbicacionRest);
            TextView HorarioTW = (TextView) view.findViewById(R.id.HorarioRest);
            ImageView imagen= (ImageView) view.findViewById(R.id.FotoRestaurante);
            final ScrollView scroll = (ScrollView) view.findViewById(R.id.scrollViewInfo);

            scroll.setOnTouchListener(new View.OnTouchListener() {



                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            StorageReference imaginesRestaurante = StorageDB.getInstance().imaginesRestaurante.child(restaurante.getCodigo());
            //-KkdQF54Mr5Xj4cCycUd
            //-KkdPFHZin_6zmo9r7va

            Glide.with(imagen.getContext())
                    .using(new FirebaseImageLoader())
                    .load(imaginesRestaurante)
                    .into(imagen);
            imagen.scrollTo(0,restaurante.getScrollY());

            NombreTW.setText(restaurante.getNombre());
            DescripcionTW.setText(restaurante.getDescripcion());
            HorarioTW.setText(restaurante.getHorario().toString());
            UbicacionTW.setText(restaurante.getUbicacion());
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

    public void esDuennoRestaurante(final View view){

        FirebaseAuth firebaseAuth;
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final DataBase db= DataBase.getInstance();
        String  emailSinPunto= user.getEmail().replace(".","");
        db.getmDatabaseReference().child("Restaurante").child(emailSinPunto).addValueEventListener(new ValueEventListener() {
            boolean esDuenno;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Restaurante> restaurantes = new ArrayList<Restaurante>();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Restaurante rest = postSnapshot.getValue(Restaurante.class);
                    restaurantes.add(rest);
                }

                for(Restaurante rest: restaurantes){
                    if( rest.getCodigo().equals(VariablesGlobales.getInstance().getRestauranteActual().getCodigo()) ){
                        esDuenno = true;
                    }
                }
                FloatingActionButton btnAgregarComida= (FloatingActionButton) view.findViewById(R.id.btnFloatEditarRestaurante);
                if(esDuenno){
                    //btnAgregarComida.setVisibility(View.VISIBLE);
                    btnAgregarComida.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intento = new Intent(getApplicationContext(), AgregarRestauranteActivity.class);
                            startActivity(intento);

                        }
                    });
                }
                else{
                    btnAgregarComida.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
