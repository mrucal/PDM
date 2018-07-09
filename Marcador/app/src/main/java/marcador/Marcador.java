package marcador;

import android.content.Intent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 20/05/2018.
 */

public class Marcador {

    public static final int FUTBOL = 0;
    public static final int BALONCESTO = 1;

    public static final int LOCAL = 0;
    public static final int VISITANTE = 1;

    private List<Punto> historial;
    private List<List<Falta>> historialFaltas;

    private int[] faltas_parte = {0, 0, 0, 0};
    private int[] faltas_consecutivas = {0, 0, 0, 0};

    private int puntuacion;

    private int tipo;


    public Marcador(int tipo) {
        assert tipo != FUTBOL || tipo != BALONCESTO;

        historial = new ArrayList<Punto>();
        if (tipo == BALONCESTO) {
            historialFaltas = new ArrayList<>();
            for (int i = 0; i < 4; i++)
                historialFaltas.add(new ArrayList<Falta>());
        } else
            historialFaltas = null;
        puntuacion = 0;
        this.tipo = tipo;
    }

    public void addPunto(int punto, String tiempo, int parte, TextView marcadorTextView) {
        assert punto < -2 || punto > 2;
        int punto_menos = 0,i = historial.size()-1, k = 0;
        if (puntuacion + punto >= 0) {
            if(punto > 0) {
                puntuacion += punto;
                historial.add(new Punto(tipo, punto, puntuacion, tiempo, parte));
            }else {
                while (historial.get(i).getPunto()<0) {
                    i--; k++;
                }
                punto_menos = historial.get(i-k).getPunto();
                puntuacion -= punto_menos;
                historial.add(new Punto(tipo, -punto_menos, puntuacion, tiempo, parte));
            }
            marcadorTextView.setText(Integer.toString(puntuacion));
        }else
            puntuacion = 0;
    }

    void addPunto(int punto, String tiempo, int parte, String comentario, TextView marcadorTextView) {
        assert punto < -2 || punto > 2;
        int punto_menos = 0, i=historial.size()-1, k=0;
        if (puntuacion + punto >= 0) {
            if(punto > 0) {
                puntuacion += punto;
                historial.add(new Punto(tipo, punto, puntuacion, tiempo, parte, comentario));
            }else {
                while (historial.get(i).getPunto()<0) {
                    i--; k++;
                }
                punto_menos = historial.get(i-k).getPunto();
                puntuacion -= punto_menos;
                historial.add(new Punto(tipo, -punto_menos, puntuacion, tiempo, parte, comentario));
            }
            marcadorTextView.setText(Integer.toString(puntuacion));
        }
        else
            puntuacion = 0;


    }

    public void addFalta(int punto, String tiempo, int parte, Marcador contrario, TextView marcadorTextView, TextView faltasTextView) {
        assert punto < -1 || punto > 1 || tipo != BALONCESTO;

        if (faltas_parte[parte - 1] < 4 && faltas_parte[parte - 1] + punto >= 0) {

            faltas_consecutivas[parte - 1] += punto;
            contrario.faltas_consecutivas[parte - 1] = 0;
            if (faltas_consecutivas[parte - 1] == 3)
                contrario.addPunto(1, tiempo, parte, "3 faltas consecutivas", marcadorTextView);
            if (faltas_consecutivas[parte - 1] == 2 && punto == -1)
                contrario.addPunto(-1, tiempo, parte, "Anulado 3 faltas consecutivas", marcadorTextView);

            faltas_parte[parte - 1] += punto;
            faltasTextView.setText(Integer.toString(faltas_parte[parte - 1]));

            historialFaltas.get(parte - 1).add(new Falta(punto, faltas_parte[parte - 1], tiempo, parte));
        }
    }

    public Intent getIntent(Intent i, int equipo) {
        i.putExtra("puntuacion_" + equipo, puntuacion);
        ArrayList<String> h = getHistorialPuntosString();
        if (tipo == BALONCESTO)
            h.addAll(getHistorialFaltasString());
        i.putStringArrayListExtra("historial_" + equipo, h);
        i.putExtra("historial_int_" + equipo, getHistorialPuntosInt());
        i.putExtra("historial_tiempos_" + equipo, getHistorialTiempos());

        return i;
    }

    public List<Punto> getHistorialPuntos() {
        return historial;
    }

    public ArrayList<String> getHistorialPuntosString() {
        ArrayList<String> als = new ArrayList<String>();
        als.add("GOLES:");
        if (tipo == BALONCESTO)
            als.set(0, "PUNTOS:");
        for (Punto p : historial)
            als.add("\t" + p.toString());
        return als;
    }

    public int[] getHistorialPuntosInt() {
        ArrayList<Integer> hi = new ArrayList();
        for (Punto p : historial)
            if (p.getPunto() > 0)
                hi.add(p.getPunto());
            else
                if(hi.size() > 0)
                    hi.remove(hi.size()-1);
        int[] puntos = new int[hi.size()];
        for (int i = 0; i < hi.size(); i++)
            puntos[i] = hi.get(i);
        return puntos;
    }

    public String[] getHistorialTiempos() {
        ArrayList<String> ht = new ArrayList();
        for (Punto p : historial)
            if (p.getPunto() > 0)
                if(tipo != BALONCESTO)
                    ht.add(p.getTiempo());
                else
                    ht.add(p.getTiempo()+"º"+p.getParte());
            else if(ht.size() > 0)
                ht.remove(ht.size()-1);
        String[] tiempos = new String[ht.size()];
        for (int i = 0; i < ht.size(); i++)
            tiempos[i] = ht.get(i);
        return tiempos;
    }

    public ArrayList<String> getHistorialFaltasString() {
        ArrayList<String> als = new ArrayList<String>();
        als.add("FALTAS:");
        for (List<Falta> a : historialFaltas)
            for (Falta f : a)
                als.add("\t" + f.toString());
        return als;
    }

}
