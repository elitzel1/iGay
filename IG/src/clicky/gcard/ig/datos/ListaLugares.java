package clicky.gcard.ig.datos;

import com.google.android.gms.maps.model.LatLng;

public class ListaLugares {
	
	
	public ListaLugares(){
	}
	
	public class item{
		
	String nombre;
	String direccion;
	String desc;
	String ima;
	LatLng geo;
	long calificacion;
	int id;
	
		public item(String nombre, String direccion, String desc, String ima, LatLng geo, long calificacion, int id){
			this.nombre=nombre;
			this.direccion = direccion;
			this.ima=ima;
			this.desc=desc;
			this.geo=geo;
			this.calificacion=calificacion;
			this.id=id;
		}
	}
	
	
	
	
}
