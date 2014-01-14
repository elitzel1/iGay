package clicky.gcard.ig.utils;

import android.support.v7.app.ActionBar;
import android.util.TypedValue;
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
	    
	    /*
	    TypedValue tv = new TypedValue();
	    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
	       if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
	         actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
	    }else if(getTheme().resolveAttribute(com.actionbarsherlock.R.attr.actionBarSize, tv, true){
	         actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
	    }
	    */
	    
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	}
}
