package com.example.mario.marcador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import marcador.Marcador;

public class MainActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        ((Button)(findViewById(R.id.botonFubol))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ConfiguracionActivity.class);
                i.putExtra("tipo", Marcador.FUTBOL);
                startActivity(i);
            }
        });

        ((Button)(findViewById(R.id.botonBaloncesto))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ConfiguracionActivity.class);
                i.putExtra("tipo", Marcador.BALONCESTO);
                startActivity(i);
            }
        });

    }

}
