package com.example.usuario.sodamovil;

import com.example.usuario.sodamovil.Entidades.Comida;
import android.view.View;
import com.example.usuario.sodamovil.Entidades.Horario;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USUARIO on 20/04/2017.
 */
// Se debe crear en el paquete com.example...
// Forma de uso:
// VariablesGlobales vg = VariablesGlobales.getInstance(); vg.setMitexto("Hola");    int i = vg.getMivalor();
public class VariablesGlobales {

    public LatLng posicionAgregarRestaurante;
    private Horario horario;
    private Restaurante restauranteActual;
    private Comida comidaActual;
    public Restaurante restauranteAgregar;
    private String ubicacion;
    private List<Restaurante> restaurantesResultadoBuscar= new ArrayList<>();
    public View vista;


    private static VariablesGlobales instance = null;

    protected VariablesGlobales() {}
    public static VariablesGlobales getInstance() {
        if(instance == null) {instance = new VariablesGlobales(); }
        return instance;
    }

    public LatLng getPosicionAgregarRestaurante() {
        return posicionAgregarRestaurante;
    }

    public void setPosicionAgregarRestaurante(LatLng posicionAgregarRestaurante) {
        this.posicionAgregarRestaurante = posicionAgregarRestaurante;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Restaurante getRestauranteActual() {
        return restauranteActual;
    }

    public void setRestauranteActual(Restaurante restauranteActual) {
        this.restauranteActual = restauranteActual;
    }

    public List<Restaurante> getRestaurantesResultadoBuscar() {
        return restaurantesResultadoBuscar;
    }

    public void setRestaurantesResultadoBuscar(List<Restaurante> restaurantesResultadoBuscar) {
        this.restaurantesResultadoBuscar = restaurantesResultadoBuscar;
    }

    public Comida getComidaActual() {
        return comidaActual;
    }

    public void setComidaActual(Comida comidaActual) {
        this.comidaActual = comidaActual;}

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}// fin de la clase de variables globales
