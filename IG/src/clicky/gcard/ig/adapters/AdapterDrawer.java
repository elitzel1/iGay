package clicky.gcard.ig.adapters;

import clicky.gcard.ig.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterDrawer extends BaseAdapter {

	private Context context;
	private String[] options;
	private Integer[] imageId;
	LayoutInflater inflater;
	
	static class ViewHolder{
		public TextView text;
		public ImageView image;
		
	}
	
	public AdapterDrawer(Context context, int resource,
			String[] objects,Integer[] imageId) {
		
		this.context=context;
		this.imageId=imageId;
		options=objects;
		
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public View getView(int position, View view, ViewGroup parent){
		
		TextView text;
		ImageView imag;
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.item_drawable, parent,false);
		text=(TextView)itemView.findViewById(R.id.texto1);
		imag=(ImageView)itemView.findViewById(R.id.imag1);
		
		text.setText(options[position]);
		imag.setImageResource(imageId[position]);
//		View rowView = view;
//		
//		if(rowView== null){
//		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		rowView = inflater.inflate(R.layout.item_drawable, null,false);
//		ViewHolder viewHolder = (ViewHolder)rowView.getTag();
//		
//		viewHolder.image=(ImageView)rowView.findViewById(R.id.imag1);
//		viewHolder.text=(TextView)rowView.findViewById(R.id.texto1);
//		
//		rowView.setTag(viewHolder);
//		}
//		
//		ViewHolder holder = (ViewHolder)rowView.getTag();
//		holder.text.setText(options[position]);
//		holder.image.setImageResource(imageId[position]);
	//	return rowView;
		return itemView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return options.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return options[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
