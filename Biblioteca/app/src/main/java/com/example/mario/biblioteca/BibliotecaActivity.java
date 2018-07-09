package com.example.mario.biblioteca;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Adapter.AdaptadorLibros;
import Adapter.RecyclerViewClickListener;
import Adapter.RecyclerViewTouchListener;
import Biblioteca.Biblioteca;
import Biblioteca.BibliotecaSQLite;
import Biblioteca.Libro;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class BibliotecaActivity extends AppCompatActivity {

    private Biblioteca biblioteca;
    private BibliotecaSQLite bsql;

    private RecyclerView recycler;

    private ArrayList<Libro> libros;

    private final int PERMISO_LECTURA = 2812;

    private Libro libro_seleccionado = null;

    public TextView titulo_dialogo=null;

    SharedPreferences prefs;
    public String ordenacion;
    public int estanteria = AdaptadorLibros.LINEAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        ordenacion = prefs.getString("ordenacion","ordenacion");
        if(ordenacion.equals("ordenacion")){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ordenacion", Biblioteca.FECHA_AGREGADO);
            editor.commit();
            ordenacion = Biblioteca.FECHA_AGREGADO;
        }
        estanteria = prefs.getInt("estanteria",-1);
        if(estanteria==-1){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("estanteria", AdaptadorLibros.LINEAR);
            editor.commit();
            estanteria = AdaptadorLibros.LINEAR;
        }

        titulo_dialogo = ((TextView)findViewById(R.id.titulo));

        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFolder();
                recycler.setAdapter(new AdaptadorLibros(biblioteca.getLibros(),((AdaptadorLibros)recycler.getAdapter()).getTipo()));
            }
        });

        //this.deleteDatabase("/data/data/com.example.mario.biblioteca/databases/Biblioteca");
        bsql = new BibliotecaSQLite(this);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recycler, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(position < biblioteca.getLibros().size()){
                    libro_seleccionado = biblioteca.getLibros().get(position);
                    switch (libro_seleccionado.getFormato()) {
                        case "pdf":
                            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISO_LECTURA);
                            } else {
                                Intent i = new Intent(getApplicationContext(), PdfViewerActivity.class);
                                i.putExtra("path", libro_seleccionado.getPath());
                                startActivity(i);
                            }
                            break;
                        case "epub":
                            showMensajeError("Todavía no soporta visualización de formato epub.");
                            break;
                    }
                }
            }

            @Override
            public void onLongClick(final View view, int position) {
                if (position < biblioteca.getLibros().size()) {
                    libro_seleccionado = biblioteca.getLibros().get(position);
                    final AlertDialog.Builder dialogo_opciones = new AlertDialog.Builder(view.getContext());
                    dialogo_opciones.setTitle(libro_seleccionado.getTitulo())
                            .setItems(new String[]{"Eliminar", "Info"}, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    switch (item) {
                                        case 0:
                                            Toast.makeText(getApplicationContext(), "ELIMINAR", Toast.LENGTH_SHORT).show();
                                            biblioteca.deleteLibro(libro_seleccionado);
                                            break;
                                        case 1:
                                            AlertDialog.Builder dialogo_info = new AlertDialog.Builder(view.getContext());
                                            LayoutInflater inflater = getLayoutInflater();
                                            View v = inflater.inflate(R.layout.dialogo_info, null);
                                            ImageView portada = (ImageView) v.findViewById(R.id.portada_dialogo);
                                            String titulo = "Nombre: ";
                                            if (libro_seleccionado.getFormato().equals("epub")) {
                                                ByteArrayInputStream bais = new ByteArrayInputStream(libro_seleccionado.getPortada());
                                                if(libro_seleccionado.getPortada().length == 0)
                                                    try {
                                                        portada.setImageBitmap(BitmapFactory.decodeStream(getAssets().open("epub.png")));
                                                    } catch (IOException e) {}
                                                else
                                                    portada.setImageBitmap(BitmapFactory.decodeStream(bais));
                                                titulo = "Título: ";
                                                ((TextView) v.findViewById(R.id.autor_dialogo)).setText("Autor/a: " + libro_seleccionado.getAutor());
                                            } else
                                                try {
                                                    portada.setImageBitmap(BitmapFactory.decodeStream(getAssets().open("pdf.png")));
                                                } catch (IOException e) {}
                                            ((TextView) v.findViewById(R.id.titulo_dialogo)).setText(titulo + libro_seleccionado.getTitulo());
                                            ((TextView) v.findViewById(R.id.path_dialogo)).setText(libro_seleccionado.getPath());
                                            ((TextView) v.findViewById(R.id.fecha_dialogo)).setText("Fecha añadido:\n" + libro_seleccionado.getFecha_agregado());
                                            dialogo_info.setView(v)
                                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            dialogo_info.show();
                                            break;
                                    }
                                }
                            });
                    dialogo_opciones.show();
                }
            }
        }));

        if(estanteria == AdaptadorLibros.LINEAR){
            recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            biblioteca = new Biblioteca(bsql,recycler, AdaptadorLibros.LINEAR,ordenacion);
        }else{
            recycler.setLayoutManager(new GridLayoutManager(this,3));
            biblioteca = new Biblioteca(bsql,recycler, AdaptadorLibros.GRID,ordenacion);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISO_LECTURA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(getApplicationContext(),PdfViewerActivity.class);
                    i.putExtra("path", libro_seleccionado.getPath());
                    startActivity(i);                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_biblioteca, menu);
        switch (ordenacion){
            case Biblioteca.FECHA_AGREGADO:
                menu.getItem(1).getSubMenu().getItem(0).setChecked(true);
                break;
            case Biblioteca.TITULO:
                menu.getItem(1).getSubMenu().getItem(1).setChecked(true);
                break;
            case Biblioteca.AUTOR:
                menu.getItem(1).getSubMenu().getItem(2).setChecked(true);
                break;
        }
        if(estanteria == AdaptadorLibros.LINEAR){
            menu.getItem(0).setChecked(false);
        }else
            menu.getItem(0).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_ordenar) {
            return true;
        }
        if (id == R.id.action_estanteria){
            if(item.isChecked()){
                recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                biblioteca.setRecycler(recycler,AdaptadorLibros.LINEAR);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("estanteria", AdaptadorLibros.LINEAR);
                editor.commit();
                item.setChecked(false);
            }else{
                recycler.setLayoutManager(new GridLayoutManager(this,3));
                biblioteca.setRecycler(recycler,AdaptadorLibros.GRID);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("estanteria", AdaptadorLibros.GRID);
                editor.commit();
                item.setChecked(true);
            }
        }
        if (id == R.id.item_fecha_agregado) {
            item.setChecked(true);
            biblioteca.setLibros(biblioteca.getLibrosOrderBy(Biblioteca.FECHA_AGREGADO));
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ordenacion", Biblioteca.FECHA_AGREGADO);
            editor.commit();
            return true;
        }
        if (id == R.id.item_titulo) {
            item.setChecked(true);
            biblioteca.setLibros(biblioteca.getLibrosOrderBy(Biblioteca.TITULO));
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ordenacion", Biblioteca.TITULO);
            editor.commit();
            return true;
        }
        if (id == R.id.item_autor) {
            item.setChecked(true);
            biblioteca.setLibros(biblioteca.getLibrosOrderBy(Biblioteca.AUTOR));
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ordenacion", Biblioteca.AUTOR);
            editor.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openFolder(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/myFolder/");
        intent.setDataAndType(uri, "text/csv");
        startActivityForResult(Intent.createChooser(intent, "Open folder"),1001);
    }

    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if(data != null) {
                Uri currFileURI = data.getData();
                    String path = getPath(currFileURI);
                try {
                    switch (getFormato(path)) {
                        case "epub":
                            InputStream epubInputStream = getContentResolver().openInputStream(currFileURI);
                            Book book = (new EpubReader()).readEpub(epubInputStream);
                            String titulo, autor;
                            autor = book.getMetadata().getAuthors().get(0).getFirstname() + " " + book.getMetadata().getAuthors().get(0).getLastname();
                            titulo = book.getTitle();
                            byte[] cover = book.getCoverImage() != null ? book.getCoverImage().getData() : new byte[0];
                            biblioteca.addLibro(new Libro(titulo, autor, cover, path));
                            break;
                        case "pdf":
                            biblioteca.addLibro(new Libro(getNombreFichero(path), "", new byte[0], path, "pdf"));
                            break;
                        default:
                            showMensajeError("Formato no soportado.");
                            break;
                    }
                } catch (IOException e) {
                    Log.e("epublib", e.getMessage());
                }
            }
        }
    }

    public void showMensajeError(String mensaje){
        AlertDialog.Builder dialogo_error = new AlertDialog.Builder(this);
        dialogo_error.setMessage(mensaje)
                .setTitle("Error")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dialogo_error.show();
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return "";
    }

    public String getFormato(String path) {
        return path.substring(path.lastIndexOf(".")+1, path.length());
    }

    public String getNombreFichero(String path){
        return path.substring(path.lastIndexOf('/')+1,path.lastIndexOf('.'));
    }

}
