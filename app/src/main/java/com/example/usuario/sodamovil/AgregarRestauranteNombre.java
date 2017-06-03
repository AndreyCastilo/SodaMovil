package com.example.usuario.sodamovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.usuario.sodamovil.Entidades.Restaurante;

import static com.example.usuario.sodamovil.R.id.btnIrInfoGeneral;

public class AgregarRestauranteNombre extends AppCompatActivity {

    private Button btnIrAInfoGeneral;
    private EditText nombreAgregarRes;
    private VariablesGlobales vg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_restaurante_nombre);
        vg = VariablesGlobales.getInstance();

        // alambramos el boton

        btnIrAInfoGeneral = (Button) findViewById(R.id.btnIrInfoGeneral);
        nombreAgregarRes = (EditText) findViewById(R.id.nombreAgregarResturante) ;
        nombreAgregarRes.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() > 0)
                    btnIrAInfoGeneral.setEnabled(true);
                else
                    btnIrAInfoGeneral.setEnabled(false);

            }
        });

        btnIrAInfoGeneral.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                if(vg.restauranteAgregar==null){
                    vg.restauranteAgregar = new Restaurante();
                    vg.restauranteAgregar.setHorario(null);
                }
                vg.restauranteAgregar.setNombre(nombreAgregarRes.getText().toString());
                Intent intento = new Intent(getApplicationContext(), AgregarRestauranteInfoGeneral.class);
                startActivity(intento);

                // escriba lo que desea hacer

            }

        });

        if(vg.restauranteAgregar==null){
            btnIrAInfoGeneral.setEnabled(false);
            nombreAgregarRes.setText("");
        }
        else {
            nombreAgregarRes.setText(vg.restauranteAgregar.getNombre());
            btnIrAInfoGeneral.setEnabled(true);
        }


    }
}
