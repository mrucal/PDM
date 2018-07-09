package com.example.mario.marcador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import listener.*;
import marcador.Crono;
import marcador.Marcador;

public class MarcadorActivity extends Activity {

    private static int tipo = Marcador.FUTBOL;

    private Crono crono;

    public static Marcador marcadorLocalFutbol;
    private static Marcador marcadorVisitanteFutbol;
    public static Marcador marcadorLocalBaloncesto;
    private static Marcador marcadorVisitanteBaloncesto;

    private Intent intent;
    private static String equipoLocal = "Local";
    private static String equipoVisitante = "Visitante";
    private String tiempo = "00:08";

    private static int parte;

    private static boolean sonido;

    private static TextView faltasLocal;
    private static TextView faltasVisitante;

    private static TextView tiempoFutbol;
    private static TextView tiempoBaloncesto;

    private static Button botonPlayFutbol;
    private static Button botonPlayBaloncesto;

    private static TextView marcadorLocalFutbolTextView;
    private static TextView marcadorVisitanteFutbolTextView;
    private static TextView marcadorLocalBaloncestoTextView;
    private static TextView marcadorVisitanteBaloncestoTextView;

    SharedPreferences prefs;

    public static String getNombreEquipoLocal(){
        return equipoLocal;
    }

    public static String getNombreEquipoVisitante(){
        return equipoVisitante;
    }

    public static int getTipo(){
        return tipo;
    }

    public static int getParte(){
        return parte;
    }

    public static void setParte(int n){
        parte = n;
    }

    public static boolean getSonido(){return sonido;}

    public static int idSonidoFutbol;
    public static int idSonidoBaloncesto;

    public static SoundPool sonidoFutbol;
    public static SoundPool sonidoBaloncesto;

    public static Marcador getMarcadorLocal(){
        if(tipo == Marcador.FUTBOL)
            return marcadorLocalFutbol;
        if(tipo == Marcador.BALONCESTO)
            return marcadorLocalBaloncesto;
        return null;
    }

    public static Marcador getMarcadorVisitante(){
        if(tipo == Marcador.FUTBOL)
            return marcadorVisitanteFutbol;
        if(tipo == Marcador.BALONCESTO)
            return marcadorVisitanteBaloncesto;
        return null;
    }

    public static TextView getTiempoTextView(){
        if(tipo == Marcador.FUTBOL)
            return tiempoFutbol;
        if(tipo == Marcador.BALONCESTO)
            return tiempoBaloncesto;
        return null;
    }

    public static Button getBotonPlay(){
        if(tipo == Marcador.FUTBOL)
            return botonPlayFutbol;
        if(tipo == Marcador.BALONCESTO)
            return botonPlayBaloncesto;
        return null;
    }

    public static TextView getMarcadorTextView(int equipo){
        if(tipo == Marcador.FUTBOL) {

            if (equipo == Marcador.LOCAL)
                return marcadorLocalFutbolTextView;
            if (equipo == Marcador.VISITANTE)
                return marcadorVisitanteFutbolTextView;
        }

        if(tipo == Marcador.BALONCESTO) {

            if (equipo == Marcador.LOCAL)
                return marcadorLocalBaloncestoTextView;
            if (equipo == Marcador.VISITANTE)
                return marcadorVisitanteBaloncestoTextView;

        }
        return null;
    }

    public static TextView getFaltasLocal(){
        return faltasLocal;
    }

    public static TextView getFaltasVisitante(){
        return faltasVisitante;
    }

