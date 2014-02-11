package clicky.gcard.ig;

import clicky.gcard.ig.FragmentResult.OnSelectItem;
import clicky.gcard.ig.datos.Lugares;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

public class SearchResult extends ActionBarActivity implements OnSelectItem, SearchView.OnQueryTextListener,SearchView.OnCloseListener{

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
			
			if(isFragmentActive()){}
		
			FragmentResult frag = new FragmentResult();
			frag.setArguments(bundle);
			
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame2, frag).addToBackStack(null).commit();}

	}
	
	private boolean isFragmentActive(){
		
		Fragment fra = getSupportFragmentManager().findFragmentById(R.id.content_frame2);
		if(fra!=null)
		{
			getSupportFragmentManager().beginTransaction().remove(fra).commit();
			ViewGroup vg = (ViewGroup)findViewById(R.id.content_frame2);
			vg.removeAllViews();
			return true;
		}
		return false;
	}
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.moradof)));
		getSupportActionBar().setTitle(getResources().getString(R.string.search));
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_search:
			onSearchRequested(); 
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onArticleSelected(Lugares lugar) {
		
		Log.i("Clic","Search Result");
		Intent i = new Intent(SearchResult.this,DetallesActivity.class);
		Bundle args = new Bundle();
		args.putString("lugarId", lugar.getLugarId());
		args.putString("nombre", lugar.getName());
		args.putString("descripcion", lugar.getDesc());
		args.putString("direccion", lugar.getDir());
		args.putString("estado", lugar.getEdo());
		args.putFloat("calificacion", lugar.getCalif());
		args.putDouble("latitud", lugar.getGeo().latitude);
		args.putDouble("longitud", lugar.getGeo().longitude);
		i.putExtra("datos", args);
		
		startActivity(i);
		
	}
	
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_result, menu);
		
		//Verificar que sea version 3.0 o mayor
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	     
		SearchManager searchManager =
		           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		    SearchView searchView =
		            (SearchView) menu.findItem(R.id.action_search).getActionView();
		    
		    searchView.setSearchableInfo(
		            searchManager.getSearchableInfo(getComponentName()));
		    
		}
		return true;
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		Log.i("Clic", "SearchResult");
	}

	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}


}
