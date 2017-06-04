package com.example.usuario.sodamovil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.usuario.sodamovil.BaseDeDatos.DataBase;
import com.example.usuario.sodamovil.BaseDeDatos.StorageDB;
import com.example.usuario.sodamovil.Entidades.Horario;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.Entidades.Usuario;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import es.dmoral.toasty.Toasty;
import retrofit2.http.HEAD;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class AgregarRestauranteActivity extends AppCompatActivity {

    EditText nombre_restaurante;
    EditText descripcion_restaurante;
    EditText horario_restaurante;
    EditText ubicacion_restaurante;
    ProgressDialog progressDialog;
    Bitmap imagenRestaurante;
    ImageView imagenRestauranteButton;
    EditText telefono_restaurante;

    static int  HORARIO_REQUEST = 1;
    static int  UBICACION_REQUEST = 1;
    static int PLACE_PICKER_REQUEST = 3;
    static int RESULT_LOAD_IMG=4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_restaurante);

        // alambramos el boton
        progressDialog = new ProgressDialog(this);
        Button MiBoton = (Button) findViewById(R.id.irAMapaRestaurante);

        Button MiBoton2 = (Button) findViewById(R.id.irAhorarioAgregar);
        Button AgregarRestaurante = (Button) findViewById(R.id.btnAgregarRestaurante);

        imagenRestauranteButton= (ImageView) findViewById(R.id.imagenViewRestaurante);



        nombre_restaurante = (EditText) findViewById(R.id.nombreReId);
        descripcion_restaurante = (EditText) findViewById(R.id.descripReId);
        ubicacion_restaurante = (EditText) findViewById(R.id.etUbicacion);
        horario_restaurante = (EditText) findViewById(R.id.etHorario);
        horario_restaurante.setEnabled(false);
        ubicacion_restaurante.setFocusable(false);
        telefono_restaurante=(EditText) findViewById(R.id.etTelf);

        //Programamos el evento onclick
        MiBoton.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                /*Intent intento = new Intent(getApplicationContext(), UbicacionRestauranteActivity.class);
                startActivityForResult(intento,UBICACION_REQUEST );*/
                PlacePicker.IntentBuilder builder= new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(AgregarRestauranteActivity.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }

        });

        MiBoton2.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                Intent intento = new Intent(getApplicationContext(), AgregarHorarioRestauranteActivity.class);
                startActivityForResult(intento,HORARIO_REQUEST );
            }

        });
