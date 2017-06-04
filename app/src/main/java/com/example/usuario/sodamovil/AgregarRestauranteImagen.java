package com.example.usuario.sodamovil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.sodamovil.BaseDeDatos.DataBase;
import com.example.usuario.sodamovil.Entidades.Horario;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.Entidades.Usuario;
import com.example.usuario.sodamovil.Utilidades.LockableScrollView;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import es.dmoral.toasty.Toasty;

import static com.example.usuario.sodamovil.AgregarRestauranteActivity.HORARIO_REQUEST;
import static com.example.usuario.sodamovil.AgregarRestauranteActivity.PLACE_PICKER_REQUEST;

public class AgregarRestauranteImagen extends AppCompatActivity {
    Bitmap imagenRestaurante;
    Button btnAgregar;
    ImageView imagenRestauranteButton;
    ImageView imagenRestauranteButtonEdit;
    ProgressDialog progressDialog;
    VariablesGlobales vg;
    ScrollView scrollView;

    boolean isBlockedScrollView;
    static int RESULT_LOAD_IMG=4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_restaurante_imagen);
        imagenRestauranteButton= (ImageView) findViewById(R.id.imageViewRestaurante);
        imagenRestauranteButtonEdit= (ImageView) findViewById(R.id.imageViewRestauranteEdit);
        btnAgregar = (Button) findViewById(R.id.botonAgregarRestaurante);
        progressDialog = new ProgressDialog(this);
        vg=VariablesGlobales.getInstance();
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        imagenRestauranteButtonEdit.setOnClickListener(new View.OnClickListener(){


            @Override

            public void onClick(View arg0) {
                getImageFromGallery();
                scrollView.scrollTo(0,0);
            }


        });

        // alambramos el TextView
        TextView MiTextView = (TextView) findViewById(R.id.textViewPortada);

        //Programamos el evento onclick

        MiTextView.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                scrollView.scrollTo(0,0);
                Mensaje("X: "+scrollView.getScrollX()+"  Y: "+scrollView.getScrollY());
            }

        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarRestaurante();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Mensaje("Entre");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(resultUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imagenRestaurante=selectedImage;
                    imagenRestauranteButton.setImageBitmap(imagenRestaurante);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Mensaje("ALGO PASO PERRITO");
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public void getImageFromGallery(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropWindowSize(0,0)
                .start(this);
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};


    private void AgregarRestaurante(){
        progressDialog.setMessage("Agregando restaurante...");
        progressDialog.show();

        FirebaseAuth firebaseAuth;
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        double latitud= vg.getPosicionAgregarRestaurante().latitude;
        double longitud= vg.getPosicionAgregarRestaurante().longitude;
        String ubicacion= vg.getUbicacion();

        final Restaurante restaurante = vg.restauranteAgregar;

        restaurante.setLatitudesH(latitud);//latitud
        restaurante.setLatitudesV(longitud);//longitud
        restaurante.setUbicacion(ubicacion);

        int scrollX = scrollView.getScrollX();
        int scrollY = scrollView.getScrollY();

        restaurante.setScrollX(scrollX);
        restaurante.setScrollY(scrollY);
        final DataBase db= DataBase.getInstance();
        Query query= db.getmDatabaseReference().child("Usuario").orderByChild("correo").equalTo(user.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        Usuario user = postSnapshot.getValue(Usuario.class);
                        MensajeSuccess("Restaurante agregado exitosamente");
                        if(imagenRestaurante !=null){
                            db.agregarRestaurante(restaurante,user.getCorreo(),imagenRestaurante,progressDialog);
                        }
                        else{
                            Bitmap noImageIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_image_available);
                            db.agregarRestaurante(restaurante,user.getCorreo(),noImageIcon,progressDialog);
                        }
                        db.actualizarRestaurantesUsuario(restaurante,user);
                        vg.restauranteAgregar = null;
                        Intent intento = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intento);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Mensaje("No habia nada!");
            }
        });
    }

    public void MensajeSuccess(String mensaje){
        Toasty.success(this, mensaje, Toast.LENGTH_SHORT, true).show();
    }
}
