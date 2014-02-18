package clicky.gcard.ig;


import java.io.Serializable;
import java.util.ArrayList;

import com.parse.ParseFile;
import com.parse.ParseUser;

import clicky.gcard.ig.AjustesFragment.onListItemClicConf;
import clicky.gcard.ig.DialogAjustesNot.AjustesNotListener;
import clicky.gcard.ig.DialogFiltro.NoticeDialogInterface;
import clicky.gcard.ig.MapFragment.OnButtonListener;
import clicky.gcard.ig.adapters.AdapterDrawer;
import clicky.gcard.ig.datos.Lugares;
import clicky.gcard.ig.utils.GPSTrakcer;
import clicky.gcard.ig.utils.Utiliy;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnItemClickListener,Listas.OnListListener,OnButtonListener,NoticeDialogInterface,
onListItemClicConf,AjustesNotListener{
	//private GoogleMap map;
	LocationManager locationManager;
	private String[] options;
	private ListView drawerMenu;
	private ActionBarDrawerToggle toggle;
	private DrawerLayout drawer;
	private MapFragment mapFragment;
	private int FRAGMENT_ID=0;
	Integer[] imageId ={R.drawable.ihome,R.drawable.ipopulares,R.drawable.icategoria,
			R.drawable.inotificacion,R.drawable.iajustes};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicio);

		setUpActionBar();

		/*** Drawer **/
		setUpDrawer();
		
		if(!Utiliy.verificaConexion(this))
			Toast.makeText(this, "No hay conexión de internet", Toast.LENGTH_LONG).show();
		
		GPSTrakcer gps = new GPSTrakcer(this);
		if(!gps.isGPSEnable())
			gps.showAlert();

		if (savedInstanceState != null) 
			return;
		
		mapFragment = new MapFragment();
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, mapFragment);
		trans.commit();
	}

	/**Action Bar**/
	private void setUpActionBar(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("J");
		actionBar.show();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.moradof)));
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
				  if(drawerMenu.getCheckedItemPosition()>0)
			        actionBar.setTitle(options[drawerMenu.getCheckedItemPosition()]);
				  else
					  actionBar.setTitle("ota");
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
	
		drawerMenu.setAdapter(new AdapterDrawer(getSupportActionBar()
				.getThemedContext(), R.layout.item_drawable, options,imageId));
		drawerMenu.setBackgroundResource(R.drawable.fondomenu);
	
				//http://www.androidbegin.com/tutorial/implementing-actionbarsherlock-side-menu-navigation-drawer-in-android/
		
		
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
            
        case R.id.action_filtrar:
        	onFilter();
        default:
            return false;
    }
		
	}

	public void onStart(){
		super.onStart();
		isEnableToggle();
	}
	
	public void onFilter(){
		DialogFiltro diag = new DialogFiltro();
		diag.show(getSupportFragmentManager(), "DFrag");
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
		
		if(FRAGMENT_ID==1){
			MenuItem item = menu.findItem(R.id.action_filtrar);
			item.setEnabled(false);
			item.setVisible(false);
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
			FRAGMENT_ID=0;
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
			
		if(ParseUser.getCurrentUser() != null){
			fragment = new NotificacionesFragment();
		}else{
			showAlert();
		}
			
			break;
		case 4:
			
			fragment = new AjustesFragment();
			break;
		default:
			
		Toast.makeText(getApplicationContext(),"default", Toast.LENGTH_SHORT).show();
		break;
		}
		
		if(position!=0){
			if(manager.getBackStackEntryCount()>0)
				manager.popBackStack();
			try{
			trans.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
			getSupportActionBar().setTitle(options[position]);
			FRAGMENT_ID=1; //ID para modificar el menú
			}catch(NullPointerException e){}
			
		}
		invalidateOptionsMenu(); //Obliga a reconstruir el menú dependiendo del fragment.
		drawerMenu.setItemChecked(position, true);
		drawer.closeDrawer(drawerMenu);
	}
	
	private void showAlert(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage(R.string.alert_no_login);
		alert.setPositiveButton(R.string.btn_ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent i = new Intent(MainActivity.this,LoginActivity.class);
				i.putExtra("inside", true);
				startActivity(i);
			}
		});
		alert.setNegativeButton(R.string.btn_cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.create().show();
	}
	
	/**Interfaz para Lista y Más Info**/
	public void onArticleSelected(Lugares lugar){
		
		Intent i = new Intent(MainActivity.this,DetallesActivity.class);
		Bundle args = new Bundle();
		args.putString("categoria",lugar.getCategory());
		args.putString("lugarId", lugar.getLugarId());
		args.putString("nombre", lugar.getName());
		args.putString("descripcion", lugar.getDesc());
		args.putString("direccion", lugar.getDir());
		args.putString("estado", lugar.getEdo());
		args.putFloat("calificacion", lugar.getCalif());
		args.putDouble("latitud", lugar.getGeo().latitude);
		args.putDouble("longitud", lugar.getGeo().longitude);
		args.putString("imagen", lugar.getImagen());
		i.putExtra("datos", args);
		
		startActivity(i);
		
			toggle.setDrawerIndicatorEnabled(false);
			
		
	}
	
	public void isEnableToggle(){
		if(!toggle.isDrawerIndicatorEnabled())
			enableToggle();
	}
	
	
	public void enableToggle(){
		
		toggle.setDrawerIndicatorEnabled(true);
		if(drawerMenu.getCheckedItemPosition()>=0)
		getSupportActionBar().setTitle(options[drawerMenu.getCheckedItemPosition()]);
		else
			getSupportActionBar().setTitle("J");
	}
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		enableToggle();
	}

	/****Métodos de DialogFragment****/
	@Override
	public void onDialogPositiveClic(boolean[] arr) {
		ArrayList<String> num = new ArrayList<String>();
		String[] lugares = getResources().getStringArray(R.array.categorias);
		
		for(int i=0;i<arr.length;i++){
			if(arr[i]==true){
				num.add(lugares[i]);
				Log.i("Filtro", "lugar: "+lugares[i]);
			} 
	
		}
		mapFragment.filter(num);
		mapFragment.hideDetail();
	}

	@Override
	public void onDialogNegativeClic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPositiveClic(boolean[] tipos,DialogAjustesNot fragment) {
		ArrayList<String> num = new ArrayList<String>();
		String[] lugares = getResources().getStringArray(R.array.tipos);
		
		for(int i=0;i<tipos.length;i++){
			if(tipos[i]==true){
				num.add(lugares[i]);
			}
		}
		fragment.changeChannels(num);
	}

	@Override
	public void onDialogNot() {
		DialogAjustesNot diag = new DialogAjustesNot();
		diag.show(getSupportFragmentManager(), "diagNoti");
	}

	/************/
	

}
