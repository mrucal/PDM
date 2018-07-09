package listener;

import android.content.Intent;
import android.media.SoundPool;
import android.view.View;
import android.widget.Chronometer;

import com.example.mario.marcador.MarcadorActivity;
import com.example.mario.marcador.R;
import com.example.mario.marcador.ResultadoActivity;

import marcador.Crono;
import marcador.Marcador;

/**
 * Created by Mario on 21/05/2018.
 */

public class CronoListener implements Chronometer.OnChronometerTickListener{

    private Crono crono;
    private boolean cambio;

    public CronoListener(Crono crono){
        this.crono = crono;
        this.cambio = false;
    }

    @Override
    public void onChronometerTick (final Chronometer chronometer){

        if (MarcadorActivity.getTipo() == Marcador.FUTBOL) {

            if (crono.getParada().contains(chronometer.getText().toString())) {
                chronometer.stop();
                crono.setTerminado(true);
                crono.getParadaSegundos();
                MarcadorActivity.getBotonPlay().setBackgroundResource(R.drawable.play);
                MarcadorActivity.getBotonPlay().setVisibility(View.INVISIBLE);


                playSonido(MarcadorActivity.sonidoFutbol, MarcadorActivity.idSonidoFutbol, 3000);

                Intent i = new Intent(crono.getContext(), ResultadoActivity.class);
                i.putExtra("tipo",MarcadorActivity.getTipo());
                MarcadorActivity.getMarcadorLocal().getIntent(i,Marcador.LOCAL);
                i.putExtra("nombre_equipo_local", MarcadorActivity.getNombreEquipoLocal());
                MarcadorActivity.getMarcadorVisitante().getIntent(i,Marcador.VISITANTE);
                i.putExtra("nombre_equipo_visitante", MarcadorActivity.getNombreEquipoVisitante());
                i.putExtra("tiempo_total",crono.getParadaSegundos());
                crono.getContext().startActivity(i);
            }
            if (crono.getTiempo().equals(/*"45:00"*/"00:05") && !cambio) {
                crono.pause();
                cambio = true;
                playSonido(MarcadorActivity.sonidoFutbol, MarcadorActivity.idSonidoFutbol, 3000);
                MarcadorActivity.getBotonPlay().setBackgroundResource(R.drawable.play);
                MarcadorActivity.setParte(2);
                MarcadorActivity.getTiempoTextView().setText("2ª Parte");
            }
        }
        if (MarcadorActivity.getTipo() == Marcador.BALONCESTO){
            if (crono.getParada().contains(crono.getTiempo())){
                if(!cambio)
                    if(MarcadorActivity.getParte()<4){
                        cambio = true;
                        crono.restart();

                        playSonido(MarcadorActivity.sonidoBaloncesto, MarcadorActivity.idSonidoBaloncesto, 0);

                        MarcadorActivity.getBotonPlay().setBackgroundResource(R.drawable.play);
                        MarcadorActivity.getFaltasLocal().setText("0");
                        MarcadorActivity.getFaltasVisitante().setText("0");
                        MarcadorActivity.setParte(MarcadorActivity.getParte()+1);
                        if(MarcadorActivity.getParte() == 1 || MarcadorActivity.getParte()==3)
                            MarcadorActivity.getTiempoTextView().setText(MarcadorActivity.getParte() + "er Cuarto");
                        else
                            MarcadorActivity.getTiempoTextView().setText(MarcadorActivity.getParte() + "º Cuarto");
                    }else {
                        chronometer.stop();
                        crono.setTerminado(true);
                        MarcadorActivity.getBotonPlay().setBackgroundResource(R.drawable.play);
                        MarcadorActivity.getBotonPlay().setVisibility(View.INVISIBLE);

                        playSonido(MarcadorActivity.sonidoBaloncesto, MarcadorActivity.idSonidoBaloncesto, 1000);

                        Intent i = new Intent(crono.getContext(), ResultadoActivity.class);
                        i.putExtra("tipo",MarcadorActivity.getTipo());
                        MarcadorActivity.getMarcadorLocal().getIntent(i,Marcador.LOCAL);
                        i.putExtra("nombre_equipo_local", MarcadorActivity.getNombreEquipoLocal());
                        MarcadorActivity.getMarcadorVisitante().getIntent(i,Marcador.VISITANTE);
                        i.putExtra("nombre_equipo_visitante", MarcadorActivity.getNombreEquipoVisitante());
                        i.putExtra("tiempo_total",crono.getParadaSegundos()*4);
                        crono.getContext().startActivity(i);
                    }
            }else
                cambio = false;
        }
    }

    private void playSonido(SoundPool sonido, int idSonido, int sleep){
        if(MarcadorActivity.getSonido()) {
            sonido.play(idSonido, 1, 1, 1, 0, 0);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) { }
        }
    }
}
