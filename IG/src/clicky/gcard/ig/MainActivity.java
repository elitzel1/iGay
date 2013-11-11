package clicky.gcard.ig;


import clicky.gcard.ig.adapters.GPSTrakcer;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnItemClickListener {
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
		
		

		if (savedInstanceState != null) {
			return;
		}
		
		MapFragmetn mapFragment = new MapFragmetn();
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, mapFragment);
		//trans.addToBackStack(null);
		trans.commit();
		
		

	}

	private void setUpActionBar() {
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("J");
		actionBar.show();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.morado)));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
	}

	@SuppressLint("NewApi")
	private void setUpDrawer() {
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		toggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer,
				R.string.drawer_open, R.string.drawer_close);
		options = getResources().getStringArray(R.array.drawable);
		drawerMenu = (ListView) findViewById(R.id.left_drawer);
		drawerMenu.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), R.layout.item_drawable, options));
		drawerMenu.setBackgroundColor(getResources().getColor(R.color.gris));
		// Set actionBarDrawerToggle as the DrawerListener
		drawer.setDrawerListener(toggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		// just styling option add shadow the right edge of the drawer
		drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		FragmentManager manager = getSupportFragmentManager();
		Bundle b=new Bundle();
		switch (position){
		case 0:
			manager.popBackStack();
			setUpActionBar();
			Toast.makeText(getApplicationContext(),"Home", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			Listas fragmentPop = new Listas();
			b.putInt("tipo", 0);
			fragmentPop.setArguments(b);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentPop).commit();
			Toast.makeText(getApplicationContext(),"Populares", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Listas fragmentCat = new Listas();
			b.putInt("tipo", 1);
			fragmentCat.setArguments(b);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentCat).commit();
			Toast.makeText(getApplicationContext(),"Categorías", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Toast.makeText(getApplicationContext(),"Ajustes", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			Toast.makeText(getApplicationContext(),"Notificaciones", Toast.LENGTH_SHORT).show();
			break;
		default:
		Toast.makeText(getApplicationContext(),"default", Toast.LENGTH_SHORT).show();
		break;
		}
		
		drawer.closeDrawer(drawerMenu);
	}
	
	  public void onDrawerClosed(View arg0) {
		  ActionBar actionBar = getSupportActionBar();
	        actionBar.setTitle("EJ ActionBar");
	    }
	 
	    public void onDrawerOpened(View arg0) {
	    	ActionBar actionBar = getSupportActionBar();
	        actionBar.setTitle("Menu Principal");
	        drawer.bringToFront();
	        drawer.requestLayout();
	    }
//https://github.com/elitzel1/iGay.git
	    public void onDrawerSlide(View drawerView, float slideOffset) {
	    	
	    	//super.onDrawerSlide(drawerView, slideOffset);
	        drawer.bringChildToFront(drawerView);
	        drawer.requestLayout();
	    }

	    public void onDrawerStateChanged(int arg0) {
	    	
	    	drawer.bringToFront();
	        drawer.requestLayout();
	    }

}
