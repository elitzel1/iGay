package clicky.gcard.ig;

import java.util.ArrayList;
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
	Object[] suscriptions;
	
	
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
		suscriptions =  PushService.getSubscriptions(getActivity()).toArray();
		String[] array = getResources().getStringArray(R.array.tipos);
		for(int i = 0; i< array.length;i++){
			for(int j = 0 ; j<suscriptions.length;j++){
				if(array[i].equals(suscriptions[j])){
					tipos[i] = true;
				}
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
			out:
			for(int j = 0; j < suscriptions.length; j++){
				if(num.get(i).equals(suscriptions[j])){
					break out;
				}
				PushService.subscribe(getActivity(), num.get(i), MainActivity.class);
			}
		}
		for(int i=0;i<suscriptions.length;i++){
			out:
			for(int j = 0; j < num.size(); j++){
				if(suscriptions[i].equals(num.get(j))){
					break out;
				}
				PushService.unsubscribe(getActivity(), suscriptions[i].toString());
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
