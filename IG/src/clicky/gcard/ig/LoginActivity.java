package clicky.gcard.ig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class LoginActivity extends ActionBarActivity {
	
	private LoginActivity activity = this;
	private ProgressDialog loading;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_layout);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		ParseUser user = ParseUser.getCurrentUser();
		Log.i("LA", "On Created");
		if(user != null){
			Intent i = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(i);
			finish();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
	
	public void btnAction(View v){
		switch(v.getId()){
		case R.id.btnRegister:
			showRegDialog();
			break;
		case R.id.btnLogin:
			showLoginDialog();
			break;
		case R.id.btnContinue:
			Intent i = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(i);
			finish();
			break;
		}
	}
	
	private void showLoginDialog(){
		final Dialog dialog = new Dialog(this,R.style.ThemeDialogCustom);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.login_layout);

		final EditText editUser = (EditText) dialog.findViewById(R.id.editUser);
		final EditText editPass = (EditText) dialog.findViewById(R.id.editPass);
		
		Button btnLogin = (Button) dialog.findViewById(R.id.btnLogin);
		Button btnFace = (Button) dialog.findViewById(R.id.btnFacebook);
		Button btnForget = (Button) dialog.findViewById(R.id.btnForget);
		
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(verifyLogin(editUser,editPass)){
					logIn(dialog,editUser.getText().toString(), editPass.getText().toString());
				}
			}
		});
		
		btnFace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connectFacebook(dialog);
			}
		});
		
		btnForget.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showMailDialog(dialog);
			}
		});
		
		dialog.show();
	}
	
	private void showRegDialog(){
		final Dialog dialog = new Dialog(this,R.style.ThemeDialogCustom);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.register_layout);

		final EditText editUser = (EditText) dialog.findViewById(R.id.editUser);
		final EditText editPass = (EditText) dialog.findViewById(R.id.editPass);
		final EditText editConfirmPass = (EditText) dialog.findViewById(R.id.editConfirmPass);
		final EditText editMail = (EditText) dialog.findViewById(R.id.editMail);
		
		Button dialogButton = (Button) dialog.findViewById(R.id.btnReg);
		Button btnFace = (Button) dialog.findViewById(R.id.btnFacebook);
		
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(verifyRegister(editUser, editPass, editConfirmPass, editMail)){
					register(dialog,editUser.getText().toString(), editPass.getText().toString(), 
							editMail.getText().toString());
				}
			}
		});
		
		btnFace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connectFacebook(dialog);
			}
		});

		dialog.show();

	}
	
	private void showMailDialog(Dialog dialogo){
		dialogo.dismiss();
		
		final Dialog dialog = new Dialog(this,R.style.ThemeDialogCustom);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.forget_password_layout);
		
		final EditText editMail = (EditText) dialog.findViewById(R.id.editMail);
		
		Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(verifyMail(editMail))
					resetPassword(dialog,editMail.getText().toString());
			}
		});
		dialog.show();
	}
	
	private void showAlert(int message){
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setMessage(message);
		alert.setNeutralButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.create().show();
	}
	private boolean verifyLogin(EditText user,EditText pass){
		if(user.getText().toString().equals("")){
			user.setError("Fill this");
			return false;
		}
		if(pass.getText().toString().equals("")){
			pass.setError("Fill this");
			return false;
		}
		return true;
	}
	
	private void resetPassword(final Dialog dialog,String mail){
		dialog.dismiss();
		loading = new ProgressDialog(activity);
		loading.setMessage("Wait a moment...");
		loading.setCancelable(false);
		loading.show();
		
		ParseUser.requestPasswordResetInBackground(mail, new RequestPasswordResetCallback() {
			public void done(ParseException e) {
				if (e == null) {
					loading.dismiss();
					showAlert(R.string.alert_email);
				} else {
					loading.dismiss();
					showAlert(R.string.alert_no_email);
				}
			}
		});
	}
	
	private void logIn(final Dialog dialog,String userName,String password){
		dialog.dismiss();
		loading = new ProgressDialog(activity);
		loading.setMessage("Wait a moment...");
		loading.setCancelable(false);
		loading.show();
		ParseUser.logInInBackground(userName, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					loading.dismiss();
					Intent i = new Intent(LoginActivity.this,MainActivity.class);
	       			startActivity(i);
        			finish();
                } else {
                	loading.dismiss();
                    Toast.makeText(getApplicationContext(),"User and/or password incorrect",
                        Toast.LENGTH_LONG).show();
                }
            }
        });
	 
	}
	
	private boolean verifyMail(EditText mail){
		if(mail.getText().toString().equals("")){
			mail.setError("Fill this");
			return false;
		}
		return true;
	}
	private boolean verifyRegister(EditText user, EditText pass,EditText confPass,EditText mail){
		boolean acepta = true;
		if(user.getText().toString().equals("")){
			user.setError("Fill this");
			acepta = false;
		}
		if(pass.getText().toString().equals("")){
			pass.setError("Fill this");
			acepta = false;
		}
		if(confPass.getText().toString().equals("")){
			confPass.setError("Fill this");
			acepta = false;
		}
		if(mail.getText().toString().equals("")){
			mail.setError("Fill this");
			acepta = false;
		}
		if(!pass.getText().toString().equals(confPass.getText().toString())){
			confPass.setError("Mismatch");
			acepta = false;
		}
		return acepta;
	}
	private void register(final Dialog dialog,String userName,String password,String mail){
		dialog.dismiss();
		loading = new ProgressDialog(activity);
		loading.setMessage("Wait a moment...");
		loading.setCancelable(false);
		loading.show();
		ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setEmail(mail);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
        	public void done(ParseException e) {
        		if (e == null) {
        			
					ParseInstallation installation = ParseInstallation.getCurrentInstallation();
					PushService.subscribe(activity, "Lesbico", MainActivity.class);
					PushService.subscribe(activity, "Gay", MainActivity.class);
					PushService.subscribe(activity, "Bisexual", MainActivity.class);
					PushService.subscribe(activity, "Transexual", MainActivity.class);
					installation.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException arg0) {
							loading.dismiss();
							Intent i = new Intent(LoginActivity.this,MainActivity.class);
		        			startActivity(i);
		        			finish();
						}
					});
					
                } else {
                	loading.dismiss();
                	if(e.getCode() == 202)
                		Toast.makeText(getApplicationContext(),"User already exists", Toast.LENGTH_LONG).show();
                }
        	}
        });
	}
	private void connectFacebook(final Dialog dialog){
		
		dialog.dismiss();
		
		loading = new ProgressDialog(activity);
		loading.setMessage("Wait a moment...");
		loading.setCancelable(false);
		loading.show();
		
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			 @Override
			 public void done(ParseUser user, ParseException err) {
				 dialog.dismiss();
				 if (user == null) {
					 //loading.dismiss();
					 Log.i("MyApp", "Error");
				 } else if (user.isNew()) {
					 Log.i("MyApp", "New user");
					 loading = new ProgressDialog(activity);
					 loading.setMessage("Wait a moment...");
					 loading.setCancelable(false);
					 loading.show();
						
					 ParseInstallation installation = ParseInstallation.getCurrentInstallation();
					 PushService.subscribe(activity, "Lesbico", MainActivity.class);
					 PushService.subscribe(activity, "Gay", MainActivity.class);
					 PushService.subscribe(activity, "Bisexual", MainActivity.class);
					 PushService.subscribe(activity, "Transexual", MainActivity.class);
					 installation.saveInBackground(new SaveCallback() {
						
						 @Override
						 public void done(ParseException arg0) {
							 loading.dismiss();
							 Intent i = new Intent(LoginActivity.this,MainActivity.class);
							 startActivity(i);
							 finish();
						}
					});
				 } else {
					 Log.i("MyApp", "Log In");
					 
					 loading = new ProgressDialog(activity);
					 loading.setMessage("Wait a moment...");
					 loading.setCancelable(false);
					 loading.show();
					 
					 ParseInstallation installation = ParseInstallation.getCurrentInstallation();
					 PushService.subscribe(activity, "Lesbico", MainActivity.class);
					 PushService.subscribe(activity, "Gay", MainActivity.class);
					 PushService.subscribe(activity, "Bisexual", MainActivity.class);
					 PushService.subscribe(activity, "Transexual", MainActivity.class);
					 installation.saveInBackground(new SaveCallback() {
						 @Override
						 public void done(ParseException arg0) {
							 loading.dismiss();
							 Intent intent = new Intent(LoginActivity.this,MainActivity.class);
							 startActivity(intent);
							 finish();
						 }
					 });
			 	}
			 }
		});
	}
}
