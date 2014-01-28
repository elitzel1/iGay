package clicky.gcard.ig.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Constants {
	public static Typeface font_lt;
	public static Typeface font_roman;
	public static Typeface font_ul;
	
	public void setFontLt(AssetManager assetm){
		font_lt = Typeface.createFromAsset(assetm, "HelveticaNeue_Lt");
	}
	
	public void setFontRoman(AssetManager asm){
		font_roman = Typeface.createFromAsset(asm, "HelveticaNeue_Roman");
	}
	
	public void setFontUl(AssetManager asm){
		font_ul = Typeface.createFromAsset(asm, "HelveticaNeue_Ul");
	}
	
	public Typeface getFtonLt(){
		return font_lt;
	}
	
	public Typeface getFontRoman(){
		return font_roman;
	}
	
	public Typeface getFontUl(){
		return font_ul;
	}
	
	

}
