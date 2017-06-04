package com.example.usuario.sodamovil;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usuario.sodamovil.BaseDeDatos.DataBase;
import com.example.usuario.sodamovil.Entidades.Comida;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.google.android.gms.appindexing.AndroidAppUri;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import es.dmoral.toasty.Toasty;
import okhttp3.internal.Platform;

import retrofit2.http.HEAD;

import static com.example.usuario.sodamovil.AgregarRestauranteActivity.HORARIO_REQUEST;
import static com.example.usuario.sodamovil.AgregarRestauranteActivity.RESULT_LOAD_IMG;

public class AgregarComida extends AppCompatActivity {
    EditText nombreComida;
    EditText precioComida;
    EditText descripcionComida;
    ImageView imagenComida;
    Bitmap imagenComidaFirebase;
    ProgressDialog progressDialog;
    Button btnAgregarComida;
    Uri mCropImageUri;
    static int RESULT_LOAD_IMG=4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_comida);
        nombreComida= (EditText) findViewById(R.id.nombreComidaId);
        precioComida = (EditText) findViewById(R.id.precioComidaId);
        descripcionComida = (EditText) findViewById(R.id.descripcionComida);
        imagenComida= (ImageView) findViewById(R.id.imageViewComida);
        BitmapDrawable drawable = (BitmapDrawable) imagenComida.getDrawable();
        imagenComidaFirebase =  drawable.getBitmap();
        progressDialog = new ProgressDialog(this);


        btnAgregarComida = (Button) findViewById(R.id.btnAgregarComida);
        btnAgregarComida.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                AgregarComida();
            }

        });
        imagenComida.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                getImageFromGallery();
            }
        });

        btnAgregarComida.setEnabled(false);
        btnAgregarComida.setAlpha(.5f);

        nombreComida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0 && precioComida.getText().toString().length()>0  && descripcionComida.getText().toString().length()>0 ){
                    btnAgregarComida.setEnabled(true);
                    btnAgregarComida.setAlpha(1);
                }
                else{
                    btnAgregarComida.setEnabled(false);
                    btnAgregarComida.setAlpha(.5f);
                }}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        precioComida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0 && nombreComida.getText().toString().length()>0  && descripcionComida.getText().toString().length()>0 ){
                    btnAgregarComida.setEnabled(true);
                    btnAgregarComida.setAlpha(1);
                }
                else{
                    btnAgregarComida.setEnabled(false);
                    btnAgregarComida.setAlpha(.5f);
                }}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        descripcionComida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0 && nombreComida.getText().toString().length()>0  && precioComida.getText().toString().length()>0 ){
                    btnAgregarComida.setEnabled(true);
                    btnAgregarComida.setAlpha(1);
                }
                else{
                    btnAgregarComida.setEnabled(false);
                    btnAgregarComida.setAlpha(.5f);
                }}

            @Override
            public void afterTextChanged(Editable s) {}
        });


    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ///imagenComida.setImageURI(resultUri);
                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, resultUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    mCropImageUri = resultUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);

                    }
                }
                else{
                    try {
                        final InputStream imageStream = getContentResolver().openInputStream(resultUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imagenComida.setImageBitmap(selectedImage);
                        imagenComidaFirebase = selectedImage;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //Mensaje("ALGO PASO PERRITO");
                    }
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                CropImage.activity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }



    /*


     boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                mCropImageView.setImageUriAsync(imageUri);
            }


     */

    public void AgregarComida(){
        progressDialog.setMessage("Agregando comida...");
        progressDialog.show();
        String nombre= nombreComida.getText().toString();
        String precio= precioComida.getText().toString();
        String descripcion= descripcionComida.getText().toString();
        Comida comida= new Comida();
        comida.setNombre(nombre);
        comida.setPrecio(Float.parseFloat(precio));
        comida.setDescripcion(descripcion);
        Restaurante restauranteActual= VariablesGlobales.getInstance().getRestauranteActual();
        DataBase.getInstance().agregarComida(restauranteActual,comida,imagenComidaFirebase,progressDialog);
        limpiaForm();
        MensajeSuccess("Comida agregada exitosamente.");
        super.onBackPressed();
    }



    public void limpiaForm(){
        nombreComida.setText("");
        precioComida.setText("");
        descripcionComida.setText("");
        imagenComida.setImageResource(R.drawable.ic_no_image_available);
        BitmapDrawable drawable = (BitmapDrawable) imagenComida.getDrawable();
        imagenComidaFirebase =  drawable.getBitmap();
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};

/*
    public void getImageFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Escoja una imagen"), RESULT_LOAD_IMG);
    }
*/

    public void getImageFromGallery(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropWindowSize(0,0)
                .setMinCropResultSize(640,190)
                .start(this);
        // .setMinCropWindowSize(0,0)
    }

    public void MensajeSuccess(String mensaje){
        Toasty.success(this, mensaje, Toast.LENGTH_SHORT, true).show();
    }


}
