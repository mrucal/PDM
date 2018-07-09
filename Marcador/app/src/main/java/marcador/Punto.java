package marcador;

/**
 * Created by Mario on 20/05/2018.
 */

public class Punto {

    private int tipo;
    private int punto;
    private int total;
    private String tiempo;
    private int parte;
    private String comentario;

    public Punto(int tipo, int punto, int total, String tiempo, int parte){
        this.tipo = tipo;
        this.punto = punto;
        this.total = total;
        this.tiempo = tiempo;
        this.parte = parte;
        this.comentario = "";
    }

    public Punto(int tipo, int punto, int total, String tiempo, int parte,String comentario){
        this.tipo = tipo;
        this.punto = punto;
        this.total = total;
        this.tiempo = tiempo;
        this.parte = parte;
        this.comentario = " [" + comentario + "]";
    }

    public int getPunto(){return punto;}

    public String getTiempo(){return tiempo;}

    public int getParte(){return parte;}

    public String toString(){
        if (tipo == Marcador.FUTBOL)
            return punto + " (" + total + ") " + tiempo + " "+ parte + "ª Parte" + comentario;
        if (tipo == Marcador.BALONCESTO)
            if(parte == 1 || parte == 3)
                return punto + " (" + total + ") " + tiempo + " "+ parte + "er Cuarto" + comentario;
            else
                return punto + " (" + total + ") " + tiempo + " "+ parte + "º Cuarto" + comentario;
        return null;
    }
}
