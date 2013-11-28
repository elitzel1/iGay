package clicky.gcard.ig;


import clicky.gcard.ig.MapFragment.OnButtonListener;
import clicky.gcard.ig.adapters.GPSTrakcer;
import clicky.gcard.ig.datos.Lugares;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnItemClickListener,Listas.OnListListener,OnButtonListener {
	//private GoogleMap map;
	LocationManager locationManager;
	private String[] options;
	private ListView drawerMenu;
	private ActionBarDrawerToggle toggle;
	private DrawerLayout drawer;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicio);

		setUpActionBar();
		/*** Drawer **/
		setUpDrawer();
		
		GPSTrakcer gps = new GPSTrakcer(this);
		if(!gps.canGetLocation())
			gps.showAlert();

		if (savedInstanceState != null) 
			return;
		
		MapFragment mapFragment = new MapFragment();
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, mapFragment);
		trans.commit();
		
		

	}

	/**Action Bar**/
	private void setUpActionBar(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("J");
		actionBar.show();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.morado)));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
	}

	/**Navigation Drawer**/
	@SuppressLint("NewApi")
	private void setUpDrawer() {
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		toggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer,
				R.string.drawer_open, R.string.drawer_close){
			  public void onDrawerClosed(View arg0) {
				  ActionBar actionBar = getSupportActionBar();
				  if(drawerMenu.getCheckedItemPosition()>=0)
			        actionBar.setTitle(options[drawerMenu.getCheckedItemPosition()]);
			    }
			    public void onDrawerOpened(View arg0) {
		    	ActionBar actionBar = getSupportActionBar();
			        actionBar.setTitle("Menú");
			        drawer.bringToFront();
			        drawer.requestFocus();
			    }
		
		};
		
		options = getResources().getStringArray(R.array.drawable);
		drawerMenu = (ListView) findViewById(R.id.left_drawer);
		drawerMenu.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), R.layout.item_drawable, options));
		drawerMenu.setBackgroundColor(getResources().getColor(R.color.gris));
	
		// Set actionBarDrawerToggle as the DrawerListener
		drawer.setDrawerListener(toggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		drawerMenu.setOnItemClickListener(this);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		toggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		toggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns
		// true
		// then it has handled the app icon touch event
		if (toggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
        case R.id.action_search:
            onSearchRequested(); // Para versiones menores a 3.0
            return true;
        default:
            return false;
    }
		//return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

//		if(position == drawerMenu.getCheckedItemPosition())
//			return;
		Bundle extras = null;
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		Fragment fragment = null;
		switch (position){
		case 0:
			manager.popBackStack();
			setUpActionBar();
			break;
		case 1:
			extras = new Bundle();
			fragment = new Listas();
			extras.putInt("tipo", 0);
			fragment.setArguments(extras);
			break;
		case 2:
			extras = new Bundle();
			fragment = new Listas();
			extras.putInt("tipo", 1);
			fragment.setArguments(extras);
			break;
		case 3:
			fragment = new AjustesFragment();
			break;
		case 4:
			fragment = new NotificacionesFragment();
			break;
		default:
		Toast.makeText(getApplicationContext(),"default", Toast.LENGTH_SHORT).show();
		break;
		}
		
		if(position!=0){
			if(manager.getBackStackEntryCount()>0)
				manager.popBackStack();
			trans.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
			getSupportActionBar().setTitle(options[position]);
		}
		drawerMenu.setItemChecked(position, true);
		drawer.closeDrawer(drawerMenu);
	}
	
	/**Interfaz para Lista y Más Info**/
	public void onArticleSelected(Lugares lugar){
		
		DetailFragment detail =  (DetailFragment)getSupportFragmentManager().findFragmentById(R.id.info_det);
		
		//Pantalla horizontal o tablet
		if(detail!=null){}
		else{
			DetailFragment fragment = new DetailFragment();
			Bundle args = new Bundle();
			args.putString("lugarId", lugar.getLugarId());
			args.putString("nombre", lugar.getName());
			args.putString("descripcion", lugar.getDesc());
			args.putString("direccion", lugar.getDir());
			args.putFloat("calificacion", lugar.getCalif());
			args.putDouble("latitud", lugar.getGeo().latitude);
			args.putDouble("longitud", lugar.getGeo().longitude);
			fragment.setArguments(args);
			toggle.setDrawerIndicatorEnabled(false);
			
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
			
		}
		
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		toggle.setDrawerIndicatorEnabled(true);
		if(drawerMenu.getCheckedItemPosition()>=0)
		getSupportActionBar().setTitle(options[drawerMenu.getCheckedItemPosition()]);
	}




}
