package Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mario.biblioteca.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Biblioteca.Libro;

/**
 * Created by Mario on 27/05/2018.
 */

public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.LibrosViewHolder> {

    private ArrayList<Libro> libros;

    public static final int LINEAR = 0;
    public static final int GRID = 1;

    private int tipo = LINEAR;

    public AdaptadorLibros(ArrayList<Libro> libros, int tipo){
        this.libros = new ArrayList<>(libros);
        if(tipo == GRID) {
            int n = libros.size();
            for (int i = 0; i < ((3 - n % 3)%3) + 3; i++)
                this.libros.add(new Libro("", "", new byte[0], "", ""));
            while(this.libros.size()<12)
                this.libros.add(new Libro("", "", new byte[0], "", ""));
        }
        this.tipo = tipo;
    }

    public void setLibros(ArrayList<Libro> libros) {
        this.libros = libros;
    }

    public int getTipo(){return tipo;}

    public void setTipo(int tipo){this.tipo = tipo;}

    @Override
    public LibrosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(tipo == LINEAR)
            return  new LibrosViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_libro, viewGroup, false),tipo);
        else
            return  new LibrosViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_libro_grid, viewGroup, false),tipo);
    }

    @Override
    public void onBindViewHolder(LibrosViewHolder viewHolder, int pos) {
        viewHolder.bindLibro(libros.get(pos));
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }


    public static class LibrosViewHolder extends RecyclerView.ViewHolder {

        private ImageView portada;
        private TextView titulo;
        private TextView autor;

        private int tipo;

        private Bitmap pfd_bitmap;
        private Bitmap epub_bitmap;

        public LibrosViewHolder(View itemView, int tipo) {
            super(itemView);
            portada = (ImageView) itemView.findViewById(R.id.portada);
            titulo = (TextView)itemView.findViewById(R.id.titulo);
            autor = (TextView)itemView.findViewById(R.id.autor);
            this.tipo = tipo;
            try {
                pfd_bitmap = BitmapFactory.decodeStream(itemView.getContext().getAssets().open("pdf.png"));
                epub_bitmap = BitmapFactory.decodeStream(itemView.getContext().getAssets().open("epub.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void bindLibro(Libro libro) {
            ByteArrayInputStream bais = new ByteArrayInputStream(libro.getPortada());
            switch(libro.getFormato()){
                case "pdf":
                    portada.setImageBitmap(pfd_bitmap);
                    if(tipo == GRID){
                        if(libro.getTitulo().length()>15)
                            titulo.setText(libro.getTitulo().substring(0,12)+"...");
                        else
                            titulo.setText(libro.getTitulo());
                    }
                    break;
                case "epub":
                    if(libro.getPortada().length == 0) {
                        portada.setImageBitmap(epub_bitmap);
                        if(tipo == GRID){
                            if(libro.getTitulo().length()>15)
                                titulo.setText(libro.getTitulo().substring(0,12)+"...");
                            else
                                titulo.setText(libro.getTitulo());
                        }
                    }else
                        portada.setImageBitmap(BitmapFactory.decodeStream(bais));
                    break;
                default:
                    portada.setImageBitmap(BitmapFactory.decodeStream(bais));
                    break;
            }
            if(tipo == LINEAR) {
                titulo.setText(libro.getTitulo());
                autor.setText(libro.getAutor());
            }

        }

    }

}
