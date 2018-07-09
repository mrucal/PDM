package com.example.mario.recuerdaen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.recuerdaen.recordatorioDB.Recordatorio;
import com.example.mario.recuerdaen.recordatorioDB.RecordatorioDB;
import com.example.mario.recuerdaen.service.UbicacionService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;



public class MapActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap mapa;
    private double latitud, longitud;
    private double radio ;
    SeekBar radio_sb;

    private final double LATLON_ERROR = 1000;

    boolean editar;

    String nombre_editar, descripcion_editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //this.deleteDatabase("/data/data/com.example.mario.recuerdaen/databases/recordatorioDB.db");
        final RecordatorioDB recordatorios = new RecordatorioDB(this);

        float zoom = 12;
        latitud = longitud = LATLON_ERROR; radio = Recordatorio.MIN_RADIO;

        // En lugar de añadir un nuevo recordatorio, se edita uno existente
        Intent i = getIntent();
        editar = i.getBooleanExtra("editar",false);
        if(editar){
            latitud = i.getDoubleExtra("lat",LATLON_ERROR);
            longitud = i.getDoubleExtra("lon",LATLON_ERROR);
            radio = i.getDoubleExtra("radio",Recordatorio.MIN_RADIO);
            ((SeekBar)findViewById(R.id.radio)).setProgress((int)radio);
            nombre_editar = i.getStringExtra("nombre");
            ((TextView)findViewById(R.id.nombre)).setText(nombre_editar);
            descripcion_editar = i.getStringExtra("descripcion");
            ((TextView)findViewById(R.id.descripcion)).setText(descripcion_editar);
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        radio_sb = (SeekBar)findViewById(R.id.radio);
        radio_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radio = progress+Recordatorio.MIN_RADIO;
                añadirMarca();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        ((Button)findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = ((TextView)findViewById(R.id.nombre)).getText().toString();
                if(!nombre.equals("")) {
                    String descripcion = ((TextView)findViewById(R.id.descripcion)).getText().toString();
                    if(latitud != LATLON_ERROR && longitud != LATLON_ERROR) {
                        // Añadir el recordatorio a la base de datos
                        if(editar)
                            recordatorios.updateRecordatorio(nombre_editar,descripcion_editar,new Recordatorio(nombre, descripcion, latitud, longitud, radio));
                        else
                            recordatorios.addRecordatorio(new Recordatorio(nombre, descripcion, latitud, longitud, radio));
                        // Iniciar el servicio
                        startService( new Intent(getBaseContext(), UbicacionService.class));
                        // Volver a la actividad RecordatorioActivity
                        startActivity(new Intent(getBaseContext(), RecordatorioActivity.class));
                    }else
                        Toast.makeText(getApplicationContext(),"Indica una localización.", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(),"El nombre no puede ser vacío.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;
        mapa.getUiSettings().setZoomControlsEnabled(true);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)  {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setMapToolbarEnabled(false);
            mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // Obtener la última localización conocida
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            int v = 15;
            if(editar){
                latitude = latitud;
                longitude = longitud;
                añadirMarca();
                v = 17;
            }
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), v));

        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }

        mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.latitude,point.longitude), mapa.getCameraPosition().zoom));
                latitud = point.latitude;
                longitud = point.longitude;
                añadirMarca();
            }
        });
    }

    void añadirMarca(){
        mapa.clear();
        mapa.addMarker(new MarkerOptions().position(new LatLng(latitud,longitud)).title(""));
        mapa.addCircle(new CircleOptions().center(
                new LatLng(latitud,longitud))
                .radius(radio)
                .strokeColor(Color.BLUE)
                .strokeWidth(4)
                .fillColor(Color.argb(128,84,175,255)));
    }
}
