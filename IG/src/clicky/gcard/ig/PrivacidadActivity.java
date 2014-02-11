package clicky.gcard.ig;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;


public class PrivacidadActivity extends ActionBarActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privacidad_layout);
		
		setUpBar();
	}
	
	public void setUpBar(){
		ActionBar bar = getSupportActionBar();
		bar.setTitle("Privacidad");
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

}
