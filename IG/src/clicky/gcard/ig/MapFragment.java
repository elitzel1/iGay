package clicky.gcard.ig;

import clicky.gcard.ig.adapters.GPSTrakcer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {
	public String TAG = "MAPA";
	private GoogleMap mapa;
	View view;
	LatLng coordenadas;
	
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
		} 
				setUpMap();
				
			return view;
		
	}
	
	public void onResume(){
		super.onResume();
		
		if(coordenadas==null)
		setUpGPS();
		
		
	}
	
	
	
	private void setUpMap(){

	mapa =((SupportMapFragment)getFragmentManager().findFragmentById(R.id.mapp)).getMap();
	
	if(coordenadas!=null)
	mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordenadas.latitude, coordenadas.longitude),15));
	mapa.setMyLocationEnabled(true);
			Log.i("", "");
			if(mapa!=null)
				
				Log.i("Mapa", "No nulo");
				setUpMarker();
		
	}
	

	public void setUpMarker(){
		
		mapa.addMarker(new MarkerOptions().position(new LatLng(19.33283,-99.18557)).title("Las islas"));
		mapa.addMarker(new MarkerOptions().position(new LatLng(19.331473,-99.331473)).title("Algo aqui"));
		mapa.addMarker(new MarkerOptions().position(new LatLng(19.331924,-99.189216)).title("Aqui hay otra cosa"));

	}
}
