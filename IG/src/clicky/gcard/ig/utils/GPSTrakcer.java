package clicky.gcard.ig.utils;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
public class GPSTrakcer extends Service implements LocationListener {

	private final Context context;
	boolean isGPSEnable=false;
	boolean isEnableWifi=false;
	boolean canGetLocation = false;
	
	Location location;
	double lat;
	double log;
	
	
	protected LocationManager locationManager;
	
	public GPSTrakcer (Context context){
		this.context = context;
		locationManager=(LocationManager)context.getSystemService(LOCATION_SERVICE);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			isGPSEnable=true;
	}
	
	public Location getLocation(){

		
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(provider);
		
		if(location!=null){
			canGetLocation=true;
			lat=location.getLatitude();
			log=location.getLongitude();
			return location;
		}
		else return null;
		
	}
	
	/***
	 * Stop using GPS listener
	 * ****/
	public void stopGPS(){
		if(locationManager!=null)
			locationManager.removeUpdates(GPSTrakcer.this);
	}
	
	public double getLatitude (){
		return lat;
	}
	
	public double getLongitude(){
		return log;
	}
	
	public boolean canGetLocation(){
		return canGetLocation;
	}
	
	public boolean isGPSEnable(){
		return isGPSEnable;
	}
	
	public void showAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("GPS Settings");
		alertDialog.setMessage("Para un mejor funcionamiento ¿Deseas activar tu GPS?");
		
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(i);
			}
		});
		
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		
		alertDialog.show();
	}
	
	

	@Override
	public void onLocationChanged(Location location) {
		Log.i("Location", "Locacion nueva:"+location.getLatitude()+" "+getLongitude());
		lat = location.getLatitude();
		log = location.getLongitude();
		
		this.location = location;
		
	}

	@Override
	public void onProviderDisabled(String provider) {

		Log.i("Location", "No Nuevo proveedor: "+provider);
		
	}

	@Override
	public void onProviderEnabled(String provider) {

		Log.i("Location", "Nuevo proveedor: "+provider);
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}


}
