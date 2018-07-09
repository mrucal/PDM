package com.example.mario.marcador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import marcador.Marcador;

public class ConfiguracionActivity extends Activity {

    private Intent i;
    int tipo;
    String tiempo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipos);

        i = getIntent();
        tipo = i.getIntExtra("tipo", Marcador.FUTBOL);

        if(tipo == Marcador.FUTBOL) {
            ((ConstraintLayout) (findViewById(R.id.activityEquipos_layout))).setBackgroundResource(R.drawable.fondofutbolblanco);
            ((RadioGroup)(findViewById(R.id.radiogrupo))).setVisibility(View.INVISIBLE);
        }
        if(tipo == Marcador.BALONCESTO)
            ((ConstraintLayout)(findViewById(R.id.activityEquipos_layout))).setBackgroundResource(R.drawable.fondobaloncestoblanco);


        ((Button)(findViewById(R.id.botonAceptarEquipos))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ConfiguracionActivity.this,MarcadorActivity.class);
                i.putExtra("tipo", tipo);
                i.putExtra("equipoLocal",((EditText)(findViewById(R.id.etEquipoLocal))).getText().toString());
                i.putExtra("equipoVisitante",((EditText)(findViewById(R.id.etEquipoVisitante))).getText().toString());

                tiempo = "00:08";
                if((((RadioButton)(findViewById(R.id.rb8)))).isChecked())
                    tiempo = "00:08";

                if((((RadioButton)(findViewById(R.id.rb10)))).isChecked())
                    tiempo = "00:10";

                if((((RadioButton)(findViewById(R.id.rb12)))).isChecked())
                    tiempo = "00:12";

                i.putExtra("tiempo",tiempo);
                startActivity(i);

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode,event);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent i = new Intent(ConfiguracionActivity.this,MainActivity.class);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
