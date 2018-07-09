package marcador;

/**
 * Created by Mario on 20/05/2018.
 */

public class Falta {

    private int punto;
    private int total;
    private String tiempo;
    private int parte;

    public Falta(int punto, int total, String tiempo, int parte){
        this.punto = punto;
        this.total = total;
        this.tiempo = tiempo;
        this.parte = parte;
    }

    public String toString(){
        if(parte == 1 || parte ==3)
            return punto + " (" + total +") "+ tiempo + " " + parte + "er Cuarto";
        else
            return punto + " (" + total +") "+ tiempo + " " + parte + "º Cuarto";
    }
}
