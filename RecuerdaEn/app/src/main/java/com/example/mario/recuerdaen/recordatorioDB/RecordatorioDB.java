package com.example.mario.recuerdaen.recordatorioDB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class RecordatorioDB {

    private List<Recordatorio> recordatorios = null;
    private RecordatorioDBRoom dbRoom ;

    public RecordatorioDB(Context context){
        dbRoom = RecordatorioDBRoom.getInstance(context);
        recordatorios = getRecordatorios();
    }

    @SuppressLint("StaticFieldLeak")
    public List<Recordatorio> getRecordatorios() {
        if(recordatorios == null)
            actualizarListaRecordatorios();
        return recordatorios;
    }

    @SuppressLint("StaticFieldLeak")
    public void actualizarListaRecordatorios(){
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    recordatorios = dbRoom.getRecordatorioDao().getAllRecordatorios();
                    return null;
                }
            }.execute().get();
        } catch (Exception e) {recordatorios = new ArrayList<>();}
    }

    @SuppressLint("StaticFieldLeak")
    public void addRecordatorio(final Recordatorio recordatorio){

        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    dbRoom.getRecordatorioDao().insert(recordatorio);
                    recordatorios = dbRoom.getRecordatorioDao().getAllRecordatorios();
                    return null;
                }
            }.execute().get();
        } catch (Exception ignored) {}
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteRecordatorio(final Recordatorio recordatorio){

        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    dbRoom.getRecordatorioDao().delete(recordatorio);
                    recordatorios = dbRoom.getRecordatorioDao().getAllRecordatorios();
                    return null;
                }
            }.execute().get();
        } catch (Exception ignored) {}
    }

    @SuppressLint("StaticFieldLeak")
    public void activarRecordatorio(final String nombre, final String descripcion, final boolean activar){
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Recordatorio r_i = dbRoom.getRecordatorioDao().getRecordatorio(nombre,descripcion);
                    r_i.setActivado(activar);
                    dbRoom.getRecordatorioDao().update(r_i);
                    recordatorios = dbRoom.getRecordatorioDao().getAllRecordatorios();
                    return null;
                }
            }.execute().get();
        } catch (Exception ignored) {}
    }

    @SuppressLint("StaticFieldLeak")
    public void updateRecordatorio(final String nombre, final String descripcion, final Recordatorio recordatorio){

        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Recordatorio r_i  =dbRoom.getRecordatorioDao().getRecordatorio(nombre, descripcion);
                    r_i.setLat(recordatorio.getLat());
                    r_i.setLon(recordatorio.getLon());
                    r_i.setRadio(recordatorio.getRadio());
                    r_i.setNombre(recordatorio.getNombre());
                    r_i.setDescripcion(recordatorio.getDescripcion());
                    dbRoom.getRecordatorioDao().update(r_i);
                    recordatorios = dbRoom.getRecordatorioDao().getAllRecordatorios();
                    return null;
                }
            }.execute().get();
        } catch (Exception ignored) {}
    }
}
