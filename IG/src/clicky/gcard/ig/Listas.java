package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;

import clicky.gcard.ig.adapters.ListaLugaresAdapter;
import clicky.gcard.ig.adapters.SpinnerAdapterSpecial;
import clicky.gcard.ig.adapters.TopLugaresAdapter;
import clicky.gcard.ig.datos.Lugares;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class Listas extends ListFragment implements OnNavigationListener{

private View footer;
private View view;
private Activity activity;
private ListaLugaresAdapter adapterList = null;
private TopLugaresAdapter adapterTop = null;
private List<Lugares> lugaresList = null;
private int tipo = -1;
private int page = 0;
private ParseQuery<ParseObject> query;
private String categoria;
private boolean cancelled = false;
private boolean more = false;


OnListListener callback;

	public interface OnListListener{
	public void onArticleSelected(Lugares lugar);
	public void isEnableToggle();
	}

	@SuppressLint("NewApi")
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
		
		AdView adView = (AdView)view.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
	    
		return view;
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
       
        lugaresList = new ArrayList<Lugares>();
        // Create an array adapter for the list view
        footer = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loading_item, null, false);
        
        tipo = getArguments() != null ? getArguments().getInt("tipo") : null;
				
		SpinnerAdapterSpecial s_adapter = new SpinnerAdapterSpecial(getActivity().getBaseContext(),getResources().getStringArray(R.array.categorias));
		setUpBar(s_adapter);	
		
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
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		getListView().setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					if(getListView().getLastVisiblePosition() <= lugaresList.size() && more){
						loadMore(categoria);
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}
	private void setUpBar(SpinnerAdapterSpecial adapter){
		
		ActionBar bar = ((ActionBarActivity)activity).getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		bar.setListNavigationCallbacks(adapter, this);
		callback.isEnableToggle();
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
          case 0://Bares y Antros
        	  categoria = "Bares y Antros";
        	  if(tipo == 0)
        		  updateTop("Bares y Antros");
        	  else
        		  updateCategorias("Bares y Antros");
              break;
          case 1: //"Comida"
        	  categoria = "Comida";
        	  if(tipo == 0)
        		  updateTop("Comida");
        	  else
        		  updateCategorias("Comida");
        	  break;
          case 2:// "Cafeteria"
        	  categoria = "Cafeteria";
        	  if(tipo == 0)
        		  updateTop("Cafeteria");
        	  else
        		  updateCategorias("Cafeteria");
        	  break;
          case 3: //"Hotel
        	  categoria = "Hotel";
        	  if(tipo == 0)
        		  updateTop("Hotel");
        	  else
        		  updateCategorias("Hotel");
        	  break;
          case 4: //"Cultural"
        	  categoria = "Cultural";
        	  if(tipo == 0)
        		  updateTop("Cultural");
        	  else
        		  updateCategorias("Cultural");
              break;
          case 5:// "Tienda"
        	  categoria = "Tienda";
        	  if(tipo == 0)
        		  updateTop("Tienda");
        	  else
        		  updateCategorias("Tienda");
        	  break;
          case 6://"Cuidado Personal"
        	  categoria = "Cuidado Personal";
        	  if(tipo == 0)
        		  updateTop("Cuidado Personal");
        	  else
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
		query = new ParseQuery<ParseObject>(
                "Lugares");
        query.orderByAscending("createdAt");
        query.whereEqualTo("categoria", category);
        query.setLimit(11);
        query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
        query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> lugares, ParseException e) {
				if(e == null && !cancelled){
					int size;
					if(lugares.size() == 11){
						more = true;
						size = 10;
					}else{
						more = false;
						size = lugares.size();
						getListView().removeFooterView(footer);
					}
					for (int i = 0; i < size; i++){
						ParseObject lugar = lugares.get(i);
						Lugares item = new Lugares();
						item.setLugarId((String) lugar.getObjectId());
						item.setName((String) lugar.get("nombre"));
						item.setCategory((String) lugar.get("categoria"));
						item.setCalif((float)lugar.getDouble("calificacion"));
						item.setDesc((String) lugar.get("descripcion"));
						item.setDir( lugar.getString("direccion"));
						item.setEdo(lugar.getString("estado"));
						item.setImagen(lugar.getParseFile("imagen").getUrl());
						item.setGeo(new LatLng(lugar.getParseGeoPoint("localizacion").getLatitude(),
								lugar.getParseGeoPoint("localizacion").getLongitude()));
						lugaresList.add(item);
					}
					adapterList = new ListaLugaresAdapter(activity, lugaresList);
		            // Binds the Adapter to the ListView
		            getListView().setAdapter(adapterList);
				}	
			}
		});
	}
	
	private void updateTop(String categoria){
		
		lugaresList.clear();
		getListView().addFooterView(footer);
		setListAdapter(null);
		
		query = new ParseQuery<ParseObject>(
                "Top5");
        query.orderByAscending("posicion");
        query.include("lugarId");
        query.whereEqualTo("categoria", categoria);
        query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
        query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> top, ParseException e) {
				if(e == null && !cancelled){
					getListView().removeFooterView(footer);
					for (ParseObject lugar : top){
						Lugares item = new Lugares();
						ParseObject res = lugar.getParseObject("lugarId");
						
						item.setLugarId((String) res.getObjectId());
						item.setName( res.getString("nombre"));
						item.setCategory( res.getString("categoria"));
						item.setCalif((float)res.getDouble("calificacion"));
						item.setDesc( res.getString("descripcion"));
						item.setDir( res.getString("direccion"));
						item.setEdo(res.getString("estado"));
						item.setImagen(res.getParseFile("imagen").getUrl());
						item.setGeo(new LatLng(res.getParseGeoPoint("localizacion").getLatitude(),
								res.getParseGeoPoint("localizacion").getLongitude()));
						lugaresList.add(item);
					}
					adapterTop = new TopLugaresAdapter(activity, lugaresList);
		            // Binds the Adapter to the ListView
		            getListView().setAdapter(adapterTop);
				}
			}	
		});
	}
	
	private void loadMore(String category){
		page++;
		query = new ParseQuery<ParseObject>(
                "Lugares");
        query.orderByAscending("createdAt");
        query.whereEqualTo("categoria", category);
        query.setSkip(10*page);
        query.setLimit(11);
        query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> lugares, ParseException e) {
				if(e == null && !cancelled){
					int size;
					if(lugares.size() == 11){
						more = true;
						size = 10;
					}else{
						more = false;
						size = lugares.size();
						getListView().removeFooterView(footer);
					}
					for (int i = 0; i < size; i++){
						ParseObject lugar = lugares.get(i);
						Lugares item = new Lugares();
						item.setLugarId((String) lugar.getObjectId());
						item.setName((String) lugar.get("nombre"));
						item.setCategory((String) lugar.get("categoria"));
						item.setCalif((float)lugar.getDouble("calificacion"));
						item.setDesc((String) lugar.get("descripcion"));
						item.setDir( lugar.getString("direccion"));
						item.setEdo(lugar.getString("estado"));
						item.setImagen(lugar.getParseFile("imagen").getUrl());
						item.setGeo(new LatLng(lugar.getParseGeoPoint("localizacion").getLatitude(),
								lugar.getParseGeoPoint("localizacion").getLongitude()));
						lugaresList.add(item);
					}
					adapterList.notifyDataSetChanged();
				}	
			}
		});
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		callback = null;
		cancelled = true;
	}
}