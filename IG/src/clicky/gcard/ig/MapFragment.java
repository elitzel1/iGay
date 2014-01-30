package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clicky.gcard.ig.datos.Lugares;
import clicky.gcard.ig.utils.GPSTrakcer;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MapFragment extends Fragment implements OnMarkerClickListener {
	public String TAG = "MAPA";
	private GoogleMap mapa;
	View view;
	LatLng coordenadas;
	OnButtonListener mCallback;
	private List<Lugares> lugaresList = null;
	private HashMap<String, Lugares> markersList = new HashMap<String, Lugares>();
	private Lugares lugar;
	private boolean visible = false;
	private LinearLayout detalles;
	private TextView txtNombre;
	private RatingBar calif;
	private Animation showDetalle,hideDetalle;
	
	
	public interface OnButtonListener{
		public void onArticleSelected(Lugares lugar);
	}
	
	private void setUpGPS(){
		GPSTrakcer gps = new GPSTrakcer(getActivity().getBaseContext());
		if(gps.canGetLocation())
		{
			coordenadas = new LatLng(gps.getLatitude(), gps.getLongitude());
			Log.i("LALA", ""+gps.getLatitude() + " "+gps.getLongitude());
		}
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		lugaresList = new ArrayList<Lugares>();
		showDetalle = AnimationUtils.loadAnimation(getActivity(), R.anim.show_detalle);
		hideDetalle = AnimationUtils.loadAnimation(getActivity(), R.anim.hide_detalle);
		hideDetalle.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				detalles.setVisibility(View.INVISIBLE);
			}
		});
		
		
		
		/**Ubicación del usuario***/
		//LatLng coordenadas;
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getBaseContext());
 
        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
 
        }
        else{
        	Log.i("Servicios", "Activados");
        }
		/*************************/
		
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState){
		
		if(view!=null){
			ViewGroup group = (ViewGroup) view.getParent();
			if(group!= null){
				group.removeView(view);
			}
		}
		else{
				view = inflater.inflate(R.layout.layout_mapa,null,false);
				detalles = (LinearLayout)view.findViewById(R.id.detalle);
				txtNombre = (TextView)view.findViewById(R.id.txtNombre);
				calif = (RatingBar)view.findViewById(R.id.rateLugar);
				Button btnInfo = (Button)view.findViewById(R.id.btnInfo);
				btnInfo.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mCallback.onArticleSelected(lugar);
					}
				});
		} 
				setUpMap();
				
			return view;
		
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mCallback = (OnButtonListener)activity;
		}catch(ClassCastException c){}
		
	}
	public void onResume(){
		super.onResume();
		ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		actionBar.setTitle("J");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		if(coordenadas==null)
		setUpGPS();
		
		
	}
	
	
	private void setUpMap(){

	mapa =((SupportMapFragment)getFragmentManager().findFragmentById(R.id.mapp)).getMap();
	
	mapa.setMyLocationEnabled(true);
	mapa.setOnMapClickListener(new OnMapClickListener() {
		@Override
		public void onMapClick(LatLng point) {
			hideDetail();
		}
	});
	
	setUpGPS();
	if(coordenadas!=null){
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordenadas.latitude, coordenadas.longitude),12.5f));
		getLugares(coordenadas, 8);
	}
			Log.i("", "");
			if(mapa!=null)
				
				Log.i("Mapa", "No nulo");
				//setUpMarker();
		
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
	public void setUpMarker(List<Lugares> listaLugares){
		mapa.clear();
		
		Log.i("Markers", ""+listaLugares.size());
		
		for(int i = 0; i < listaLugares.size(); i++){

			getCategoryId(listaLugares, i);
			
		}
		mapa.setOnMarkerClickListener(this);
		Log.i("Markers", "Marcadores: "+markersList.size());
	}
	
	private void getCategoryId(List<Lugares> listaLugares, int i){
		String cat = listaLugares.get(i).getCategory();
	
		if(cat.equals("Antros y Bares")){
			Marker mark = mapa.addMarker(new MarkerOptions()
			.position(listaLugares.get(i).getGeo()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinantro)));
			markersList.put(mark.getId(), listaLugares.get(i));
			Log.i("Markers", "Antros: "+i);
			//id = R.drawable.pinantro;

		}
		if(cat.equals("Comida")){
			Marker mark = mapa.addMarker(new MarkerOptions()
			.position(listaLugares.get(i).getGeo()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinrestaurante)));
			markersList.put(mark.getId(), listaLugares.get(i));
			Log.i("Markers", "Comida: "+i);
			//id=R.drawable.pinrestaurante;
			
		}
		if(cat.equals("Cafeteria")){
			Marker mark = mapa.addMarker(new MarkerOptions()
			.position(listaLugares.get(i).getGeo()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pincafe)));
			markersList.put(mark.getId(), listaLugares.get(i));
			Log.i("Markers", "Cafeteria: "+i);
		//	id=R.drawable.pincafe;
			
		}
		if(cat.equals("Hotel")){
			Marker mark = mapa.addMarker(new MarkerOptions()
			.position(listaLugares.get(i).getGeo()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinhotel)));
			markersList.put(mark.getId(), listaLugares.get(i));
			Log.i("Markers", "Hotel: "+i);
		//	id=R.drawable.pinhotel;
	
		}
		if(cat.equals("Cultural")){
			Marker mark = mapa.addMarker(new MarkerOptions()
			.position(listaLugares.get(i).getGeo()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pincultural)));
			markersList.put(mark.getId(), listaLugares.get(i));
			Log.i("Markers", "Cultural: "+i);
		//	id=R.drawable.pincultural;
			
		}
		if(cat.equals("Tienda")){
			Marker mark = mapa.addMarker(new MarkerOptions()
			.position(listaLugares.get(i).getGeo()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pintienda)));
			markersList.put(mark.getId(), listaLugares.get(i));
			Log.i("Markers", "Tienda: "+i);
			//id=R.drawable.pintienda;
			
		}
		if(cat.equals("Cuidado personal")){
			Marker mark = mapa.addMarker(new MarkerOptions()
			.position(listaLugares.get(i).getGeo()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pincpersonal)));
			markersList.put(mark.getId(), listaLugares.get(i));
			Log.i("Markers", "CP: "+i);
		//	id=R.drawable.pincpersonal;
		
		}
		//return id;
	}
	
	

	@Override
	public boolean onMarkerClick(Marker marker) {
		lugar = markersList.get(marker.getId());
		showDetail(markersList.get(marker.getId()));
		return false;
	}
	
	private void showDetail(Lugares lugar){
		try{
		txtNombre.setText(lugar.getName());
		calif.setRating(lugar.getCalif());
		detalles.setVisibility(View.VISIBLE);
		visible = true;
		detalles.startAnimation(showDetalle);
		}catch(NullPointerException e){
			Toast.makeText(getActivity(), "Espere un momento", Toast.LENGTH_LONG).show();
		}
	}
	
	public void hideDetail(){
		if(visible){
			detalles.startAnimation(hideDetalle);
			visible = false;
		}
	}
	
	private void getLugares(LatLng position, double kilometros){
		lugaresList.clear();
		
		ParseQuery<ParseObject> query =  new ParseQuery<ParseObject>("Lugares");
		query.whereWithinKilometers("localizacion", new ParseGeoPoint(position.latitude, position.longitude) , kilometros);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> lugares, ParseException e) {
				if(lugares.isEmpty()){
					Toast.makeText(getActivity(), "No se encontraron lugares cerca de ti", Toast.LENGTH_SHORT).show();
				}else{
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
					setUpMarker(lugaresList);
				}
			}

		});
		
	}
	
	public void filter(ArrayList<String> num){
		
		List<Lugares> filtroLugares = new ArrayList<Lugares>();
		
		for(Lugares lu : lugaresList){
			for(int i=0;i<num.size();i++){
				if(lu.getCategory().equals(num.get(i)))
					filtroLugares.add(lu);
			}
		}
		setUpMarker(filtroLugares);
	}
	
	public class MarkerAnsyc extends AsyncTask<Lugares,String,String>{

	
		@Override
	    protected void onPreExecute() {
			mapa.clear();
			    }
		


		@Override
		protected String doInBackground(Lugares... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
			
		
		
		
		
		
		
		@Override
		  protected void onProgressUpdate(String...id) {
		//	setupMarker(id[0],lat,lng);
			 
		  }
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//setupMarker(result, coor_est);
			
		}
		
		
		
	}
	

}
