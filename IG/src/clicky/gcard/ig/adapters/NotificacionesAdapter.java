package clicky.gcard.ig.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import clicky.gcard.ig.R;
import clicky.gcard.ig.datos.Notificacion;

public class NotificacionesAdapter extends BaseAdapter {

	// Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Notificacion> lugaresList = null;
    protected int count;
 
    public NotificacionesAdapter(Context context, List<Notificacion> lugaresList) {
        mContext = context;
        this.lugaresList = lugaresList;
        inflater = LayoutInflater.from(mContext);
    }
 
    public class ViewHolder {
        TextView title;
        TextView desc;
        TextView link;
        TextView fecha;
    }
 
    @Override
    public int getCount() {
        return lugaresList.size();
    }
 
    @Override
    public Notificacion getItem(int position) {
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
            view = inflater.inflate(R.layout.notificaciones_item, null);
            // Locate the TextView in listview_item.xml
            holder.title = (TextView) view.findViewById(R.id.txtTitulo);
            holder.desc = (TextView) view.findViewById(R.id.txtDesc);
            holder.link = (TextView) view.findViewById(R.id.txtLink);
            holder.fecha = (TextView) view.findViewById(R.id.txtFecha);
            
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView
        holder.title.setText(lugaresList.get(position).getTitulo());
        holder.desc.setText(lugaresList.get(position).getDesc());
        
        String text = "<a href='"+lugaresList.get(position).getLink()+"'>Ver Más</a>";
        holder.link.setMovementMethod(LinkMovementMethod.getInstance());
        holder.link.setText(Html.fromHtml(text));
        
        holder.fecha.setText(lugaresList.get(position).getFecha());
        
        return view;
    }
}
