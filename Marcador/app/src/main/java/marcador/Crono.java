package marcador;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Chronometer;

import listener.CronoListener;

import java.util.List;

/**
 * Created by Mario on 20/05/2018.
 */

public class Crono {

    private Context context;
    private Chronometer crono;
    private String parada;

    private boolean terminado;
    private long tiempo_parado;
    private boolean isplay;

    public Crono(Context context, Chronometer crono, String parada  ){
        this.context = context;
        this.crono = crono;
        this.crono.setOnChronometerTickListener (new CronoListener(this));
        this.parada = parada;
        this.terminado = false;
        this.tiempo_parado = 0;
        this.isplay = false;
    }

    public String getTiempo(){
        return this.crono.getText().toString();
    }

    public void start(){
        crono.setBase(SystemClock.elapsedRealtime() + tiempo_parado);
        this.tiempo_parado = 0;
        isplay = !isplay;
        crono.start();
    }

    public void pause(){
        this.tiempo_parado = crono.getBase() - SystemClock.elapsedRealtime();
        isplay = !isplay;
        crono.stop();
    }

    public void restart(){
        crono.setBase(SystemClock.elapsedRealtime());
        pause();
    }

    public boolean isTerminado(){
        return terminado;
    }

    public void setTerminado(boolean terminado){
        this.terminado = terminado;
    }

    public boolean isPlay(){
        return isplay;
    }

    public String getParada(){
        return parada;
    }

    public int getParadaSegundos(){
        return (Integer.parseInt(parada.substring(0,2))*60)+Integer.parseInt(parada.substring(3,5));
    }

    public Context getContext(){
        return context;
    }

}
