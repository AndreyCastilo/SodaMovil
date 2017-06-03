package com.example.usuario.sodamovil.Fragmentos;


import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.usuario.sodamovil.AgregarComida;
import com.example.usuario.sodamovil.BaseDeDatos.DataBase;
import com.example.usuario.sodamovil.BaseDeDatos.StorageDB;
import com.example.usuario.sodamovil.Entidades.Comida;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.MainActivity;
import com.example.usuario.sodamovil.R;
import com.example.usuario.sodamovil.VariablesGlobales;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.usuario.sodamovil.R.id.btnAgregarComida;
import static com.example.usuario.sodamovil.R.id.miniatura_comida;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MenuFragmento extends Fragment {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Comida,ComidaHolder> mAdapter;
    public MenuFragmento(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_menu, container, false);
        FloatingActionButton btnAgregarComida= (FloatingActionButton) view.findViewById(R.id.btnFloatAgregarComida);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        reciclador.setLayoutManager(layoutManager);
        DataBase db=DataBase.getInstance();
        DatabaseReference ref = db.getmDatabaseReference().child("Comida").child(VariablesGlobales.getInstance().getRestauranteActual().getCodigo()); //Deberia pedir el codigo( getCodigo) del restaurante actual. Variables.g...getRes
        mAdapter = new FirebaseRecyclerAdapter<Comida, ComidaHolder>(
                Comida.class,
                R.layout.item_lista_menu,
                ComidaHolder.class,
                ref
        ) {

            @Override
            public void onBindViewHolder(ComidaHolder viewHolder, int position) {
                Comida model = getItem(position);
                populateViewHolder(viewHolder, model, position);

            }


            @Override
            protected void populateViewHolder(ComidaHolder viewHolder, Comida model, int position) {

                viewHolder.setNombre(model.getNombre());
                viewHolder.setPrecio(Double.toString(model.getPrecio()));
                StorageReference imaginesRestaurante = StorageDB.getInstance().imaginesComidas.child(model.getIdFirebase()); //Aqui iria la key de la comida, y sin el jpg. EJ:  model.setIdFirebase();
                Glide.with(viewHolder.itemView.getContext())
                        .using(new FirebaseImageLoader())
                        .load(imaginesRestaurante)
                        .centerCrop()
                        .into(viewHolder.imagen);
            }

            ;

        };

        esDuennoRestaurante(view);

        reciclador.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

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
                FloatingActionButton btnAgregarComida= (FloatingActionButton) view.findViewById(R.id.btnFloatAgregarComida);
                if(esDuenno){
                    //btnAgregarComida.setVisibility(View.VISIBLE);
                    btnAgregarComida.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intento = new Intent(getApplicationContext(), AgregarComida.class);
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
