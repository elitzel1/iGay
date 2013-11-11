package clicky.gcard.ig.adapters;



import clicky.gcard.ig.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapterSpecial extends BaseAdapter {
	
	private String[] items;
	private Context context;
	
	public SpinnerAdapterSpecial (Context context,String[] items){
		this.items=items;
		this.context=context;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		ViewHolder holder;
		if (convertView == null) {
		    convertView = inflater.inflate(clicky.gcard.ig.R.layout.list_item_navigation, parent, false);
		    holder = new ViewHolder();

		  holder.text=(TextView)convertView.findViewById(clicky.gcard.ig.R.id.txtTitle);
		    convertView.setTag(holder);
		} else {
		    holder = (ViewHolder) convertView.getTag();
		}
			convertView.setBackgroundColor(context.getResources().getColor(R.color.morado));

		    holder.text.setTextColor(context.getResources().getColor(R.color.blanco));
		    // Bind the data efficiently with the holder.
		    holder.text.setText(items[position]);
		    //Set the background color depending of  odd/even colorPos result
		   return convertView;
	}
	
	 @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent) {
		 TextView text;
		 
		 if (convertView == null) {
	        	LayoutInflater inflater = (LayoutInflater) context
	    		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = inflater.inflate(clicky.gcard.ig.R.layout.list_item_navigation, parent, false);
	            text=(TextView)convertView.findViewById(clicky.gcard.ig.R.id.txtTitle);
			    //convertView.setTag(holder);
	        }
		 else
			 text=(TextView)convertView.findViewById(clicky.gcard.ig.R.id.txtTitle);
		 	
		 	convertView.setBackgroundColor(context.getResources().getColor(R.color.blanco));
		 // Bind the data efficiently with the holder.
		    text.setText(items[position]);
		    //Set the background color depending of  odd/even colorPos result
		   return convertView;
	    }
	
	 
	static class ViewHolder {
		TextView text;
	}

}
