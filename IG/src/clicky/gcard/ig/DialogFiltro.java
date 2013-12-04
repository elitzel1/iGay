package clicky.gcard.ig;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogFiltro extends DialogFragment implements OnMultiChoiceClickListener {


	public boolean[] arr = new boolean[7];
	NoticeDialogInterface mCallback;
	
	public interface NoticeDialogInterface{
		public void onDialogPositiveClic(boolean[] arr);
		public void onDialogNegativeClic();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		try{
			mCallback = (NoticeDialogInterface)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString());
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
	
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle(R.string.dfil).setMultiChoiceItems(getResources().getStringArray(R.array.categorias), null, this).
		setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				mCallback.onDialogPositiveClic(arr);
				
			}
		}).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
					mCallback.onDialogNegativeClic();
					dialog.cancel();
				
			}
		});
		
		
		return builder.create();
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		arr[which] = checked(arr[which],isChecked);
	}
	
	private boolean checked(boolean num, boolean isChecked){
		if(isChecked==true)
			num=true;
		else
			num=false;
		return num;
	}
	
	
	public boolean[] getOptionsChecked(){
		return arr;
	}
	
	public void onDestroy(){
		super.onDestroy();
		mCallback=null;
	}
}
