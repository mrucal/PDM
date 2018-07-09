package listener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mario.marcador.R;

/**
 * Created by Mario on 22/06/2018.
 */

public class ListaAdapter extends ArrayAdapter<ItemLista> {

    ItemLista[] datos;

    public ListaAdapter(Context context, ItemLista[] datos) {
        super(context, R.layout.item_lista, datos);
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.item_lista, null);

        TextView lblTitulo = (TextView)item.findViewById(R.id.text);
        lblTitulo.setText(datos[position].getTexto());


        return(item);
    }
}
