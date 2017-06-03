package com.example.usuario.sodamovil.Entidades;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by Danel on 6/1/2017.
 */

public class RestauranteSuggestion implements SearchSuggestion {

    String nombreRestaurante;
    String codigoFireBase;
    String descripcion;
    private boolean mIsHistory = false;

    public RestauranteSuggestion(String suggestion, String codigoFireBase,String descripcion) {
        this.codigoFireBase=codigoFireBase;
        this.nombreRestaurante = suggestion.toLowerCase();
        this.descripcion=descripcion;
    }


    public RestauranteSuggestion(Parcel source) {
        this.nombreRestaurante = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public String getCodigoFireBase() {
        return codigoFireBase;
    }

    public void setCodigoFireBase(String codigoFireBase) {
        this.codigoFireBase = codigoFireBase;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    public void setIsHistory(boolean val) {
         this.mIsHistory=val;
    }

    @Override
    public String getBody() {
        return this.nombreRestaurante;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RestauranteSuggestion> CREATOR = new Creator<RestauranteSuggestion>() {
        @Override
        public RestauranteSuggestion createFromParcel(Parcel in) {
            return new RestauranteSuggestion(in);
        }

        @Override
        public RestauranteSuggestion[] newArray(int size) {
            return new RestauranteSuggestion[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreRestaurante);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}
