package com.example.usuario.sodamovil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.R.attr.data;
import static com.example.usuario.sodamovil.AgregarRestauranteActivity.HORARIO_REQUEST;
import static com.example.usuario.sodamovil.AgregarRestauranteActivity.PLACE_PICKER_REQUEST;

public class AgregarResturanteUbicacion extends AppCompatActivity {

    EditText direccion;
    EditText ubicacion;
    private VariablesGlobales vg;
    private LatLng latlng;

    static int PLACE_PICKER_REQUEST = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_resturante_ubicacion);
        vg = VariablesGlobales.getInstance();
        direccion = (EditText) findViewById(R.id.direccionAgregarRestaurante);
        final Button MiBoton = (Button) findViewById(R.id.btnIrAgregarRestauranteImagen);
        ubicacion = (EditText) findViewById(R.id.ubicacionAgregarRestaurante);
            if(vg.getPosicionAgregarRestaurante()!=null){
                MiBoton.setEnabled(true);
                MiBoton.setAlpha(1);
                ubicacion.setText("Editar");
            }
            else{
                ubicacion.setText("Agregar");
                MiBoton.setEnabled(false);
                MiBoton.setAlpha(.5f);
            }
        ubicacion.setClickable(true);
        ubicacion.setFocusable(false);
        ubicacion.setFocusableInTouchMode(false);

        // alambramos el boton


        //Programamos el evento onclick

        MiBoton.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                vg.setPosicionAgregarRestaurante(latlng);
                vg.restauranteAgregar.setUbicacion(direccion.getText().toString());
                Intent intento = new Intent(getApplicationContext(), AgregarRestauranteImagen.class);
                startActivity(intento);

            }

        });
        direccion.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() > 0 && latlng!=null){
                    MiBoton.setEnabled(true);
                    MiBoton.setAlpha(1);
                }
                else{
                    MiBoton.setEnabled(false);
                    MiBoton.setAlpha(.5f);
                }
            }
        });
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder= new PlacePicker.IntentBuilder();
                Intent intent;
                ubicacion.setEnabled(false);
                try {
                    if(vg.getPosicionAgregarRestaurante()!=null){
                        intent = builder.build(AgregarResturanteUbicacion.this);
                        startActivityForResult(intent,PLACE_PICKER_REQUEST);
                    }
                    else{
                        intent = builder.build(AgregarResturanteUbicacion.this);
                        startActivityForResult(intent,PLACE_PICKER_REQUEST);
                    }


                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==PLACE_PICKER_REQUEST){
            ubicacion.setEnabled(true);
            if(resultCode==RESULT_OK){
                //Mensaje(data.getExtras().toString());
                Place place = PlacePicker.getPlace(this,data);
                String address = ""+place.getAddress();
                latlng = place.getLatLng();
                direccion.setText(address);
                ubicacion.setText("Editar");
                vg.setUbicacion(address);
            }
        }
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
