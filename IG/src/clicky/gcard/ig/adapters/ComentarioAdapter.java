package clicky.gcard.ig.adapters;

import java.util.ArrayList;
import java.util.List;

import clicky.gcard.ig.R;
import clicky.gcard.ig.datos.Comentario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ComentarioAdapter extends BaseAdapter {

	// Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Comentario> lugaresList = null;
    private ArrayList<Comentario> arraylist;
    protected int count;
 
    public ComentarioAdapter(Context context, List<Comentario> lugaresList) {
        mContext = context;
        this.lugaresList = lugaresList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Comentario>();
        this.arraylist.addAll(lugaresList);
    }
 
    public class ViewHolder {
        TextView comment;
        TextView user;
    }
 
    @Override
    public int getCount() {
        return lugaresList.size();
    }
 
    @Override
    public Comentario getItem(int position) {
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
            view = inflater.inflate(R.layout.comentario_item, null);
            // Locate the TextView in listview_item.xml
            holder.user = (TextView) view.findViewById(R.id.textView1);
            holder.comment = (TextView) view.findViewById(R.id.textView2);
 
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView
        holder.user.setText(lugaresList.get(position).getUser());
        holder.comment.setText(lugaresList.get(position).getComment());
        
        return view;
    }
}
