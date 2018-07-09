package com.example.mario.marcador;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import listener.ItemLista;
import listener.ListaAdapter;
import marcador.Marcador;

/**
 * Created by Mario on 21/05/2018.
 */

public class ResultadoActivity extends Activity {

    String nombre_equipo_local, nombre_equipo_visitante;
    int tiempo_total,tipo,color_local, color_visitante;
    String[] tiempos_punto_local, tiempos_punto_visitante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        Intent i = getIntent();

        tipo = i.getIntExtra("tipo", Marcador.FUTBOL);
        final int puntuacion_local = i.getIntExtra("puntuacion_0",0);
                final int puntuacion_visitante = i.getIntExtra("puntuacion_1",0);
        final ArrayList<String> historial_local = i.getStringArrayListExtra("historial_0");
        final int[] historial_int_local = i.getIntArrayExtra("historial_int_0");
        tiempos_punto_local = i.getStringArrayExtra("historial_tiempos_0");

        tiempos_punto_visitante = i.getStringArrayExtra("historial_tiempos_1");
        final ArrayList<String> historial_visitante = i.getStringArrayListExtra("historial_1");
        final int[] historial_int_visitante = i.getIntArrayExtra("historial_int_1");
        nombre_equipo_local = i.getStringExtra("nombre_equipo_local");
        nombre_equipo_visitante = i.getStringExtra("nombre_equipo_visitante");
        tiempo_total = i.getIntExtra("tiempo_total",0);

        if(tipo == Marcador.FUTBOL)
            ((ConstraintLayout) (findViewById(R.id.constraintResultado))).setBackgroundResource(R.drawable.fondofutbolblanco);
        if(tipo == Marcador.BALONCESTO)
            ((ConstraintLayout) (findViewById(R.id.constraintResultado))).setBackgroundResource(R.drawable.fondobaloncestoblanco);

        if(puntuacion_local > puntuacion_visitante){
            ((LinearLayout)(findViewById(R.id.layoutLocal))).setBackgroundResource(R.drawable.ganashape);
            color_local = ColorTemplate.rgb("#00ff2a");
            ((LinearLayout)(findViewById(R.id.layoutVisitante))).setBackgroundResource(R.drawable.pierdeshape);
            color_visitante = ColorTemplate.rgb("#FFFC3037");
        }else
            if(puntuacion_local < puntuacion_visitante){
                ((LinearLayout)(findViewById(R.id.layoutLocal))).setBackgroundResource(R.drawable.pierdeshape);
                color_local = ColorTemplate.rgb("#FFFC3037");
                ((LinearLayout)(findViewById(R.id.layoutVisitante))).setBackgroundResource(R.drawable.ganashape);
                color_visitante = ColorTemplate.rgb("#00ff2a");
            }else{
                ((LinearLayout)(findViewById(R.id.layoutLocal))).setBackgroundResource(R.drawable.pierdeshape);
                color_local = ColorTemplate.rgb("#FF5D67F4");
                ((LinearLayout)(findViewById(R.id.layoutVisitante))).setBackgroundResource(R.drawable.pierdeshape);
                color_visitante = ColorTemplate.rgb("#FF01A2FF");
            }

        ((TextView)(findViewById(R.id.puntuacionLocal))).setText(Integer.toString(puntuacion_local));
        ((TextView)(findViewById(R.id.puntuacionVisitante))).setText(Integer.toString(puntuacion_visitante));

        ((TextView)(findViewById(R.id.equipoLocalResultado))).setText(nombre_equipo_local);
        ((TextView)(findViewById(R.id.equipoVisitanteResultado))).setText(nombre_equipo_visitante);


        ItemLista[] il_local = new ItemLista[historial_local.size()];
        for(int k = 0; k < historial_local.size(); k++)
            il_local[k] = new ItemLista(historial_local.get(k));
        ItemLista[] il_visitante = new ItemLista[historial_visitante.size()];
        for(int k = 0; k < historial_visitante.size(); k++)
            il_visitante[k] = new ItemLista(historial_visitante.get(k));

