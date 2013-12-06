package clicky.gcard.ig;

import clicky.gcard.ig.adapters.AdapterListaNotificaciones;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AjustesFragment extends ListFragment {

	String values[] = new String[]{"Configuración de Cuenta","Configuración de Notificaciones",
			"Redes sociales","Acerca de","Privacidad","Finalizar sesión"};
	
	private ListView list;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
		
		View rootView =  inflater.inflate(R.layout.frag_ajustes, container, false);
		list=(ListView)rootView.findViewById(android.R.id.list);
		return rootView;
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setListAdapter(new AdapterListaNotificaciones(getActivity(),values));
		
	}
	
	public void onListItemClick(ListView list,View view, int position, long id){
		super.onListItemClick(list, view, position, id);
		
	}
}