/*
        imagenRestauranteButton.setOnClickListener(new View.OnClickListener(){


            @Override

            public void onClick(View arg0) {
                getImageFromGallery();
            }


        });
*/
        LlenarCamposRestaurante();


        AgregarRestaurante.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                AgregarRestaurante();
            }

        });
        getSupportActionBar().setTitle("Editar Restaurante");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //limpiaForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==PLACE_PICKER_REQUEST){
            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(this,data);
                String address = ""+place.getAddress();
                VariablesGlobales.getInstance().setPosicionAgregarRestaurante(place.getLatLng());
                ubicacion_restaurante.setText(address);
            }
        }
        if(requestCode==HORARIO_REQUEST){
            if(resultCode==RESULT_OK){
                horario_restaurante.setText(VariablesGlobales.getInstance().getHorario().toString());
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imagenRestauranteButton.setImageURI(resultUri);
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(resultUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imagenRestaurante=selectedImage;
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                    //Mensaje("ALGO PASO PERRITO");
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }





    private void AgregarRestaurante(){
        progressDialog.setMessage("Editando restaurante...");
        progressDialog.show();

        FirebaseAuth firebaseAuth;
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Horario horario;
        final String nombre=nombre_restaurante.getText().toString();
        String descripcion= descripcion_restaurante.getText().toString();
        String telef=telefono_restaurante.getText().toString();
        String ubicacion = ubicacion_restaurante.getText().toString();

        double latitud= VariablesGlobales.getInstance().getPosicionAgregarRestaurante().latitude;
        double longitud= VariablesGlobales.getInstance().getPosicionAgregarRestaurante().longitude;


        if(VariablesGlobales.getInstance().getHorario()!=null){
            horario = VariablesGlobales.getInstance().getHorario();
        }
        else{
            horario= new Horario();
        }

        final Restaurante restaurante = VariablesGlobales.getInstance().getRestauranteActual();
        restaurante.setNombre(nombre);
        restaurante.setDescripcion(descripcion);
        restaurante.setHorario(horario);
        restaurante.setLatitudesH(latitud);//latitud
        restaurante.setLatitudesV(longitud);//longitud
        restaurante.setTelefono(telef);
        restaurante.setUbicacion(ubicacion);
        restaurante.setImagen(imagenRestaurante);

        final DataBase db= DataBase.getInstance();
        Query query= db.getmDatabaseReference().child("Usuario").orderByChild("correo").equalTo(user.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        Usuario user = postSnapshot.getValue(Usuario.class);
                        //Mensaje("Restaurante editado exitosamente");
                        if(imagenRestaurante !=null){
                            //Mensaje("Entré a este...");
                            db.actualizarRestaurante(restaurante,user.getCorreo(),imagenRestaurante,progressDialog);
                        }
                        else{
                            Bitmap noImageIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_image_available);
                            db.actualizarRestaurante(restaurante,user.getCorreo(),noImageIcon,progressDialog);
                        }
                        //db.actualizarRestaurantesUsuario(restaurante,user);
                        MensajeSuccess("Restaurante editado exitosamente");
                        progressDialog.dismiss();
                        //limpiaForm();
                        Intent intento = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intento);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Mensaje("No habia nada!");
            }
        });
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};

    public void limpiaForm(){
        nombre_restaurante.setText("");
        descripcion_restaurante.setText("");
        ubicacion_restaurante.setText("");
        telefono_restaurante.setText("");
        horario_restaurante.setText("");
        VariablesGlobales.getInstance().posicionAgregarRestaurante=null;
    }



/*
    public void getImageFromGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);


    }
*/

    public void getImageFromGallery(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropWindowSize(0,0)
                .setMinCropResultSize(260,260)
                .setMaxCropResultSize(600,410)
                .start(this);
    }

    /////////////////////
    public void LlenarCamposRestaurante(){
        Restaurante rest = VariablesGlobales.getInstance().getRestauranteActual();
        nombre_restaurante.setText(rest.getNombre());
        descripcion_restaurante.setText(rest.getDescripcion());
        ubicacion_restaurante.setText(rest.getUbicacion());
        horario_restaurante.setText(rest.getHorario().toString());
        horario_restaurante.setEnabled(false);
        ubicacion_restaurante.setFocusable(false);
        telefono_restaurante.setText(rest.getTelefono());
        imagenRestaurante = rest.getImagen();
        imagenRestauranteButton.setImageBitmap(rest.getImagen());
        VariablesGlobales.getInstance().setPosicionAgregarRestaurante(new LatLng(rest.getLatitudesH(),rest.getLatitudesV()));
        VariablesGlobales.getInstance().setHorario(rest.getHorario());
        //VariablesGlobales.getInstance().restauranteAgregar.setHorario(rest.getHorario());
        StorageReference imaginesRestaurante = StorageDB.getInstance().imaginesRestaurante.child(rest.getCodigo());

        Glide.with(imagenRestauranteButton.getContext())
                .using(new FirebaseImageLoader())
                .load(imaginesRestaurante)
                .centerCrop()
                .into(imagenRestauranteButton);

        final ScrollView scroll = (ScrollView) findViewById(R.id.scrollViewEditar);

        scroll.setOnTouchListener(new View.OnTouchListener() {



            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Glide.with(imagenRestauranteButton.getContext())
                .using(new FirebaseImageLoader())
                .load(imaginesRestaurante)
                .into(imagenRestauranteButton);
        imagenRestauranteButton.scrollTo(0,rest.getScrollY());

    }

    @Override
    public void onResume(){
        super.onResume();
        LlenarCamposRestaurante();

    }

    public void MensajeSuccess(String mensaje){
        Toasty.success(this, mensaje, Toast.LENGTH_SHORT, true).show();
    }

}
