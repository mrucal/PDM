package listener;

import android.view.View;
import android.widget.Button;

import com.example.mario.marcador.MarcadorActivity;

import marcador.Crono;
import marcador.Marcador;

/**
 * Created by Mario on 20/05/2018.
 */

public class BotonIncrementarFaltasListener implements View.OnClickListener {

    private int incremento;
    private Marcador marcador;
    private Crono crono;
    private Marcador contrario;

    private int equipo;

    public BotonIncrementarFaltasListener(Marcador marcador, int incremento, Crono crono, int equipo, Marcador contrario){
        this.marcador = marcador;
        this.incremento = incremento;
        this.crono = crono;
        this.contrario = contrario;
        this.equipo = equipo;
    }

    @Override
    public void onClick(View v) {
        Button b = (Button)v;
        if(crono.isPlay() && !crono.isTerminado())
            marcador.addFalta(incremento, crono.getTiempo(), MarcadorActivity.getParte(), contrario, MarcadorActivity.getMarcadorTextView((equipo+1)%2), MarcadorActivity.getFaltasTextView(equipo));
    }
}
