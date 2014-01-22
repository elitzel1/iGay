package clicky.gcard.ig.adapters;



import clicky.gcard.ig.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerAdapterSpecial extends BaseAdapter {
	
	private String[] items;
	private Context context;
	private Integer[] images = {R.drawable.nsantro,R.drawable.nsrestaurante,R.drawable.nscafe,
			R.drawable.nshotel,R.drawable.nscultural,R.drawable.nstienda,R.drawable.nscpersonal};
	
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

	/*Elemento seleccionado*/
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
			convertView.setBackgroundColor(context.getResources().getColor(R.color.moradof));
		    holder.text.setTextColor(context.getResources().getColor(R.color.blanco));
		    holder.text.setText(items[position]);
		    //Set the background color depending of  odd/even colorPos result
		   return convertView;
	}
	
	 @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent) {
		 
		 ViewHolderDrop mholder;
		 if (convertView == null) {
	        	LayoutInflater inflater = (LayoutInflater) context
	    		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = inflater.inflate(clicky.gcard.ig.R.layout.list_item_navigation_drop, parent, false);
	            
	            mholder = new ViewHolderDrop();
	            mholder.text1=(TextView)convertView.findViewById(clicky.gcard.ig.R.id.txtdropcat);
	            mholder.img = (ImageView)convertView.findViewById(R.id.imgicon);
			    convertView.setTag(mholder);
	        }
		 else
			 mholder = (ViewHolderDrop)convertView.getTag();
		 	
		 	convertView.setBackgroundColor(context.getResources().getColor(R.color.blanco));
		 	
		 	mholder.img.setImageResource(images[position]);
		 	mholder.text1.setText(items[position]);
		    //Set the background color depending of  odd/even colorPos result
		   return convertView;
	    }
	
	 
	static class ViewHolder {
		TextView text;
	}
	
	static class ViewHolderDrop{
		TextView text1;
		ImageView img;
	}

}