        ((ListView) findViewById(R.id.listLocal)).setAdapter(new ListaAdapter(ResultadoActivity.this, il_local));
        ((ListView) findViewById(R.id.listVisitante)).setAdapter(new ListaAdapter(ResultadoActivity.this, il_visitante));


        ((Button) findViewById(R.id.boton_grafica)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo_info = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.tabla, null);
                LineChart chart = (LineChart) view.findViewById(R.id.chart);

                LineDataSet dataSet_local = getLineDataSet(tiempos_punto_local,historial_int_local,nombre_equipo_local+"-"+puntuacion_local, color_local);
                LineDataSet dataSet_visitante = getLineDataSet(tiempos_punto_visitante,historial_int_visitante,nombre_equipo_visitante+"-"+puntuacion_visitante, color_visitante);
                dataSet_local.setLineWidth(8);
                dataSet_visitante.setLineWidth(4);
                LineData lineData = new LineData();
                lineData.addDataSet(dataSet_local);
                lineData.addDataSet(dataSet_visitante);
                chart.setData(lineData);
                Description d = new Description(); d.setText("");
                chart.setDescription(d);
                setLabels(chart.getXAxis());
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.getXAxis().setTextSize(15);
                chart.getAxisLeft().setTextSize(15);
                chart.getAxisRight().setDrawLabels(false);
                chart.getAxisRight().setDrawGridLines(false);
                chart.getAxisLeft().setAxisMinimum(0f);
                chart.getLegend().setTextSize(20);
                chart.invalidate();

                dialogo_info.setView(view);
                dialogo_info.show();
            }
        });

        ((Button) findViewById(R.id.boton_inicio)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultadoActivity.this,MainActivity.class);
                startActivity(i);
            }
        });



        ((Button) findViewById(R.id.boton_cerrar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });


    }

    int[] getTiempos(String[] historial){
        int[] tiempos = new int[historial.length];
        for(int i = 0; i < historial.length; i++){
            if(tipo == Marcador.FUTBOL)
                tiempos[i] = getTiempo(historial[i].substring(historial[i].indexOf(':')-2));
            if(tipo == Marcador.BALONCESTO)
                tiempos[i] = getTiempo(historial[i].substring(historial[i].indexOf(':')-2))+
                        (tiempo_total/4)*(Integer.parseInt(historial[i].substring(historial[i].indexOf('º')+1,historial[i].indexOf('º')+2))-1);
        }

        return tiempos;
    }

    int getTiempo(String historial_i){
        return (Integer.parseInt(historial_i.substring(0,2))*60)+Integer.parseInt(historial_i.substring(3,5));
    }

    LineDataSet getLineDataSet(String[] historial, int[] historial_int, String label, int color){
        int[] tiempos_int = getTiempos(historial);

        List<Entry> entries = new ArrayList<Entry>();
        int puntos = 0;
        entries.add(new Entry(0, 0 ));
        for (int i = 0; i < historial_int.length; i++) {
            entries.add(new Entry(((float)tiempos_int[i]/(float)tiempo_total)*100, puntos ));
            puntos += historial_int[i];
            entries.add(new Entry(((float)tiempos_int[i]/(float)tiempo_total)*100, puntos ));
        }
        entries.add(new Entry(100, puntos ));

        int radio = 2;
        LineDataSet lds = new LineDataSet(entries, label);
        lds.setColors(color);
        lds.setCircleColor(color);
        lds.setCircleRadius(radio);

        return lds;
    }

    void setLabels(XAxis xaxis){
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int tiempo = (int)(Math.ceil((value/100)*tiempo_total));
                String minutos = tiempo/60>=10 ? Integer.toString(tiempo/60) : "0" + tiempo/60 ;
                String segundos = tiempo%60>=10 ? Integer.toString(tiempo%60) : "0" + tiempo%60 ;
                return minutos+":"+segundos;
            }
        };
        xaxis.setGranularity(1f);
        xaxis.setValueFormatter(formatter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode,event);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent i = new Intent(ResultadoActivity.this, MarcadorActivity.class);
            i.putExtra("tipo", tipo);
            i.putExtra("equipoLocal", nombre_equipo_local);
            i.putExtra("equipoVisitante", nombre_equipo_visitante);
            i.putExtra("tiempo", "");
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
