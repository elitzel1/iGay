package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import clicky.gcard.ig.adapters.NotificacionesAdapter;
import clicky.gcard.ig.datos.Notificacion;
import clicky.gcard.ig.utils.NotificationsFile;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotificacionesFragment extends ListFragment{
	
	private View view;
	private NotificacionesAdapter adapter;
	private List<Notificacion> notificationList = null;
	
	private Activity activity;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		if(view==null){
			try{
		view = inflater.inflate(R.layout.notificaciones_layout,null,false);
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
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
     
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
       
		notificationList = new ArrayList<Notificacion>();
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		getNotificaciones();
	}
	
	private void getNotificaciones(){
		JSONArray array = NotificationsFile.getNotifications(activity, "notificaciones");
		
		for(int i = 0; i < array.length(); i++){
			try {
				JSONObject object = array.getJSONObject(i);
				Notificacion notification = new Notificacion();
				
				notification.setTitulo(object.getString("titulo"));
				notification.setDesc(object.getString("descripcion"));
				notification.setLink(object.getString("link"));
				notification.setFecha(object.getString("fecha"));
				
				notificationList.add(notification);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		adapter = new NotificacionesAdapter(activity, notificationList);
		getListView().setAdapter(adapter);
	}

	
}