    public static TextView getFaltasTextView(int equipo){
        if(equipo == Marcador.LOCAL)
            return faltasLocal;
        if(equipo == Marcador.VISITANTE)
            return faltasVisitante;
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        tipo = intent.getIntExtra("tipo",Marcador.FUTBOL);
        equipoLocal = intent.getStringExtra("equipoLocal");
        if (equipoLocal.equals("")) equipoLocal = "Local";
        equipoVisitante = intent.getStringExtra("equipoVisitante");
        if (equipoVisitante.equals("")) equipoVisitante = "Visitante";

        parte = 1;

        prefs = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        sonido = prefs.getBoolean("sonido",true);

        if(tipo == Marcador.FUTBOL) {

            setContentView(R.layout.activity_futbol);

            if(sonido)
                ((Button) findViewById(R.id.boton_sonido)).setBackgroundResource(R.drawable.boton_sonido);
            else
                ((Button) findViewById(R.id.boton_sonido)).setBackgroundResource(R.drawable.boton_mute);

            sonidoFutbol = new SoundPool.Builder().build();
            idSonidoFutbol = sonidoFutbol.load(this, R.raw.futbol_fin, 1);

            tiempoFutbol = (TextView) findViewById(R.id.tiempoFutbol);
            botonPlayFutbol = (Button) findViewById(R.id.playFutbol);
            marcadorLocalFutbolTextView = (TextView)findViewById(R.id.marcadorLocalFutbol);
            marcadorVisitanteFutbolTextView = (TextView)findViewById(R.id.marcadorVisitanteFutbol);

            ((TextView) findViewById(R.id.equipoLocalFutbol)).setText(equipoLocal);
            ((TextView) findViewById(R.id.equipoVisitanteFutbol)).setText(equipoVisitante);

            crono = new Crono(this,(Chronometer) findViewById(R.id.cronometroFutbol), "00:10");

            marcadorLocalFutbol = new Marcador(tipo);
            marcadorVisitanteFutbol = new Marcador(tipo);

            ((Button) findViewById(R.id.botonMasLocalFutbol)).setOnClickListener(new BotonIncrementosListener(marcadorLocalFutbol,1,crono, Marcador.LOCAL));
            ((Button) findViewById(R.id.botonMenosLocalFutbol)).setOnClickListener(new BotonIncrementosListener(marcadorLocalFutbol,-1,crono, Marcador.LOCAL));
            ((Button) findViewById(R.id.botonMasVisitanteFutbol)).setOnClickListener(new BotonIncrementosListener(marcadorVisitanteFutbol,1,crono, Marcador.VISITANTE));
            ((Button) findViewById(R.id.botonMenosVisitanteFutbol)).setOnClickListener(new BotonIncrementosListener(marcadorVisitanteFutbol,-1,crono, Marcador.VISITANTE));

            ((Button) findViewById(R.id.playFutbol)).setOnClickListener(new PlayPauseListener (crono));

        }

        if(tipo == Marcador.BALONCESTO){

            setContentView(R.layout.activity_baloncesto);

            if(sonido)
                ((Button) findViewById(R.id.boton_sonido)).setBackgroundResource(R.drawable.boton_sonido);
            else
                ((Button) findViewById(R.id.boton_sonido)).setBackgroundResource(R.drawable.boton_mute);


            sonidoBaloncesto = new SoundPool.Builder().build();
            idSonidoBaloncesto = sonidoBaloncesto.load(this, R.raw.baloncesto_fin, 1);

            tiempoBaloncesto = (TextView) findViewById(R.id.tiempoBaloncesto);
            botonPlayBaloncesto = (Button) findViewById(R.id.playBaloncesto);
            marcadorLocalBaloncestoTextView = (TextView)findViewById(R.id.marcadorLocalBaloncesto);
            marcadorVisitanteBaloncestoTextView = (TextView)findViewById(R.id.marcadorVisitanteBaloncesto);
            faltasLocal = (TextView)findViewById(R.id.faltasLocalBaloncesto);
            faltasVisitante = (TextView)findViewById(R.id.faltasVisitanteBaloncesto);

            ((TextView) findViewById(R.id.equipoLocalBaloncesto)).setText(equipoLocal);
            ((TextView) findViewById(R.id.equipoVisitanteBaloncesto)).setText(equipoVisitante);

            if(!intent.getStringExtra("tiempo").equals(""))
                tiempo = intent.getStringExtra("tiempo");
            crono = new Crono(this,(Chronometer) findViewById(R.id.cronometroBaloncesto),tiempo);

            marcadorLocalBaloncesto = new Marcador(tipo);
            marcadorVisitanteBaloncesto = new Marcador(tipo);

            ((Button) findViewById(R.id.botonMas1LocalBaloncesto)).setOnClickListener(new BotonIncrementosListener(marcadorLocalBaloncesto,1,crono, Marcador.LOCAL));
            ((Button) findViewById(R.id.botonMas1VisitanteBaloncesto)).setOnClickListener(new BotonIncrementosListener(marcadorVisitanteBaloncesto,1,crono, Marcador.VISITANTE));

            ((Button) findViewById(R.id.botonMas2LocalBaloncesto)).setOnClickListener(new BotonIncrementosListener(marcadorLocalBaloncesto,2,crono, Marcador.LOCAL));
            ((Button) findViewById(R.id.botonMas2VisitanteBaloncesto)).setOnClickListener(new BotonIncrementosListener(marcadorVisitanteBaloncesto,2,crono, Marcador.VISITANTE));

            ((Button) findViewById(R.id.botonMas3LocalBaloncesto)).setOnClickListener(new BotonIncrementosListener(marcadorLocalBaloncesto,3,crono, Marcador.LOCAL));
            ((Button) findViewById(R.id.botonMas3VisitanteBaloncesto)).setOnClickListener(new BotonIncrementosListener(marcadorVisitanteBaloncesto,3,crono, Marcador.VISITANTE));

            ((Button) findViewById(R.id.botonMasLocalBaloncesto)).setOnClickListener(new BotonIncrementarFaltasListener(marcadorLocalBaloncesto,1,crono, Marcador.LOCAL,marcadorVisitanteBaloncesto));
            ((Button) findViewById(R.id.botonMenosFaltasLocalBaloncesto)).setOnClickListener(new BotonIncrementarFaltasListener(marcadorLocalBaloncesto,-1,crono, Marcador.LOCAL,marcadorVisitanteBaloncesto));
            ((Button) findViewById(R.id.botonMasVisitanteBaloncesto)).setOnClickListener(new BotonIncrementarFaltasListener(marcadorVisitanteBaloncesto,1,crono, Marcador.VISITANTE,marcadorLocalBaloncesto));
            ((Button) findViewById(R.id.botonMenosFaltasVisitanteBaloncesto)).setOnClickListener(new BotonIncrementarFaltasListener(marcadorVisitanteBaloncesto,-1,crono, Marcador.VISITANTE,marcadorLocalBaloncesto));

            ((Button) findViewById(R.id.botonMenosLocalBaloncesto)).setOnClickListener(new BotonIncrementosListener(marcadorLocalBaloncesto,-1,crono, Marcador.LOCAL));
            ((Button) findViewById(R.id.botonMenosVisitanteBaloncesto)).setOnClickListener(new BotonIncrementosListener(marcadorVisitanteBaloncesto,-1,crono, Marcador.VISITANTE));

            ((Button) findViewById(R.id.playBaloncesto)).setOnClickListener(new PlayPauseListener (crono));

        }

        ((Button) findViewById(R.id.boton_inicio)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MarcadorActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        ((Button) findViewById(R.id.boton_sonido)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sonido = !sonido;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("sonido", sonido);
                editor.commit();
                if(sonido)
                    ((Button) findViewById(R.id.boton_sonido)).setBackgroundResource(R.drawable.boton_sonido);
                else
                    ((Button) findViewById(R.id.boton_sonido)).setBackgroundResource(R.drawable.boton_mute);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode,event);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent i = new Intent(getApplicationContext(),ConfiguracionActivity.class);
            i.putExtra("tipo", tipo);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
