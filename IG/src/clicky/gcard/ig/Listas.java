package clicky.gcard.ig;



import clicky.gcard.ig.adapters.SpinnerAdapterSpecial;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Listas extends ListFragment implements OnNavigationListener{
private String[] cat_Antro = {"Antro X","Antro Y","Antro Z","Kinky","7 vidas","La purisima"};
private String[] cat_Bar = {"La Facultad","Bar X","Salon Corona","Bar A","Bar D","Bive","Bar B"};
private String[] cat_Cultural={"CU","Teatro","Diversidad","Centro cultural","Parque Vivelo"};
private String[] cat_Res={"Mama Bella","Chillis","Pizzas L","Tortilleria"};
private String[] cat_Hotel = {"Hotel Genova","Hotel XXX","Hotel Rapid-Inn"};
private String[] cat_Playa = {"Playa G","Playa L","Playa T","Playa las 3T","Playa B","Playa I","Playa H"};

private int layout;
private Activity activity;

	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        // We need to use a different list item layout for devices older than Honeycomb
        layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        
        // Create an array adapter for the list view
     //   setListAdapter(new ArrayAdapter<String>(getActivity(), layout, cat_Antro));
       
        SpinnerAdapterSpecial s_adapter = new SpinnerAdapterSpecial(getActivity().getBaseContext(),getResources().getStringArray(R.array.categorias));
        setUpBar(s_adapter);
	}
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        this.activity=activity;
	 }
	 
	private void setUpBar(SpinnerAdapterSpecial adapter){
		int tipo=getArguments().getInt("tipo");
		ActionBar bar = ((ActionBarActivity)activity).getSupportActionBar();
		if(tipo==0){
			bar.setTitle("Populares");
		}else
			bar.setTitle("Categorías");
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		bar.setListNavigationCallbacks(adapter, this);
	}
	
	/**
	 * Antro 0
	 * Bar 1
	 * Cultural 2
	 * Restauran 3
	 * Hotel 4
	 * Playa 5
	 */
	

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		  switch (itemPosition) {
          case 0:
              Toast.makeText(getActivity().getBaseContext(), "Antro", Toast.LENGTH_SHORT).show();
              setListAdapter(new ArrayAdapter<String>(getActivity(), layout, cat_Antro));
              break;
          case 1:
              Toast.makeText(getActivity().getBaseContext(), "Bar", Toast.LENGTH_SHORT).show();
              setListAdapter(new ArrayAdapter<String>(getActivity(), layout, cat_Bar));
              break;
          case 2:
              Toast.makeText(getActivity().getBaseContext(), "Cultural", Toast.LENGTH_SHORT).show();
              setListAdapter(new ArrayAdapter<String>(getActivity(), layout, cat_Cultural));
              break;
          case 3:
              Toast.makeText(getActivity().getBaseContext(), "Restauran", Toast.LENGTH_SHORT).show();
              setListAdapter(new ArrayAdapter<String>(getActivity(), layout, cat_Res));
              break;
          case 4:
              Toast.makeText(getActivity().getBaseContext(), "Hotel", Toast.LENGTH_SHORT).show();
              setListAdapter(new ArrayAdapter<String>(getActivity(), layout, cat_Hotel));
              break;
          case 5:
              Toast.makeText(getActivity().getBaseContext(), "Playa", Toast.LENGTH_SHORT).show();
              setListAdapter(new ArrayAdapter<String>(getActivity(), layout, cat_Playa));
              break;
          default:
        	  break;
      }
      return false;
	}
}
