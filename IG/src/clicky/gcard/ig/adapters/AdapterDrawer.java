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
	
	
	
	public AdapterDrawer(Context context, int resource,
			String[] objects,Integer[] imageId) {
		
		this.context=context;
		this.imageId=imageId;
		options=objects;
		
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public View getView(int position, View view, ViewGroup parent){
		
		View view2 = null;
		
		if(position==0)
			view2 = getViewHeader(view,parent);
		else
			view2 = getViewItem(view, parent,position);
			
		return view2;
	}
	
	public View getViewItem(View view, ViewGroup parent,int position){
		ViewHolder holder;
		if(view==null){
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_drawable, parent,false);
		
		TextView text;
		ImageView imag;
	
		text=(TextView)view.findViewById(R.id.texto1);
		imag=(ImageView)view.findViewById(R.id.imag1);
		holder= new ViewHolder();
		holder.text = text;
		holder.image = imag;
		view.setTag(holder);
		}
		
		holder = (ViewHolder)view.getTag();
		holder.text.setText(options[position]);
		holder.image.setImageResource(imageId[position]);
		
		return view;
	}
	
	public View getViewHeader(View view, ViewGroup parent){
		ViewHolderHeader holder;
		
		if(view==null){
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.header, parent,false);
			holder= new ViewHolderHeader();
			
			ImageView imag = (ImageView)view.findViewById(R.id.image_header);
			
			holder.imagen = imag;
			view.setTag(holder);
			}
			
		return view;
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
	
	static class ViewHolder{
		public TextView text;
		public ImageView image;
		
	}
	
	class ViewHolderHeader{
		public ImageView imagen;
	}
	

}
