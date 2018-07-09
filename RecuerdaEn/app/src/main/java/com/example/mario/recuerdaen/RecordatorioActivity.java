package com.example.mario.recuerdaen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.mario.recuerdaen.adapter.ListRecordatorioAdapter;
import com.example.mario.recuerdaen.recordatorioDB.Recordatorio;
import com.example.mario.recuerdaen.service.UbicacionService;
import com.example.mario.recuerdaen.recordatorioDB.RecordatorioDB;

public class RecordatorioActivity extends Activity {

    private ListView listaRecordatorios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);

        Intent i = new Intent(getBaseContext(), UbicacionService.class);
        startService(i);

        //this.deleteDatabase("/data/data/com.example.mario.recuerdaen/databases/recordatorioDB.db");
        final RecordatorioDB recordatorios = new RecordatorioDB(this);

        listaRecordatorios = findViewById(R.id.recordatorios);
        listaRecordatorios.setAdapter(new ListRecordatorioAdapter(this,recordatorios.getRecordatorios(),getBaseContext()));
        listaRecordatorios.setClickable(true);
        listaRecordatorios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Recordatorio recordatorio_seleccionado = recordatorios.getRecordatorios().get(position);
                Intent i = new Intent(getBaseContext(), MapActivity.class);
                i.putExtra("editar",true);
                i.putExtra("lat",recordatorio_seleccionado.getLat());
                i.putExtra("lon",recordatorio_seleccionado.getLon());
                i.putExtra("radio",recordatorio_seleccionado.getRadio());
                i.putExtra("nombre",recordatorio_seleccionado.getNombre());
                i.putExtra("descripcion",recordatorio_seleccionado.getDescripcion());
                startActivity(i);
            }
        });
        listaRecordatorios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Recordatorio recordatorio_seleccionado = recordatorios.getRecordatorios().get(position);
                final AlertDialog.Builder dialogo_opciones = new AlertDialog.Builder(view.getContext());
                dialogo_opciones.setTitle(recordatorio_seleccionado.getNombre())
                        .setItems(new String[]{"Eliminar"}, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if(item == 0){
                                    recordatorios.deleteRecordatorio(recordatorio_seleccionado);
                                    listaRecordatorios.setAdapter(new ListRecordatorioAdapter(((ListRecordatorioAdapter)listaRecordatorios.getAdapter()).getContext(),recordatorios.getRecordatorios(),getBaseContext()));
                                    startService(new Intent(getBaseContext(), UbicacionService.class));
                                }
                            }
                        });
                dialogo_opciones.show();
                return true;
            }
        });

        ((Button)(findViewById(R.id.botonNuevo))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MapActivity.class));
            }
        });
    }

}
