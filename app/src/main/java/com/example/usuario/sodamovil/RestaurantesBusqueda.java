package com.example.usuario.sodamovil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.sodamovil.BaseDeDatos.DataBase;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.Entidades.RestauranteSuggestion;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Danel on 6/1/2017.
 */

public class RestaurantesBusqueda extends AppCompatActivity {
    //ListAdapter mAdapter;
    ListView listView;
    private List<Restaurante> restaurantes = new ArrayList<Restaurante>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurantes_lista_menu);
        listView = (ListView) findViewById(R.id.lista_mis_restaurantes);
        restaurantes= VariablesGlobales.getInstance().getRestaurantesResultadoBuscar();
        populateListView();
        registerClickCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAdapter.cleanup();
    }


    private void populateListView() {
        ArrayAdapter<Restaurante> adapter = new MyListAdapterRestaurantes();
        listView.setAdapter(adapter);
    }

    private void registerClickCallback() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Restaurante restauranteActual = restaurantes.get(position);
                final DataBase db= DataBase.getInstance();
                db.getmDatabaseReference().child("Restaurantes_Todos").child(restauranteActual.getCodigo()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Restaurante restaurante= dataSnapshot.getValue(Restaurante.class);
                        VariablesGlobales.getInstance().setRestauranteActual(restaurante);
                        Intent intento = new Intent(getApplicationContext(), RestauranteActivity.class);
                        startActivity(intento);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });
    }

    private class MyListAdapterRestaurantes extends ArrayAdapter<Restaurante> {
        public MyListAdapterRestaurantes() {
            super(RestaurantesBusqueda.this, R.layout.busqueda_restaurante_lista, restaurantes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.busqueda_restaurante_lista, parent, false);
            }

            // Find the car to work with.
            Restaurante restauranteActual = restaurantes.get(position);

            // Fill the view
            //ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
            //imageView.setImageResource(currentCar.getIconID());

            // Make:
            TextView nombreRestauranteTxt = (TextView) itemView.findViewById(R.id.restaurante_nombre);
            nombreRestauranteTxt.setText(restauranteActual.getNombre());

            // Year:
            TextView descripcionRestauranteTxt = (TextView) itemView.findViewById(R.id.restaurante_descripcion);
            descripcionRestauranteTxt.setText("" + restauranteActual.getDescripcion());
            return itemView;
        }
    }

}