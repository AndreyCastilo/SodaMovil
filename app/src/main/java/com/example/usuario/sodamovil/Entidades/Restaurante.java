package com.example.usuario.sodamovil.Entidades;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Danel on 4/12/2017.
 */

public class Restaurante {

    private String codigo;
    private String nombre;
    private String descripcion;
    private Bitmap imagen; //http://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database
    private Double latitudesH;
    private Double latitudesV;
    private Horario horario;
    private String  usuario ;
    private String telefono;
    private String ubicacion;
    private int scrollX;
    private int scrollY;


    public Restaurante(String codigo, String nombre, String descripcion, Bitmap imagen, Double latitudesH, Double latitudesV, Horario horario, String usuario,String telefono, String ubicacion,int scrollX,int scrollY) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.latitudesH = latitudesH;
        this.latitudesV = latitudesV;
        this.usuario=usuario;
        this.horario=horario;
        this.telefono=telefono;
        this.ubicacion=ubicacion;
        this.scrollX=scrollX;
        this.scrollY=scrollY;
    }

    public Restaurante() {
        this.codigo = "";
        this.nombre = "";
        this.descripcion = "";
        this.imagen = null;
        this.latitudesH = 0.0;
        this.latitudesV = 0.0;
        this.horario= new Horario();
        this.usuario= "";
        this.telefono="";
        this.ubicacion="";
        this.scrollX=0;
        this.scrollY=0;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public Double getLatitudesH() {
        return latitudesH;
    }

    public void setLatitudesH(Double latitudesH) {
        this.latitudesH = latitudesH;
    }

    public Double getLatitudesV() {
        return latitudesV;
    }

    public void setLatitudesV(Double latitudesV) {
        this.latitudesV = latitudesV;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getScrollX() {
        return scrollX;
    }

    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("codigo",  this.codigo);
        result.put("nombre", this.nombre);
        result.put("descripcion", this.descripcion);
        result.put("latitudesH", this.latitudesH);
        result.put("latitudesV",  this.latitudesV);
        result.put("horario", this.horario);
        result.put("usuario", this.usuario);
        result.put("telefono", this.telefono);
        result.put("ubicacion",this.ubicacion);
        result.put("scrollX",this.scrollX);
        result.put("scrollY",this.scrollY);
        return result;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}