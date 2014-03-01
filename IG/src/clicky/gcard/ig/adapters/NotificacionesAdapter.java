package clicky.gcard.ig.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
        Button link;
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
 
    public View getView(int position, View view, ViewGroup parent) {
        if(lugaresList.get(position).isOpened()){
        	if(lugaresList.get(position).isRecent()){
        		view = null;
        	}
        	return getOpened(view, parent, position);
        }else{
        	return getClosed(view, parent, position);
        }
    }
    
    public View getClosed(View view, ViewGroup parent,final int position){
    	final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
        	view = inflater.inflate(R.layout.new_notificaciones_item, null);
        	holder.title = (TextView) view.findViewById(R.id.txtLink);
            
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

    	holder.title.setOnClickListener(new OnClickListener() {
				
    		@Override
    		public void onClick(View v) {
    			lugaresList.get(position).setOpened(true);
    			lugaresList.get(position).setRecent(true);
				notifyDataSetChanged();
    		}
    	});
        return view;
    }
    
    public View getOpened(View view, ViewGroup parent,final int position){
    	final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            lugaresList.get(position).setRecent(false);
            view = inflater.inflate(R.layout.notificaciones_item, null);
            // Locate the TextView in listview_item.xml
            holder.title = (TextView) view.findViewById(R.id.txtTitulo);
            holder.desc = (TextView) view.findViewById(R.id.txtDesc);
            holder.link = (Button) view.findViewById(R.id.txtLink);
            holder.fecha = (TextView) view.findViewById(R.id.txtFecha);
            
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.title.setText(lugaresList.get(position).getTitulo());
        holder.desc.setText(lugaresList.get(position).getDesc());
        
        holder.fecha.setText(lugaresList.get(position).getFecha());
	        
        holder.link.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(lugaresList.get(position).getLink()));
				mContext.startActivity(i);
			}
		});
        return view;
    }
}
