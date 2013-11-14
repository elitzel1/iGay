package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import clicky.gcard.ig.adapters.ListaLugaresAdapter;
import clicky.gcard.ig.adapters.SpinnerAdapterSpecial;
import clicky.gcard.ig.datos.Lugares;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class Listas extends ListFragment implements OnNavigationListener{

private View footer;
private Activity activity;
private ListaLugaresAdapter adapter = null;
private List<Lugares> lugaresList = null;

OnListListener callback;

	public interface OnListListener{
	public void onArticleSelected(Lugares lugar);
	}


	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
       
        lugaresList = new ArrayList<Lugares>();
        // Create an array adapter for the list view
        footer = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loading_item, null, false);
	}
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        
	        try{
	        	callback = (OnListListener) activity;
	        }catch(ClassCastException e){
	        	throw new ClassCastException(activity.toString()+ "Excepción");
	        }
	        this.activity=activity;
	 }
	 
	 @Override
	 public void onStart(){
		 super.onStart();
		 
		 SpinnerAdapterSpecial s_adapter = new SpinnerAdapterSpecial(getActivity().getBaseContext(),getResources().getStringArray(R.array.categorias));
	     setUpBar(s_adapter);
	 }
	private void setUpBar(SpinnerAdapterSpecial adapter){
		
		ActionBar bar = ((ActionBarActivity)activity).getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		bar.setListNavigationCallbacks(adapter, this);
	}
	
	
	/**
	 * Antros y Bares 0
	 * Comida 1
	 * Cafeteria 2
	 * Hotel 3
	 * Cultural 4
	 * Tienda 5
	 * Cuidado personal 6
	 */
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		  switch (itemPosition) {
          case 0:
               updateCategorias("Bares y Antros");
              break;
          case 1: //"Comida"
               updateCategorias("Comida");
        	  break;
          case 2:// "Cafeteria"
        	  updateCategorias("Cafeteria");
        	  break;
          case 3: //"Hotel
        	  updateCategorias("Hotel");
        	  break;
          case 4: //"Cultural"
        	  updateCategorias("Cultural");
              break;
          case 5:// "Tienda"
        	  updateCategorias("Tienda");
        	  break;
          case 6://"Cuidado Personal"
        	  updateCategorias("Cuidado Personal");
        	  break;
          default:
        	  break;
      }
      return false;
	}
	
	
	
	public void onListItemClick(ListView l, View v, int position,long id){
		
		callback.onArticleSelected(lugaresList.get(position));
		
		getListView().setItemChecked(position, true);
	}
	
	private void updateCategorias(String category){
	
		lugaresList.clear();
		getListView().addFooterView(footer);
		setListAdapter(null);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Lugares");
        query.orderByAscending("createdAt");
        query.whereEqualTo("categoria", category);
        query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> lugares, ParseException e) {
				getListView().removeFooterView(footer);
				for (ParseObject lugar : lugares){
					Lugares item = new Lugares();
					item.setLugarId((String) lugar.getObjectId());
					item.setName((String) lugar.get("nombre"));
					item.setCategory((String) lugar.get("categoria"));
					item.setCalif((float)lugar.getDouble("calificacion"));
					item.setDesc((String) lugar.get("descripcion"));
					item.setDir((String) lugar.get("direccion"));
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
	
	public void onDestroy(){
		super.onDestroy();
		callback = null;
	}
}
