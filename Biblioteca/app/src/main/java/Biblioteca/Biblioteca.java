package Biblioteca;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.AdaptadorLibros;

/**
 * Created by Mario on 27/05/2018.
 */

public class Biblioteca {

    public static final String TITULO = "titulo";
    public static final String AUTOR = "autor";
    public static final String FECHA_AGREGADO = "fecha_agregado";

    private String ordenacion_defecto = FECHA_AGREGADO;

    private ArrayList<Libro> libros;
    SQLiteDatabase db = null;
    private RecyclerView recycler;

    public Biblioteca(BibliotecaSQLite bsql, RecyclerView recycler,int tipo, String ordenacion){
        this.db = bsql.getWritableDatabase();
        this.recycler = recycler;
        ordenacion_defecto = ordenacion;
        libros = getLibrosOrderBy(ordenacion_defecto);
        recycler.setAdapter(new AdaptadorLibros(libros,tipo));
        //((AdaptadorLibros)recycler.getAdapter()).setTipo(((AdaptadorLibros)recycler.getAdapter()).getTipo());
    }

    public ArrayList<Libro> getLibros() {
        return libros;
    }

    public void setLibros(ArrayList<Libro> libros) {
        this.libros = libros;
        recycler.setAdapter(new AdaptadorLibros(libros,((AdaptadorLibros)recycler.getAdapter()).getTipo()));
        //((AdaptadorLibros)recycler.getAdapter()).setTipo(((AdaptadorLibros)recycler.getAdapter()).getTipo());
    }

    public void addLibro(Libro libro){
        if(db!=null){
            //Creamos el registro a insertar como objeto ContentValues
            ContentValues nuevoRegistro = new ContentValues();
            //nuevoRegistro.put("codigo", libros.size());
            nuevoRegistro.put("titulo", libro.getTitulo());
            nuevoRegistro.put("autor", libro.getAutor());
            nuevoRegistro.put("portada",libro.getPortada());
            nuevoRegistro.put("path", libro.getPath());
            nuevoRegistro.put("formato",libro.getFormato());
            nuevoRegistro.put("fecha_agregado", libro.getFecha_agregado());

            boolean insertado = false;
            //Insertamos el registro en la base de datos
            try {
                db.insertOrThrow("Biblioteca", null, nuevoRegistro);
                insertado = true;
            }catch (SQLiteConstraintException e){ }
            if(insertado) {
                libros.add(libro);
                libros = getLibrosOrderBy(ordenacion_defecto);
                recycler.setAdapter(new AdaptadorLibros(libros,((AdaptadorLibros)recycler.getAdapter()).getTipo()));
                //((AdaptadorLibros)recycler.getAdapter()).setTipo(((AdaptadorLibros)recycler.getAdapter()).getTipo());
            }
        }
    }

    public void deleteLibro(Libro libro){
        if(db!=null) {
            db.delete("Biblioteca", "titulo='"+libro.getTitulo()+"'", null);
            libros.remove(libro);
            libros = getLibrosOrderBy(ordenacion_defecto);
            recycler.setAdapter(new AdaptadorLibros(libros,((AdaptadorLibros)recycler.getAdapter()).getTipo()));
            //((AdaptadorLibros)recycler.getAdapter()).setTipo(((AdaptadorLibros)recycler.getAdapter()).getTipo());
        }
    }

    public ArrayList<Libro> getLibrosOrderBy(String order){
        ordenacion_defecto = order;
        ArrayList<Libro> array = new ArrayList<Libro>();
        if(db!=null) {
            String[] campos = new String[]{"titulo", "autor", "portada", "path", "formato", "fecha_agregado"};
            Cursor c;
            if(order.equals(FECHA_AGREGADO))
                c = db.query("Biblioteca", campos, null, null, null, null, order+" DESC");
            else
                c = db.query("Biblioteca", campos, null, null, null, null, order);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                        array.add(new Libro(c.getString(0), c.getString(1), c.getBlob(2), c.getString(3), c.getString(4), c.getString(5)));
                } while (c.moveToNext());
            }
        }
        return array;
    }

    public void setRecycler(RecyclerView recycler,int tipo){
        this.recycler = recycler;
        recycler.setAdapter(new AdaptadorLibros(libros,tipo));
    }

}
