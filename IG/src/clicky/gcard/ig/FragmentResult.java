package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import clicky.gcard.ig.adapters.ListaLugaresAdapter;
import clicky.gcard.ig.datos.Lugares;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentResult extends ListFragment{

	private View footer;
	private Activity activity;
	private ListaLugaresAdapter adapter=null;
	private List<Lugares> lugaresList = null;
	private View view;
OnSelectItem callback;

	public interface OnSelectItem{
		public void onArticleSelected(Lugares lugar);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		if(view==null){
			try{
		view = inflater.inflate(R.layout.lugares_layout,null,false);
			}catch(InflateException e){}
		}
		else{
			ViewGroup group = (ViewGroup)view.getParent();
			if(group!=null)
				group.removeView(view);
		}
		
		return view;
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		lugaresList = new ArrayList<Lugares>();
		footer = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_item,
				null,false);
		
		Log.i("ListView", "lala");
		
			
		
		
	}
	
	public void onStart(){
		super.onStart();
		Bundle bundle = getArguments();
		setUpList(bundle.getString("query"));
	}

	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			callback = (OnSelectItem)activity;
		}catch (ClassCastException e){
			throw new ClassCastException(activity.toString()+" exception");
		}
		this.activity=activity;
	}
	

	private void setUpList(String query){
		lugaresList.clear();
		getListView().addFooterView(footer);
		setListAdapter(null);
		ParseQuery<ParseObject> queryP = new ParseQuery<ParseObject>("Lugares");
		queryP.whereContains("nombre", query);
		
		queryP.findInBackground(new FindCallback<ParseObject>() {	
			@Override
			public void done(List<ParseObject> lugares, ParseException e) {
				getListView().removeFooterView(footer);
				for (ParseObject lugar : lugares){
					Lugares item = new Lugares();
					item.setLugarId((String) lugar.getObjectId());
					item.setName( lugar.getString("nombre"));
					item.setCategory( lugar.getString("categoria"));
					item.setCalif((float)lugar.getDouble("calificacion"));
					item.setDesc( lugar.getString("descripcion"));
					item.setDir( lugar.getString("direccion"));
					item.setEdo(lugar.getString("estado"));
					item.setImagen(lugar.getParseFile("imagen").getUrl());
					item.setGeo(new LatLng(lugar.getParseGeoPoint("localizacion").getLatitude(),
							lugar.getParseGeoPoint("localizacion").getLongitude()));
					lugaresList.add(item);
				}
				adapter = new ListaLugaresAdapter(activity, lugaresList);
	            // Binds the Adapter to the ListView
	            getListView().setAdapter(adapter);
				
			}
		});
		
	}
	
	public void onListItemClick(ListView l,View v, int position, long id){
		Log.i("Clic", ""+position);
		callback.onArticleSelected(lugaresList.get(position));
		getListView().setItemChecked(position, true);
	}
	
	public void onDestroy(){
		super.onDestroy();
		callback=null;
	}
}
