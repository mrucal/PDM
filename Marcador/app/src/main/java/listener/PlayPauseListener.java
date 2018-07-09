package listener;

import android.view.View;
import android.widget.Button;

import com.example.mario.marcador.MarcadorActivity;
import com.example.mario.marcador.R;

import marcador.Crono;

/**
 * Created by Mario on 21/05/2018.
 */

public class PlayPauseListener implements View.OnClickListener{


    private Crono crono;

    public PlayPauseListener(Crono crono){
        this.crono = crono;
    }

    @Override
    public void onClick(View v) {
        if(crono.isPlay()){
            crono.pause();
            MarcadorActivity.getBotonPlay().setBackgroundResource(R.drawable.play);
        }else{
            crono.start();
            MarcadorActivity.getBotonPlay().setBackgroundResource(R.drawable.pause);
        }

    }
}
