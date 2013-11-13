package clicky.gcard.ig.adapters;

import java.util.ArrayList;
import java.util.List;
import clicky.gcard.ig.R;
import clicky.gcard.ig.datos.Lugares;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListaLugaresAdapter extends BaseAdapter {

	// Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Lugares> lugaresList = null;
    private ArrayList<Lugares> arraylist;
    protected int count;
 
    public ListaLugaresAdapter(Context context, List<Lugares> lugaresList) {
        mContext = context;
        this.lugaresList = lugaresList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Lugares>();
        this.arraylist.addAll(lugaresList);
    }
 
    public class ViewHolder {
        TextView name;
        TextView category;
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
            holder.category = (TextView) view.findViewById(R.id.txtCategoria);
 
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView
        holder.name.setText(lugaresList.get(position).getName());
        holder.category.setText(lugaresList.get(position).getCategory());
        // Listen for ListView Item Click
       // view.setOnClickListener(new OnClickListener() {
 
//            @Override
//            public void onClick(View arg0) {
//                Bundle extras = new Bundle();
//                // Pass all data number
//                extras.putString("lugarId", (lugaresList.get(position).getLugarId()));
//                // Start SingleItemView Class
//                ActionBarActivity activity = (ActionBarActivity)mContext;
//                FragmentManager manager = activity.getSupportFragmentManager();
//                DetailFragment fragmentPop = new DetailFragment();
//        		FragmentTransaction trans = manager.beginTransaction();
//        		fragmentPop.setArguments(extras);
//        		trans.replace(R.id.content_frame, fragmentPop);
//    			trans.addToBackStack(null);
//    			trans.commit();
//            }
//        });
 
        return view;
    }
}
