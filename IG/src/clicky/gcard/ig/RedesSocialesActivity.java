package clicky.gcard.ig;

import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
		
		if(ParseFacebookUtils.isLinked(user)){
			checkLinked.setChecked(true);
		}
		
		checkLinked.setOnCheckedChangeListener(this);
	}
	
	public void setUpBar(){
		ActionBar bar = getSupportActionBar();
		bar.setTitle("RedesSociales");
		bar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.morado)));
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
							loading.dismiss();
							if (ParseFacebookUtils.isLinked(user)) {
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
						if (ex == null) {
							Log.d("MyApp", "The user is no longer associated with their Facebook account.");
							finish();
						}
					}
				});
			}
		}else if(buttonView == checkPost){
			
		}
		
	}
	
}
