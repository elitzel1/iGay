package clicky.gcard.ig.datos;

import com.google.android.gms.maps.model.LatLng;

public class Lugares {
	
	private String lugarId;
	private String name;
	private String category;
	private String desc;
	private String dir;
	private float calif;
	private LatLng geo;
	
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

}
