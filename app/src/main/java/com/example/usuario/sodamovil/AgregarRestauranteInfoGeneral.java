package com.example.usuario.sodamovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AgregarRestauranteInfoGeneral extends AppCompatActivity {

    private Button btnIrAHorario;
    private EditText descripcionAgregarRes;
    private EditText telefonoAgregarRes;
    private VariablesGlobales vg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_restaurante_info_general);
        vg= VariablesGlobales.getInstance();
        Mensaje(vg.restauranteAgregar.getNombre());
        // alambramos el boton

        btnIrAHorario = (Button) findViewById(R.id.btnIrAgregarRestauranteHorario);
        descripcionAgregarRes = (EditText) findViewById(R.id.agregarDescripcionRestaurante);
        telefonoAgregarRes = (EditText) findViewById(R.id.agregarTelefonoRestaurante);
        //Programamos el evento onclick
        if(vg.restauranteAgregar!=null){
            descripcionAgregarRes.setText(vg.restauranteAgregar.getDescripcion());
            telefonoAgregarRes.setText(vg.restauranteAgregar.getTelefono());
        }
        else{
            descripcionAgregarRes.setText("");
            telefonoAgregarRes.setText("");
        }
        btnIrAHorario.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                if(descripcionAgregarRes.getText().length()>0 && telefonoAgregarRes.getText().length()>0){
                    vg.restauranteAgregar.setDescripcion(descripcionAgregarRes.getText().toString());
                    vg.restauranteAgregar.setTelefono(telefonoAgregarRes.getText().toString());
                    Intent intento = new Intent(getApplicationContext(), AgregarHorarioRestauranteActivity.class);
                    startActivity(intento);
                }
                // escriba lo que desea hacer

            }

        });
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
