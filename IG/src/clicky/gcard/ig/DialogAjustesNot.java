package clicky.gcard.ig;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogAjustesNot extends DialogFragment implements OnMultiChoiceClickListener {

	public static boolean[] tipos = new boolean[5];
	AjustesNotListener mCallback;
	
	
	public interface AjustesNotListener{
		public void onPositiveClic(boolean tipos[]);
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
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dfil).setMultiChoiceItems(getResources().getStringArray(R.array.tipos), tipos, this).
		setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCallback.onPositiveClic(tipos);
				
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
	
	public boolean[] getOptions(){
		return tipos;
	}
	
	public void onDestroy(){
		super.onDestroy();
		mCallback = null;
	}
}
