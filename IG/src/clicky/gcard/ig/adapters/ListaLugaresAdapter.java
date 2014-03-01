package clicky.gcard.ig.adapters;

import java.util.List;
import clicky.gcard.ig.R;
import clicky.gcard.ig.datos.Lugares;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class ListaLugaresAdapter extends BaseAdapter {

	// Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Lugares> lugaresList = null;
    protected int count;
 
    public ListaLugaresAdapter(Context context, List<Lugares> lugaresList) {
        mContext = context;
        this.lugaresList = lugaresList;
        inflater = LayoutInflater.from(mContext);
    }
 
    public class ViewHolder {
        TextView name;
        TextView estado;
        RatingBar calificacion;
    }
 
    @Override
    public int getCount() {
        return lugaresList.size();
    }
 
    @Override
    public Lugares getItem(int position) {
        return lugaresList.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lugar_list_item, null);
            // Locate the TextView in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.txtNombre);
            holder.calificacion = (RatingBar) view.findViewById(R.id.rateLugar);
            holder.estado = (TextView)view.findViewById(R.id.txtEstado);
            holder.name.setTextColor(Color.BLACK);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView
        holder.name.setText(lugaresList.get(position).getName());
        holder.estado.setText(lugaresList.get(position).getEdo());
        holder.calificacion.setRating(lugaresList.get(position).getCalif());
 
        return view;
    }
}