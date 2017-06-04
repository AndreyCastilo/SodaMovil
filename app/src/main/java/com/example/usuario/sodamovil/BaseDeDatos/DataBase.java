package com.example.usuario.sodamovil.BaseDeDatos;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.usuario.sodamovil.Entidades.Comida;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.Entidades.Usuario;
import com.example.usuario.sodamovil.VariablesGlobales;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;



public class DataBase {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private static DataBase instance = null;

    protected DataBase() {
        initFirebase();
    }

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }


    private void initFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    public FirebaseDatabase getmFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    public void setmFirebaseDatabase(FirebaseDatabase mFirebaseDatabase) {
        this.mFirebaseDatabase = mFirebaseDatabase;
    }

    public DatabaseReference getmDatabaseReference() {
        return mDatabaseReference;
    }

    public void setmDatabaseReference(DatabaseReference mDatabaseReference) {
        this.mDatabaseReference = mDatabaseReference;
    }

    public void agregarUsuario(Usuario usuario) {
        String key = mDatabaseReference.child("Usuario").push().getKey();
        usuario.setIdFirebase(key);
        mDatabaseReference.child("Usuario").child(key).setValue(usuario.toMap());

    }

    public Restaurante agregarRestaurante(Restaurante restaurante, String email, final Bitmap imagenRestaurante, final ProgressDialog progressDialog) {
        String  emailSinPunto= email.replace(".","");
        final String key = mDatabaseReference.child("Restaurante").child(emailSinPunto).push().getKey();
        restaurante.setCodigo(key);
        restaurante.setUsuario(email);
        mDatabaseReference.child("Restaurantes_Todos").child(key).setValue(restaurante.toMap());
        mDatabaseReference.child("Restaurante").child(emailSinPunto).child(key).setValue(restaurante.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    StorageDB storageDB= StorageDB.getInstance();
                    storageDB.guardarImagenRestauranteBitMap(imagenRestaurante,key);
                    progressDialog.dismiss();
                }

            }
        });
        return restaurante;
    }
    public Comida agregarComida(Restaurante restaurante,Comida comida,final Bitmap imagenComida, final ProgressDialog progressDialog){
        DatabaseReference mRef = mDatabaseReference.child("Comida").child(restaurante.getCodigo()); // id del restaurante
        final String key =mRef.push().getKey();
        comida.setIdFirebase(key);
        comida.setIdRestaurante(restaurante.getCodigo());
        mRef.child(key).setValue(comida).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    StorageDB storageDB= StorageDB.getInstance();
                    storageDB.guardarImagenComidaBitMap(imagenComida,key);
                    progressDialog.dismiss();
                }
            }
        });;
        return comida;

    }

    public void actualizarRestaurantesUsuario(Restaurante restaurante,Usuario usuario) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> Valores = new HashMap<>();
        usuario.getRestaurantes().add(restaurante.getCodigo());
        childUpdates.put("/Usuario/" + usuario.getIdFirebase(), usuario.toMap());
        mDatabaseReference.updateChildren(childUpdates);
    }

    /*public void actualizarRestaurante(Restaurante restaurante,String email) {
        String emailSinpunto;
        if(email.contains("."))
            emailSinpunto = email.replace(".","");
        else emailSinpunto=email;
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> childUpdatesTodosRestaurantes = new HashMap<>();
        Map<String, Object> Valores = new HashMap<>();
        childUpdates.put("/Restaurante/" + emailSinpunto +"/" +restaurante.getCodigo(), restaurante.toMap());
        childUpdatesTodosRestaurantes.put("/Restaurantes_Todos/" +restaurante.getCodigo(), restaurante.toMap());
        mDatabaseReference.updateChildren(childUpdates);
        mDatabaseReference.updateChildren(childUpdatesTodosRestaurantes);
    }*/

    public void actualizarRestaurante(final Restaurante restaurante, String email, final Bitmap imagenRestaurante, final ProgressDialog progressDialog) {
        String emailSinpunto;
        if(email.contains("."))
            emailSinpunto = email.replace(".","");
        else emailSinpunto=email;
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> childUpdatesTodosRestaurantes = new HashMap<>();
        Map<String, Object> Valores = new HashMap<>();
        childUpdates.put("/Restaurante/" + emailSinpunto +"/" +restaurante.getCodigo(), restaurante.toMap());
        childUpdatesTodosRestaurantes.put("/Restaurantes_Todos/" +restaurante.getCodigo(), restaurante.toMap());
        mDatabaseReference.updateChildren(childUpdates);
        mDatabaseReference.updateChildren(childUpdatesTodosRestaurantes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    StorageDB storageDB= StorageDB.getInstance();
                    storageDB.updateImagenRestauranteBitMap(imagenRestaurante,restaurante.getCodigo());
                    progressDialog.dismiss();
                }

            }
        });
    }

}