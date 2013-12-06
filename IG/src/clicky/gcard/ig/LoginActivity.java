package clicky.gcard.ig;

import android.app.Dialog;
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
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class LoginActivity extends ActionBarActivity {
	
	private LoginActivity activity = this;
	

	
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
	
	public void btnAction(View v){
		switch(v.getId()){
		case R.id.btnRegister:
			showRegDialog();
			break;
		case R.id.btnLogin:
			showLoginDialog();
			break;
		case R.id.btnFacebook:
			connectFacebook();
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
		
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(verifyLogin(editUser,editPass)){
					logIn(dialog,editUser.getText().toString(), editPass.getText().toString());
				}
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
		
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(verifyRegister(editUser, editPass, editConfirmPass, editMail)){
					register(dialog,editUser.getText().toString(), editPass.getText().toString(), 
							editMail.getText().toString());
				}
			}
		});

		dialog.show();

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
	
	private void logIn(final Dialog dialog,String userName,String password){
		ParseUser.logInInBackground(userName, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					dialog.dismiss();
					
					ParseInstallation installation = ParseInstallation.getCurrentInstallation();
					PushService.subscribe(activity, "lesbiana", MainActivity.class);
					PushService.subscribe(activity, "gay", MainActivity.class);
					PushService.subscribe(activity, "bisexual", MainActivity.class);
					PushService.subscribe(activity, "transexual", MainActivity.class);
					installation.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException arg0) {
							dialog.dismiss();
							Intent i = new Intent(LoginActivity.this,MainActivity.class);
		        			startActivity(i);
		        			finish();
						}
					});
                } else {
                    Toast.makeText(getApplicationContext(),"User and/or password incorrect",
                        Toast.LENGTH_LONG).show();
                }
            }
        });
	 
	}
	private boolean verifyRegister(EditText user, EditText pass,EditText confPass,EditText mail){
		if(user.getText().toString().equals("")){
			user.setError("Fill this");
			return false;
		}
		if(pass.getText().toString().equals("")){
			pass.setError("Fill this");
			return false;
		}
		if(confPass.getText().toString().equals("")){
			confPass.setError("Fill this");
			return false;
		}
		if(mail.getText().toString().equals("")){
			mail.setError("Fill this");
			return false;
		}
		if(!pass.getText().toString().equals(confPass.getText().toString())){
			confPass.setError("Mismatch");
			return false;
		}
		return true;
	}
	private void register(final Dialog dialog,String userName,String password,String mail){
		ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setEmail(mail);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
        	public void done(ParseException e) {
        		if (e == null) {
        			
					ParseInstallation installation = ParseInstallation.getCurrentInstallation();
					PushService.subscribe(activity, "lesbiana", MainActivity.class);
					PushService.subscribe(activity, "gay", MainActivity.class);
					PushService.subscribe(activity, "bisexual", MainActivity.class);
					PushService.subscribe(activity, "transexual", MainActivity.class);
					installation.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException arg0) {
							dialog.dismiss();
							Intent i = new Intent(LoginActivity.this,MainActivity.class);
		        			startActivity(i);
		        			finish();
						}
					});
					
                } else {
                	if(e.getCode() == 202)
                    Toast.makeText(getApplicationContext(),"User already exists", Toast.LENGTH_LONG).show();
                }
        	}
        });
	}
	private void connectFacebook(){
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			 @Override
			 public void done(ParseUser user, ParseException err) {
				 if (user == null) {
					 Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
				 } else if (user.isNew()) {
					 Intent i = new Intent(LoginActivity.this,MainActivity.class);
					 startActivity(i);
					 finish();
				 } else {
					 Intent i = new Intent(LoginActivity.this,MainActivity.class);
					 startActivity(i);
					 finish();
			 	}
			 }
		});
	}
}
