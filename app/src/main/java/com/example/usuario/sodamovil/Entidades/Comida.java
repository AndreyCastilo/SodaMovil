package com.example.usuario.sodamovil.Entidades;

import com.example.usuario.sodamovil.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josue on 6/5/2017.
 */

public class Comida {
    private String idDrawable;
    private String idFirebase;
    private String idRestaurante;
    private String nombre;
    private float precio;
    public Comida(){

    }

    public Comida(String idDrawable, String idFirebase, String idRestaurante, String nombre, float precio) {
        this.idDrawable = idDrawable;
        this.idFirebase = idFirebase;
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.precio = precio;
    }




    public String getIdFirebase() {
        return idFirebase;
    }

    public void setIdFirebase(String idFirebase) {
        this.idFirebase = idFirebase;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdDrawable(String idDrawable) {
        this.idDrawable = idDrawable;
    }

    public float getPrecio() {
        return precio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdDrawable() {
        return idDrawable;
    }

}