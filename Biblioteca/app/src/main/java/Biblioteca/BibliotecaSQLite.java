package Biblioteca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Mario on 27/05/2018.
 */

public class BibliotecaSQLite extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    //String sqlCreate = "CREATE TABLE Biblioteca (codigo INTEGER UNIQUE, titulo TEXT PRIMARY KEY, autor TEXT,portada BLOB, path TEXT, formato TEXT, fecha_agregado TEXT)";
    String sqlCreate = "CREATE TABLE Biblioteca (titulo TEXT PRIMARY KEY, autor TEXT,portada BLOB, path TEXT, formato TEXT, fecha_agregado TEXT)";

    public BibliotecaSQLite(Context contexto) {
        super(contexto, "Biblioteca", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Biblioteca");

        //Se crea la nueva versión de la tabla
        //db.execSQL(sqlCreate);
    }

}
