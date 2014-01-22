package clicky.gcard.ig;

import com.parse.ParseUser;

import clicky.gcard.ig.adapters.AdapterListaNotificaciones;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AjustesFragment extends ListFragment {

	String values[] = new String[]{"Configuración de Notificaciones",
			"Redes sociales","Acerca de","Privacidad","Finalizar sesión"};
	
	private Activity activity;
	private ParseUser user;
	
	onListItemClicConf mCallback;
	
	public interface onListItemClicConf{
		public void onDialogNot();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
		
		View rootView =  inflater.inflate(R.layout.frag_ajustes, container, false);
		
		return rootView;
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setListAdapter(new AdapterListaNotificaciones(getActivity(),values));
		
		user = ParseUser.getCurrentUser();
		
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
	
	private void showAlert(){
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setMessage(R.string.alert_no_login);
		alert.setPositiveButton(R.string.btn_ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent i = new Intent(activity,LoginActivity.class);
				startActivity(i);
				activity.finish();
			}
		});
		alert.setNegativeButton(R.string.btn_cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.create().show();
	}
	
	public void onListItemClick(ListView list,View view, int position, long id){
		super.onListItemClick(list, view, position, id);
		Intent i;
		switch(position){
		case 0:
			if(user != null)
				mCallback.onDialogNot();
			else{
				showAlert();
			}
			break;
		case 1:
			if(user != null){
				i = new Intent(activity,RedesSocialesActivity.class);
				startActivity(i);
			}else{
				showAlert();
			}
			break;
		case 2:
			break;
		case 3:
			i = new Intent(activity,PrivacidadActivity.class);
			startActivity(i);
			break;
		case 4:
			if(user != null){
				ParseUser.logOut();
				i = new Intent(activity,LoginActivity.class);
				startActivity(i);
				activity.finish();
			}else{
				showAlert();
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
