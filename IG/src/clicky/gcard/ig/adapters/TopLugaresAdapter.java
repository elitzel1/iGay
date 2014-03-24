package clicky.gcard.ig.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import clicky.gcard.ig.R;
import clicky.gcard.ig.datos.Lugares;

public class TopLugaresAdapter extends BaseAdapter {

	// Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Lugares> lugaresList = null;
    protected int count;
 
    public TopLugaresAdapter(Context context, List<Lugares> lugaresList) {
        mContext = context;
        this.lugaresList = lugaresList;
        inflater = LayoutInflater.from(mContext);
    }
 
    public class ViewHolder {
    	ImageView pos;
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
            view = inflater.inflate(R.layout.top_list_item, null);
            // Locate the TextView in listview_item.xml
            holder.pos = (ImageView) view.findViewById(R.id.txtPos);
            holder.name = (TextView) view.findViewById(R.id.txtNombre);
            holder.estado =(TextView)view.findViewById(R.id.txtEstados);
            holder.calificacion = (RatingBar) view.findViewById(R.id.rateLugar);
            holder.name.setTextColor(Color.BLACK);
            
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView
        holder.name.setText(lugaresList.get(position).getName());
        holder.estado.setText(lugaresList.get(position).getEdo());
        holder.calificacion.setRating(lugaresList.get(position).getCalif());
        
        switch(position){
        	case 0:
        		holder.pos.setImageResource(R.drawable.uno);
        		break;
        	case 1:
        		holder.pos.setImageResource(R.drawable.dos);
        		break;
        	case 2:
        		holder.pos.setImageResource(R.drawable.tres);
        		break;
        	case 3:
        		holder.pos.setImageResource(R.drawable.cuatro);
        		break;	
        	case 4:
        		holder.pos.setImageResource(R.drawable.cinco);
        		break;
        	default:
        		holder.pos.setImageResource(R.drawable.uno);
        		break;
        }
 
        return view;
    }
}