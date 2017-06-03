package com.example.usuario.sodamovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MisRestaurantes extends AppCompatActivity {
    FirebaseListAdapter mAdapter;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurantes_lista_menu);
        FirebaseAuth firebaseAuth;
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String  userEmail=user.getEmail().replace(".","");

        final ListView listView = (ListView) findViewById(R.id.lista_mis_restaurantes);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Restaurante").child(userEmail);

        mAdapter = new FirebaseListAdapter<Restaurante>(this, Restaurante.class, R.layout.busqueda_restaurante_lista,ref) {
            @Override
            protected void populateView(View view, Restaurante restaurante, int position) {
                ((TextView) view.findViewById(R.id.restaurante_nombre)).setText(restaurante.getNombre());
                ((TextView) view.findViewById(R.id.restaurante_descripcion)).setText(restaurante.getDescripcion());
            }
        };
        listView.setAdapter(mAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Restaurante restaurante = (Restaurante) mAdapter.getItem(position);
                Toast toast = Toast.makeText(getApplicationContext().getApplicationContext(), restaurante.getCodigo(), Toast.LENGTH_SHORT);
                toast.show();
                if(restaurante != null){
                    VariablesGlobales.getInstance().setRestauranteActual(restaurante);
                    Intent intento = new Intent(getApplicationContext(), RestauranteActivity.class);
                    startActivity(intento);
                }
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

}
