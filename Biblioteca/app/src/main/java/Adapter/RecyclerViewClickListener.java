package Adapter;

import android.view.View;

/**
 * Created by Mario on 15/06/2018.
 */

public interface RecyclerViewClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
