package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clicky.gcard.ig.adapters.GPSTrakcer;
import clicky.gcard.ig.datos.Lugares;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
	private LinearLayout detalles;
	private TextView txtNombre;
	private RatingBar calif;
	private Animation showDetalle;
	
	
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
		
		/**Ubicaci��n del usuario***/
		//LatLng coordenadas;
		setUpGPS();
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
	
	if(coordenadas!=null){
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordenadas.latitude, coordenadas.longitude),12.5f));
		getLugares(coordenadas, 5);
	}
			Log.i("", "");
			if(mapa!=null)
				
				Log.i("Mapa", "No nulo");
				//setUpMarker();
		
	}
	

	public void setUpMarker(){
		mapa.clear();
		
		for(int i = 0; i < lugaresList.size(); i++){
			Marker mark = mapa.addMarker(new MarkerOptions()
					.position(lugaresList.get(i).getGeo())
					.title(lugaresList.get(i).getName()));
			markersList.put(mark.getId(), lugaresList.get(i));
		}


		mapa.setOnMarkerClickListener(this);
	}
	
	

	@Override
	public boolean onMarkerClick(Marker marker) {
		lugar = markersList.get(marker.getId());
		showDetail(markersList.get(marker.getId()));
		return false;
	}
	
	private void showDetail(Lugares lugar){
		txtNombre.setText(lugar.getName());
		calif.setRating(lugar.getCalif());
		detalles.setVisibility(View.VISIBLE);
		detalles.startAnimation(showDetalle);
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
					setUpMarker();
				}
			}

		});
		
	}
}
