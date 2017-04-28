package com.example.usuario.sodamovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AcercaDeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        getSupportActionBar().setTitle("Acerca De");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LlenarListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
// app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void LlenarListView() {
        String[] integrantes = {
                "Andrey Castillo Meléndez 402310457",
                "Daniel Montealegre Vindas 116300970",
                "Jose Pablo Obando Rosales 116340814",
                "Josué David Aguilar Ulate 402300598",
				"AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH",

        };

        ArrayAdapter<String> adaptador =new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, integrantes);
        ListView milistview = (ListView) findViewById(R.id.listViewIntegrantes);
        milistview.setAdapter(adaptador);
    }
}
