package com.example.mario.recuerdaen.recordatorioDB;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mario on 28/06/2018.
 */
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "recordatorio_enity")
public class Recordatorio {

    public static final double MIN_RADIO = 15;

    @PrimaryKey(autoGenerate = true)
    private int id;

    private double lat;
    private double lon;
    private double radio;

    private String nombre;
    private String descripcion;

    private boolean activado;

    public Recordatorio(){
        this.lat = 0;
        this.lon = 0;
        radio = MIN_RADIO;
        nombre = "Localización";
        descripcion = "";
        activado = true;
    }

    public Recordatorio(double latitud, double longitud, double radio){
        this();
        this.lat = latitud;
        this.lon = longitud;
        this.radio = radio;
    }

    public Recordatorio(String nombre, String descripcion,double latitud, double longitud, double radio){
        this(latitud,longitud,radio);
        this.nombre = nombre;
        this.descripcion = descripcion;

    }

    public int getId() {
        return this.id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getRadio(){return radio;}

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }
}
