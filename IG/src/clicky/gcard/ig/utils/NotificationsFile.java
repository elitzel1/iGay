package clicky.gcard.ig.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.ContextWrapper;

public class NotificationsFile {
	
	
	public static void saveNotification(Context context,String fileName, String jsonString) {
		ContextWrapper cw = new ContextWrapper(context);
		FileOutputStream outputStream;
		File directory = cw.getDir("notifications", Context.MODE_PRIVATE);
		File file = new File(directory,fileName+".json");
		try {
		    outputStream = new FileOutputStream(file);
		    outputStream.write(jsonString.getBytes());
		    outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	public static JSONArray getNotifications(Context context,String fileName){
		String json;
		ContextWrapper cw = new ContextWrapper(context);
		File directory = cw.getDir("notifications", Context.MODE_PRIVATE);
		try {
	        File file=new File(directory, fileName+".json");
	        json = convertStreamToString(new FileInputStream(file));
	    } 
	    catch (FileNotFoundException e) 
	    {
	    	json = "";
	        e.printStackTrace();
	    } catch (Exception e) {
	    	json = null;
			e.printStackTrace();
		}

		JSONArray array;
		try {
			array = new JSONArray(json);
		} catch (JSONException e) {
			array = new JSONArray();
			e.printStackTrace();
		}
		return array;
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    return sb.toString();
	}

}
