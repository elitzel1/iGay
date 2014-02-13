package clicky.gcard.ig;

import com.parse.ParseUser;

import clicky.gcard.ig.adapters.AdapterAbout;
import clicky.gcard.ig.adapters.AdapterListaNotificaciones;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AjustesFragment extends ListFragment {

	String values[] = new String[]{"Configuración de Notificaciones",
			"Redes sociales","Acerca de","Privacidad","Finalizar sesión"};
	
	String redes[] = new String[]{"AppJ","@AppJ","appj@cl1cky.com"};
	int logos[] = new int[]{R.drawable.facebook_icon,R.drawable.twitter_icon,android.R.drawable.ic_dialog_email};
	
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
	}
	
	@Override
	public void onResume(){
		super.onResume();
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
				i.putExtra("inside", true);
				startActivity(i);
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
	
	private void showAbout(){
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setTitle(R.string.about);
		View v = activity.getLayoutInflater().inflate(R.layout.ajustes_layout, null);
		alert.setView(v);
		ListView lista = (ListView)v.findViewById(R.id.listaAbout);
		lista.setAdapter(new AdapterAbout(activity, redes, logos));
		lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				switch(pos){
					case 0:
						String urlFace = "http://www.google.com";
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(urlFace));
						startActivity(i);
						break;
					case 1:
						String urlTweet = "http://www.google.com";
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(urlTweet));
						startActivity(intent);
						break;
					case 2:
						Intent email = new Intent(Intent.ACTION_SEND);
						email.setType("text/html");
						email.putExtra(Intent.EXTRA_EMAIL, new String[]{"appj@cl1cky.com"});		  
						startActivity(Intent.createChooser(email, activity.getString(R.string.txt_mail)));
						break;	
				}
			}
		});
		
		alert.setPositiveButton(R.string.btn_ok, new OnClickListener() {
			
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
			showAbout();
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
