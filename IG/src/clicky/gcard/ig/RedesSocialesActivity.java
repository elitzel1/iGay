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
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RedesSocialesActivity extends ActionBarActivity implements OnCheckedChangeListener{
	
	private CheckBox checkLinked,checkPost;
	private ParseUser user;
	private ProgressDialog loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.redes_layout);
		
		setUpBar();
		
		user = ParseUser.getCurrentUser();
		
		checkLinked = (CheckBox)findViewById(R.id.checkLinked);
		checkPost = (CheckBox)findViewById(R.id.checkComment);
		
		if(ParseFacebookUtils.isLinked(user)){
			checkLinked.setChecked(true);
			checkPost.setOnCheckedChangeListener(this);
		}
		
		checkPost.setChecked(getPreference());
		
		checkLinked.setOnCheckedChangeListener(this);
		
	}
	
	public void setUpBar(){
		ActionBar bar = getSupportActionBar();
		bar.setTitle("RedesSociales");
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView == checkLinked){
			if(isChecked){
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
				}
			}else{
				loading = new ProgressDialog(this);
				loading.setMessage("Wait a moment...");
				loading.setCancelable(false);
				loading.show();
				ParseFacebookUtils.unlinkInBackground(user, new SaveCallback() {
					@Override
					public void done(ParseException ex) {
						loading.dismiss();
						savePreferences("comment", false);
						if (ex == null) {
							Log.d("MyApp", "The user is no longer associated with their Facebook account.");
							finish();
						}
					}
				});
			}
		}else if(buttonView == checkPost){
			savePreferences("comment", isChecked);
		}
		
	}
	
	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	private boolean getPreference(){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPreferences.getBoolean("comment", false);
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
	
}
