package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.List;

import clicky.gcard.ig.adapters.ListaLugaresAdapter;
import clicky.gcard.ig.datos.Lugares;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

public class SearchResult extends ActionBarActivity implements OnItemClickListener {

	private View footer;
	private ListView lResult;
	private List<Lugares> lugaresList = null;
	private ListaLugaresAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
	//	footer = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_item, null, false);
		lResult = (ListView)findViewById(R.id.listSearch);
		lResult.setCacheColorHint(0);
		
        lugaresList = new ArrayList<Lugares>();
        
		// Show the Up button in the action bar.
		setupActionBar();
		handleIntent(getIntent());
	}

	public void onNewIntent(Intent i){
		setIntent(i);
		handleIntent(i);
	}
	
	private void handleIntent(Intent i){

	//	lResult.addFooterView(footer);
		lugaresList.clear();
		if(Intent.ACTION_SEARCH.equals(i.getAction())){
			String query = i.getStringExtra(SearchManager.QUERY);
			Log.i("SA", query);
			ParseQuery<ParseObject> queryP = new ParseQuery<ParseObject>("Lugares");
			queryP.whereContains("nombre", query);
			
			queryP.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> lugares, ParseException e) {
				//	lResult.removeFooterView(footer);
					for (ParseObject lugar : lugares){
						Lugares item = new Lugares();
						item.setLugarId((String) lugar.getObjectId());
						item.setName((String) lugar.get("nombre"));
						item.setCategory((String) lugar.get("categoria"));
						item.setCalif((float)lugar.getDouble("calificacion"));
						item.setDesc((String) lugar.get("descripcion"));
						item.setDir((String) lugar.get("direccion"));
						item.setGeo(new LatLng(lugar.getParseGeoPoint("localizacion").getLatitude(),
								lugar.getParseGeoPoint("localizacion").getLongitude()));
						lugaresList.add(item);
					}
					adapter = new ListaLugaresAdapter(getApplicationContext(), lugaresList);
		            // Binds the Adapter to the ListView
		            lResult.setAdapter(adapter);
					
				}
			});
		}
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		lugaresList.get(position);
	
	}

}
