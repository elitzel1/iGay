package clicky.gcard.ig;


import clicky.gcard.ig.FragmentResult.OnSelectItem;
import clicky.gcard.ig.datos.Lugares;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

public class SearchResult extends ActionBarActivity implements OnSelectItem{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
	        
		// Show the Up button in the action bar.
		setupActionBar();
		handleIntent(getIntent());
	}

	public void onNewIntent(Intent i){
		setIntent(i);
		handleIntent(i);
	}
	
	private void handleIntent(Intent i){

		
		if(Intent.ACTION_SEARCH.equals(i.getAction())){
			String query = i.getStringExtra(SearchManager.QUERY);
			
			Bundle bundle = new Bundle();
			bundle.putString("query", query);
			
			FragmentResult frag = new FragmentResult();
			frag.setArguments(bundle);
			
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame2, frag).addToBackStack(null).commit();}

	}
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.morado)));
		getSupportActionBar().setTitle(getResources().getString(R.string.search));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_result, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onArticleSelected(Lugares lugar) {
		
		Log.i("Clic","Search Result");
		DetailFragment detail = (DetailFragment)getSupportFragmentManager().findFragmentById(R.id.info_det);
		if(detail!=null){
			
		}
		else
		{
			DetailFragment frag = new DetailFragment();
			Bundle args = new Bundle();
			args.putString("lugarId", lugar.getLugarId());
			args.putString("nombre", lugar.getName());
			args.putString("descripcion", lugar.getDesc());
			args.putString("direccion", lugar.getDir());
			args.putFloat("calificacion", lugar.getCalif());
			args.putDouble("latitud", lugar.getGeo().latitude);
			args.putDouble("longitud", lugar.getGeo().longitude);
			frag.setArguments(args);
			
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame2, frag).addToBackStack(null).commit();
		}
		
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		Log.i("Clic", "SearchResult");
	}


}
