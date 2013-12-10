package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.parse.PushService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogAjustesNot extends DialogFragment implements OnMultiChoiceClickListener {

	public boolean[] tipos = new boolean[4];
	AjustesNotListener mCallback;
	Set<String> suscriptions;
	
	
	public interface AjustesNotListener{
		public void onPositiveClic(boolean tipos[], DialogAjustesNot frag);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mCallback=(AjustesNotListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException("Error in DialogAjustes");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		suscriptions =  PushService.getSubscriptions(getActivity());
		String[] array = getResources().getStringArray(R.array.tipos);
		for(int i = 0; i< array.length;i++){
			if(suscriptions.contains(array[i])){
					tipos[i] = true;
				}
		}
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dfil).setMultiChoiceItems(getResources().getStringArray(R.array.tipos), tipos, this).
		setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCallback.onPositiveClic(tipos,DialogAjustesNot.this);
				
			}
		}).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		
		return builder.create();
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		tipos[which]=checked(tipos[which],isChecked);
		
	}
	
	private boolean checked(boolean num,boolean isChecked){
		if(isChecked==true)
			num=true;
			else
				num=false;
		return num;
		
	}
	
	public void changeChannels(ArrayList<String> num){
		
		for(int i=0;i<num.size();i++){
			if(!suscriptions.contains(num.get(i))){
				PushService.subscribe(getActivity(), num.get(i), MainActivity.class);
			}
		}
		Iterator<String> iterator = suscriptions.iterator();
		while(iterator.hasNext()){
			String tipo = iterator.next();
			if(!num.contains(tipo)){
				PushService.unsubscribe(getActivity(), tipo);
			}
		}
		
	}
	
	public boolean[] getOptions(){
		return tipos;
	}
	
	public void onDestroy(){
		super.onDestroy();
		mCallback = null;
	}
}
