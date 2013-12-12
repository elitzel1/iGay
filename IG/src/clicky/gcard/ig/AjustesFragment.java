package clicky.gcard.ig;

import com.parse.ParseUser;

import clicky.gcard.ig.adapters.AdapterListaNotificaciones;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AjustesFragment extends ListFragment {

	String values[] = new String[]{"Configuración de Cuenta","Configuración de Notificaciones",
			"Redes sociales","Acerca de","Privacidad","Finalizar sesión"};
	
	private ListView list;
	private Activity activity;
	
	onListItemClicConf mCallback;
	
	public interface onListItemClicConf{
		public void onDialogNot();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
		
		View rootView =  inflater.inflate(R.layout.frag_ajustes, container, false);
		list=(ListView)rootView.findViewById(android.R.id.list);
		return rootView;
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setListAdapter(new AdapterListaNotificaciones(getActivity(),values));
		
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        
        try{
        	mCallback=(onListItemClicConf)activity;
        }catch(ClassCastException e){
        	Log.i("", "");
        }
	}
	
	public void onListItemClick(ListView list,View view, int position, long id){
		super.onListItemClick(list, view, position, id);
		Intent i;
		switch(position){
		case 0:
			i = new Intent(activity,CuentaActivity.class);
			startActivity(i);
			break;
		case 1:
			mCallback.onDialogNot();
			break;
		case 2:
			i = new Intent(activity,RedesSocialesActivity.class);
			startActivity(i);
			break;
		case 3:
			break;
		case 4:
			i = new Intent(activity,PrivacidadActivity.class);
			startActivity(i);
			break;
		case 5:
			ParseUser user = ParseUser.getCurrentUser();
			if(user != null){
				ParseUser.logOut();
				i = new Intent(activity,LoginActivity.class);
				startActivity(i);
				activity.finish();
			}
			break;
		default:
			break;
				
		}
		
	}
	
	public void onDestroy(){
		super.onDestroy();
		mCallback=null;
	}
}
