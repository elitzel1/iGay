package clicky.gcard.ig.utils;

import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utiliy {
	public static void setListViewHeightBasedOnChildren(ListView listView)
	{
	    ListAdapter listAdapter = listView.getAdapter();
	    if(listAdapter == null) return;
	    if(listAdapter.getCount() <= 1) return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0) {
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
	        }
	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	     
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	}
	
	/******CONEXIÓN**************/
	 public static boolean verificaConexion(Context ctx) {
	        boolean bConectado = false;
	        ConnectivityManager connec = (ConnectivityManager) ctx
	                .getSystemService(Context.CONNECTIVITY_SERVICE);
	        // No sólo wifi, también GPRS
	        NetworkInfo[] redes = connec.getAllNetworkInfo();
	        for (int i = 0; i < 2; i++) {
	            // ¿Tenemos conexión? ponemos a true
	            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
	                bConectado = true;
	            }
	        }
	        return bConectado;
	    }
	 /****************************/
	 
	 public static void CopyStream(InputStream is, OutputStream os)
	    {
	        final int buffer_size=1024;
	        try
	        {
	            byte[] bytes=new byte[buffer_size];
	            for(;;)
	            {
	              int count=is.read(bytes, 0, buffer_size);
	              if(count==-1)
	                  break;
	              os.write(bytes, 0, count);
	            }
	        }
	        catch(Exception ex){}
	    }
}
