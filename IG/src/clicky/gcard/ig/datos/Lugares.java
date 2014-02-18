package clicky.gcard.ig.datos;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Lugares")
public class Lugares extends ParseObject {
	
	private String lugarId;
	private String name;
	private String category;
	private String desc;
	private String edo;
	private String dir;
	private float calif;
	private LatLng geo;
	private String imagen;
	
	public Lugares(){}
	
	public float getCalif() {
		return calif;
	}
	public void setCalif(float calif) {
		this.calif = calif;
	}
	public LatLng getGeo() {
		return geo;
	}
	public void setGeo(LatLng geo) {
		this.geo = geo;
	}
	public String getLugarId() {
		return lugarId;
	}
	public void setLugarId(String lugarId) {
		this.lugarId = lugarId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getEdo() {
		return edo;
	}
	public void setEdo(String edo) {
		this.edo = edo;
	}
	
	public ParseFile getPic() {
        return getParseFile("imagen");
    }

	public String getImagen() {
		return imagen;
	}

	public void setImagen(ParseFile imagen) {
		this.imagen = imagen.getUrl();
	}

}
