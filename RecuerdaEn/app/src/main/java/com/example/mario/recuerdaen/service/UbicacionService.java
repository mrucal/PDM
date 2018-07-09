package com.example.mario.recuerdaen.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.mario.recuerdaen.R;
import com.example.mario.recuerdaen.recordatorioDB.Recordatorio;
import com.example.mario.recuerdaen.recordatorioDB.RecordatorioDB;

import java.util.ArrayList;


public class UbicacionService extends Service {

    private final int ESTADO_DENTRO = 0;
    private final int ESTADO_FUERA = 1;

    RecordatorioDB recordatorios;
    ArrayList<Integer> estados;

    LocationListener mLocationListener;
    LocationManager mLocationManager;

    public UbicacionService() {
        super();
        recordatorios = new RecordatorioDB(getBaseContext());
        estados = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        recordatorios = new RecordatorioDB(getBaseContext());
        estados = new ArrayList<>();

        actualizarRecordatorios();

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                comprobarUbicaciones(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    1, mLocationListener);
        }
        super.onCreate();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(intent!=null) {
            actualizarRecordatorios();
            pararServicio();
        }
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    public void startAlarm(int estado, String titulo, String texto){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        if(estado == ESTADO_DENTRO)
            titulo = "Llegada: " + titulo;
        if(estado == ESTADO_FUERA)
            titulo = "Salida: " + titulo;
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    public void actualizarRecordatorios(){
        recordatorios.actualizarListaRecordatorios();
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location localizacion_actual = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
            for(Recordatorio r: recordatorios.getRecordatorios())
                estados.add(getEstado(r,localizacion_actual));
        }
    }

    public void comprobarUbicaciones(Location localizacion_actual){
        Location localizacion_i;
        Recordatorio recordatorio_i;
        for(int i = 0; i < recordatorios.getRecordatorios().size(); i++){
            recordatorio_i = recordatorios.getRecordatorios().get(i);
            if(recordatorio_i.isActivado()) {
                localizacion_i = new Location(localizacion_actual);
                localizacion_i.setLatitude(recordatorio_i.getLat());
                localizacion_i.setLongitude(recordatorio_i.getLon());
                if (estados.get(i) == ESTADO_FUERA && getEstado(recordatorio_i, localizacion_actual) == ESTADO_DENTRO) {
                    estados.set(i, ESTADO_DENTRO);
                    startAlarm(estados.get(i), recordatorio_i.getNombre(), recordatorio_i.getDescripcion());
                } else if (estados.get(i) == ESTADO_DENTRO && getEstado(recordatorio_i, localizacion_actual) == ESTADO_FUERA) {
                    estados.set(i, ESTADO_FUERA);
                    startAlarm(estados.get(i), recordatorio_i.getNombre(), recordatorio_i.getDescripcion());
                }
            }
        }
    }

    public int getEstado(Recordatorio recordatorio, Location localizacion_actual){
        Location localizacion;
        localizacion = new Location(localizacion_actual);
        localizacion.setLatitude(recordatorio.getLat());
        localizacion.setLongitude(recordatorio.getLon());
        if(localizacion_actual.distanceTo(localizacion) <= recordatorio.getRadio())
            return ESTADO_DENTRO;
        else
            return ESTADO_FUERA;
    }

    public void pararServicio() {
        boolean todos_desactivados = true;
        if(recordatorios.getRecordatorios().size()>0)
            for (Recordatorio r : recordatorios.getRecordatorios())
                if (r.isActivado()) {
                    todos_desactivados = false;
                    break;
                }
        if(todos_desactivados) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationManager = null;
            stopSelf();
        }
    }
}
