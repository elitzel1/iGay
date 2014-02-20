package clicky.gcard.ig.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class NotificationsReceiver extends BroadcastReceiver {
	
	private static final String TAG = "MyCustomReceiver";
 
  @Override
  public void onReceive(Context context, Intent intent) {
    try {
      String action = intent.getAction();
      String channel = intent.getExtras().getString("com.parse.Channel");
      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
      JSONArray array = NotificationsFile.getNotifications(context, "notificaciones");
      JSONObject notification = new JSONObject();

      Calendar calendar = Calendar.getInstance();
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      String fecha = format.format(calendar.getTime());
      
      notification.put("titulo", json.getString("alert"));
      notification.put("descripcion", json.getString("desc"));
      notification.put("link", json.getString("link"));
      notification.put("fecha", fecha);

      array.put(notification);
      NotificationsFile.saveNotification(context, "notificaciones", array.toString());
 
      Log.d(TAG, "got action " + action + " on channel " + channel + " with:");

    } catch (JSONException e) {
    	Log.d(TAG, "JSONException: " + e.getMessage());
    }
  }
}