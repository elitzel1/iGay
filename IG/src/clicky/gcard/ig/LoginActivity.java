package clicky.gcard.ig;

import java.util.Arrays;
import java.util.List;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
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
	private Animation hide,show,fadeIn,fadeOut;
	
	private LinearLayout loginLayout,registerLayout;
	private ImageView imgBlur;
	private EditText user,pass,mail,confpass;
	private Button btnAceptar,btnForget;
	
	private boolean inside = false;
	private static String mailValidation = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+
			"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_layout);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		Bundle extras = getIntent().getExtras();
		if(extras != null)
			inside = extras.getBoolean("inside");
		
		ParseUser user = ParseUser.getCurrentUser();
		Log.i("LA", "On Created");
		if(user != null){
			toMain();
		}
		
		loginLayout = (LinearLayout)findViewById(R.id.logLayout);
		registerLayout = (LinearLayout)findViewById(R.id.regLayout);
		imgBlur = (ImageView)findViewById(R.id.imgBlur);
		
		show = AnimationUtils.loadAnimation(this, R.anim.show_detalle);
		hide = AnimationUtils.loadAnimation(this, R.anim.hide_detalle);
		fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		
		loading = new ProgressDialog(activity);
		loading.setMessage(getString(R.string.alert_wait));
		loading.setCancelable(false);
		
		hide.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				loginLayout.setVisibility(View.GONE);
				registerLayout.setVisibility(View.GONE);
				imgBlur.setVisibility(View.GONE);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
	
	@Override
	public void onBackPressed(){
		if(loginLayout.isShown()){
			loginLayout.startAnimation(hide);
			imgBlur.startAnimation(fadeOut);
		}else if(registerLayout.isShown()){
			registerLayout.startAnimation(hide);
			imgBlur.startAnimation(fadeOut);
		}else{
			super.onBackPressed();
		}
	}
	
	public void btnAction(View v){
		switch(v.getId()){
		case R.id.btnRegister:
			showReg();
			break;
		case R.id.btnLogin:
			showLogin();
			break;
		case R.id.btnFacebook:
			connectFacebook();
			break;
		case R.id.imgBlur:	
			hideView();
			break;
		case R.id.btnContinue:
			toMain();
			break;
		}
	}
	
	private void showLogin(){
		
		if(!loginLayout.isShown()){
			loginLayout.setVisibility(View.VISIBLE);
			loginLayout.startAnimation(show);
			
			imgBlur.setVisibility(View.VISIBLE);
			imgBlur.startAnimation(fadeIn);
		}
		user = (EditText) findViewById(R.id.editUser);
		pass = (EditText) findViewById(R.id.editPass);
		
		btnForget = (Button) findViewById(R.id.btnForget);
		btnAceptar = (Button) findViewById(R.id.btnLog);
		
		btnAceptar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(verifyLogin(user,pass)){
					logIn(user.getText().toString(), pass.getText().toString());
					user.setText("");
					pass.setText("");
				}
			}
		});
		
		btnForget.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideView();
				showMailDialog();
			}
		});
		
	}
	
	private void showReg(){
		
		if(!registerLayout.isShown()){
			registerLayout.setVisibility(View.VISIBLE);
			registerLayout.startAnimation(show);
			
			imgBlur.setVisibility(View.VISIBLE);
			imgBlur.startAnimation(fadeIn);
		}
		
		user = (EditText) findViewById(R.id.editUserReg);
		pass = (EditText) findViewById(R.id.editPassReg);
		confpass = (EditText) findViewById(R.id.editConfirmPass);
		mail = (EditText) findViewById(R.id.editMail);
		
		btnAceptar = (Button) findViewById(R.id.btnReg);
		
		btnAceptar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(verifyRegister(user, pass, confpass, mail)){
					register(user.getText().toString(), pass.getText().toString(),mail.getText().toString());
					user.setText("");
					pass.setText("");
					confpass.setText("");
					mail.setText("");
				}
			}
		});
		
	}
	
	private void showMailDialog(){
		
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
		boolean acepta = true;
		if(user.getText().toString().equals("")){
			user.setError("Fill this");
			acepta = false;
		}
		if(pass.getText().toString().equals("")){
			pass.setError("Fill this");
			acepta = false;
		}
		return acepta;
	}
	
	private void resetPassword(final Dialog dialog,String mail){
		dialog.dismiss();
		
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
	
	private void logIn(String userName,String password){
		
		loading.show();
		ParseUser.logInInBackground(userName, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					getInstallation(false);
                } else {
                	loading.dismiss();
                	hideView();
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
		}else if(!mail.getText().toString().matches(mailValidation)){
			mail.setError("Incorrect Format");
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
		}else if(!mail.getText().toString().matches(mailValidation)){
			mail.setError("Incorrect Format");
			acepta = false;
		}
		if(!pass.getText().toString().equals(confPass.getText().toString())){
			confPass.setError("Mismatch");
			acepta = false;
		}
		return acepta;
	}
	
	private void register(String userName,String password,String mail){
		
		loading.show();
		ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setEmail(mail);
        user.setPassword(password);
        user.put("fbId", "");
        user.signUpInBackground(new SignUpCallback() {
        	public void done(ParseException e) {
        		if (e == null) {
        			
					getInstallation(false);
					
                } else {
                	loading.dismiss();
                	hideView();
                	if(e.getCode() == 202)
                		Toast.makeText(getApplicationContext(),"User already exists", Toast.LENGTH_LONG).show();
                }
        	}
        });
	}
	
	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					// Save the user profile info in a user property
					ParseUser currentUser = ParseUser.getCurrentUser();
					currentUser.setUsername(user.getName());
					currentUser.put("fbId", user.getId());
					currentUser.saveInBackground(new SaveCallback() {
								
						@Override
						public void done(ParseException arg0) {
							loading.dismiss();
							toMain();
						}
					});

				} else if (response.getError() != null) {
					loading.dismiss();
					if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
							|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
						Log.d("algo","The facebook session was invalidated.");
					} else {
						Log.d("Algo","Some other error: "+ response.getError().getErrorMessage());
					}
				}
			}
		});
		request.executeAsync();

	}
	
	private void connectFacebook( ){
		
		loading.show();
		
		List<String> permissions = Arrays.asList("basic_info", "user_about_me");
		ParseFacebookUtils.logIn(permissions,this, new LogInCallback() {
			 @Override
			 public void done(ParseUser user, ParseException err) {
				 if (user == null) {
					 loading.dismiss();
					 Log.i("MyApp", "Error");
				 } else if (user.isNew()) {
					 Log.i("MyApp", "New user");
					 getInstallation(true);
					 
				 } else {
					 Log.i("MyApp", "Log In");
					 getInstallation(false);
			 	}
			 }
		});
	}
	
	private void hideView(){
		if(loginLayout.isShown()){
			loginLayout.startAnimation(hide);
			imgBlur.startAnimation(fadeOut);
		}else if(registerLayout.isShown()){
			registerLayout.startAnimation(hide);
			imgBlur.startAnimation(fadeOut);
		}
	}
	
	private void getInstallation(final boolean facebook){
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		PushService.subscribe(activity, "Lesbico", MainActivity.class);
		PushService.subscribe(activity, "Gay", MainActivity.class);
		PushService.subscribe(activity, "Bisexual", MainActivity.class);
		PushService.subscribe(activity, "Transexual", MainActivity.class);
		installation.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException arg0) {
				if(facebook)
				makeMeRequest();
				else{
					loading.dismiss();
					toMain();
				}
			}
		});
	}
	
	private void toMain(){
		if(inside){
			finish();
		}else{
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
