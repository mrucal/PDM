package Biblioteca;

import com.example.mario.biblioteca.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mario on 27/05/2018.
 */

public class Libro {

    private String path;
    private String titulo;
    private String autor;

    private byte[] portada;

    private String fecha_agregado;

    private String formato;

    public Libro(String titulo, String autor, byte[] portada, String path){
        this.titulo = titulo;
        this.autor = autor;
        this.portada = portada;
        this.path = path;
        //getResources();R.drawable.estante)
        this.formato = "epub";
        this.fecha_agregado = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
    }

    public Libro(String titulo, String autor, byte[] portada, String path, String formato){
        this(titulo,autor,portada,path);
        assert formato != "epub" || formato != "pdf";
        this.formato = formato;
    }

    public Libro(String titulo, String autor, byte[] portada, String path, String formato, String fecha_agregado){
        this(titulo,autor,portada,path);
        assert formato != "epub" || formato != "pdf";
        this.formato = formato;
        this.fecha_agregado = fecha_agregado;
    }

    public String getAutor(){
        return autor;
    }

    public void setAutor(String autor){
        this.autor = autor;
    }

    public String getTitulo(){
        return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public byte[] getPortada(){ return this.portada; }

    public String getPath(){ return this.path;}

    public String getFormato(){ return this.formato; }

    public String getFecha_agregado(){ return fecha_agregado; }

}
