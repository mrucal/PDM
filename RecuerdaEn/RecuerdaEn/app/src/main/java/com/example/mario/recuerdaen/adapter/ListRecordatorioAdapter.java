package com.example.mario.recuerdaen.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mario.recuerdaen.R;
import com.example.mario.recuerdaen.recordatorioDB.Recordatorio;
import com.example.mario.recuerdaen.service.UbicacionService;
import com.example.mario.recuerdaen.recordatorioDB.RecordatorioDB;

import java.util.List;

public class ListRecordatorioAdapter extends ArrayAdapter {

    private List<Recordatorio> datos;

    private Context context;
    private Context aplication_context;

    public ListRecordatorioAdapter(Context context, List<Recordatorio> recordatorios, Context aplication_context) {
        super(context, R.layout.item_recordatorio, recordatorios);
        this.datos = recordatorios;
        this.context = context;
        this.aplication_context = aplication_context;
    }

    public View getView(int position, final View convertView, ViewGroup parent)
    {
        View item = convertView;
        ViewHolder holder;
        if(item == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.item_recordatorio, null);

            holder = new ViewHolder();
            holder.activado = (CheckBox) item.findViewById(R.id.item_activado);
            holder.nombre = (TextView)item.findViewById(R.id.item_nombre);
            holder.descripcion = (TextView)item.findViewById(R.id.item_descripcion);

            item.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)item.getTag();
        }

        holder.activado.setOnClickListener(new MyOnClickListener(datos.get(position).getNombre(), datos.get(position).getDescripcion()));
        holder.activado.setChecked(datos.get(position).isActivado());
        holder.nombre.setText(String.valueOf(datos.get(position).getNombre()));
        holder.descripcion.setText(String.valueOf(datos.get(position).getDescripcion()));

        return(item);
    }

    public Context getContext(){
        return context;
    }

    static class ViewHolder {
        public CheckBox activado;
        TextView nombre;
        TextView descripcion;
    }

    class MyOnClickListener implements OnClickListener {
        private String nombre;
        private String descripcion;
        MyOnClickListener(String nombre, String descripcion){this.nombre = nombre; this.descripcion = descripcion;}
        @Override
        public void onClick(View v) {

            boolean activado = ((CheckBox) v.findViewById(R.id.item_activado)).isChecked();
            RecordatorioDB recordatorios = new RecordatorioDB(getContext());
            recordatorios.activarRecordatorio(nombre, descripcion, activado);

            aplication_context.startService( new Intent(aplication_context, UbicacionService.class));

        }
    }
}

