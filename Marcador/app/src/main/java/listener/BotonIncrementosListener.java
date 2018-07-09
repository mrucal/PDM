package listener;

import android.view.View;
import android.widget.Button;

import com.example.mario.marcador.MarcadorActivity;

import marcador.Crono;
import marcador.Marcador;

/**
 * Created by Mario on 20/05/2018.
 */

public class BotonIncrementosListener implements View.OnClickListener {

    private int incremento;
    private Marcador marcador;
    private Crono crono;

    private int equipo;

    public BotonIncrementosListener(Marcador marcador, int incremento, Crono crono, int equipo){
        this.marcador = marcador;
        this.incremento = incremento;
        this.crono = crono;
        this.equipo = equipo;
    }

    @Override
    public void onClick(View v) {
        Button b = (Button)v;
        if(crono.isPlay() && !crono.isTerminado())
            marcador.addPunto(incremento, crono.getTiempo(), MarcadorActivity.getParte(), MarcadorActivity.getMarcadorTextView(equipo));
    }
}
