package clicky.gcard.ig;

import com.facebook.FacebookRequestError;
import clicky.gcard.ig.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RedesSocialesActivity extends ActionBarActivity implements OnClickListener{
	
	private Button btnFacebook;
	private ParseUser user;
	private ProgressDialog loading;
	private String name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.redes_layout);
		name = getIntent().getExtras().getString("name");
		setUpBar();
		
		user = ParseUser.getCurrentUser();
		
		btnFacebook = (Button)findViewById(R.id.btnFacebook);
		btnFacebook.setOnClickListener(this);
		
		if(ParseFacebookUtils.isLinked(user)){
			btnFacebook.setText(R.string.txt_linked);
		}
		
	}
	
	public void setUpBar(){
		ActionBar bar = getSupportActionBar();
		bar.setTitle(name);
		bar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.moradof)));
		bar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Get item selected and deal with it
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return true;
	
	}

	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							// Save the user profile info in a user property
							ParseUser currentUser = ParseUser
									.getCurrentUser();
							currentUser.setUsername(user.getName());
							currentUser.put("fbId", user.getId());
							currentUser.saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException arg0) {
									loading.dismiss();
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

	private void unlinkFacebook(){
		loading = new ProgressDialog(this);
		loading.setMessage("Wait a moment...");
		loading.setCancelable(false);
		loading.show();
		ParseFacebookUtils.unlinkInBackground(user, new SaveCallback() {
			@Override
			public void done(ParseException ex) {
				loading.dismiss();
				if (ex == null) {
					Log.d("MyApp", "The user is no longer associated with their Facebook account.");
					setResult(RESULT_OK);
					finish();
				}
			}
		});
	}
	
	private void showAlert(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage(R.string.alert_logout);
		alert.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				unlinkFacebook();
			}
		});
		alert.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.create().show();
	}
	
	@Override
	public void onClick(View v) {
		if (!ParseFacebookUtils.isLinked(user)) {
			loading = new ProgressDialog(this);
			loading.setMessage("Wait a moment...");
			loading.setCancelable(false);
			loading.show();
			ParseFacebookUtils.link(user, this, new SaveCallback() {
				@Override
				public void done(ParseException ex) {
					if (ParseFacebookUtils.isLinked(user)) {
						makeMeRequest();
						Log.d("MyApp", "Woohoo, user logged in with Facebook!");
					}
				}
			});
		}else{
			showAlert();
		}
	}
	
}
