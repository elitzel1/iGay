package clicky.gcard.ig.adapters;


import clicky.gcard.ig.R;
import clicky.gcard.ig.utils.Constants;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterListaNotificaciones extends ArrayAdapter<String>{

	private final String[] options;
	Activity context;
	
	static class ViewHolder{
		public TextView text;
	}
	
	public AdapterListaNotificaciones(Activity context,
			String[] options) {
		super(context,R.layout.item_configuracion,options);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.options=options;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View rowView=convertView;
		if(rowView==null){
			LayoutInflater inflater = context.getLayoutInflater();
			rowView=inflater.inflate(R.layout.item_configuracion, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView)rowView.findViewById(R.id.textitem);
		//	viewHolder.text.setTypeface(Constants.font_roman);
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder)rowView.getTag();
		holder.text.setText(options[position]);
		return rowView;
	}

}
