package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clicky.gcard.ig.adapters.ComentarioAdapter;
import clicky.gcard.ig.datos.Comentario;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment{

//Datos del lugar
private String lugarId,nombre,descripcion,direccion;
private float calificacion;
private double lat,longitud;

private TextView txtNombre,txtDesc,txtDir;
private RatingBar ratingLugar;
private Button btnCom;
private ListView listComments;
private GoogleMap mapa;

private ParseUser user;
private Comentario userComment = null;
private boolean isactive=false;
private ComentarioAdapter adapter = null;
private List<Comentario> lugaresList = null;
private View footer;
private View view;
private Activity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   
	    lugarId = getArguments() != null ? getArguments().getString("lugarId") : null;
	    nombre = getArguments() != null ? getArguments().getString("nombre") : null;
	    descripcion = getArguments() != null ? getArguments().getString("descripcion") : null;
	    direccion = getArguments() != null ? getArguments().getString("direccion") : null;
	    calificacion = getArguments() != null ? getArguments().getFloat("calificacion") : null;
	    lat = getArguments() != null ? getArguments().getDouble("latitud") : null;
	    longitud = getArguments() != null ? getArguments().getDouble("longitud") : null;
	    
	    user = ParseUser.getCurrentUser();
	    if(user!=null)
	    	isactive=true;
	    
	    setHasOptionsMenu(true);
	    setUpBar();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
	
		if(view==null){
			try{
		view = inflater.inflate(R.layout.detail_layout,null,false);
			}catch(InflateException e){}
		}
		else{
			ViewGroup group = (ViewGroup)view.getParent();
			if(group!=null)
				group.removeView(view);
		}
		
		txtNombre = (TextView)view.findViewById(R.id.txtNombre);
		txtDesc = (TextView)view.findViewById(R.id.txtDesc);
		txtDir = (TextView)view.findViewById(R.id.txtDir);
		
		ratingLugar = (RatingBar)view.findViewById(R.id.rateLugar);
		
		btnCom = (Button)view.findViewById(R.id.btnComment);
		listComments = (ListView)view.findViewById(R.id.listComments);
		
		txtNombre.setText(nombre);
		txtDesc.setText(descripcion);
		txtDir.setText(direccion);
		
		ratingLugar.setRating(calificacion);
		
		footer = ((LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loading_item, null, false);
		
		
		lugaresList = new ArrayList<Comentario>();
		
		setUpMap();
		
		updatePostList();
		
		btnCom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(user != null)
					showCommentDialog();
				else
					Toast.makeText(activity, "You must sign in to comment", Toast.LENGTH_SHORT).show();
			}
		});
		
		return view;
	}
	
	public void setUpBar(){
		ActionBar bar = ((ActionBarActivity)activity).getSupportActionBar();
		bar.setTitle(nombre);
		bar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
		bar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void setUpMap(){

		mapa =((SupportMapFragment)getFragmentManager().findFragmentById(R.id.mapaLugar)).getMap();
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,longitud),15));
		mapa.getUiSettings().setAllGesturesEnabled(false);
		mapa.getUiSettings().setZoomControlsEnabled(false);

		mapa.addMarker(new MarkerOptions().position(new LatLng(lat,longitud)));
		
		mapa.setOnMapClickListener(new OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point){
            	String uri = "geo:"+lat+","+longitud+"?q="+lat+","+longitud+"&z=15";
            	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            	activity.startActivity(intent);
            }
        });
		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				String uri = "geo:"+lat+","+longitud+"?q="+lat+","+longitud+"&z=15";
            	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            	activity.startActivity(intent);
				return false;
			}
		});
		
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
	}
	
	private void showCommentDialog(){
		final Dialog dialog = new Dialog(activity,R.style.ThemeDialogCustom);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.coment_layout);
		
		final EditText editComent = (EditText) dialog.findViewById(R.id.editCommentario);
		final RatingBar calificacion = (RatingBar)dialog.findViewById(R.id.ratingBar1);
		Button btnAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
		
		if(userComment != null){
			editComent.setText(userComment.getComment());
			calificacion.setRating(userComment.getCalif());
		}
		
		btnAceptar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(userComment != null){
					updateComment(dialog,editComent.getText().toString(), calificacion.getRating());
				}else{
					setComment(dialog,editComent.getText().toString(), calificacion.getRating());
				}
			}
		});
		
		dialog.show();
	}
	
	private void setComment(final Dialog dialog,String text,float calif){
		ParseObject comment = new ParseObject("Comentarios");
		
		comment.put("usuario", ParseUser.createWithoutData(ParseUser.class, user.getObjectId()));
		comment.put("userName", user.getUsername());
		
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		defaultACL.setWriteAccess(user, true);
		
		comment.put("comentario", text);
		comment.put("calificacion", calif);
		comment.put("idLugar", ParseObject.createWithoutData("Lugares",lugarId));
		comment.setACL(defaultACL);
		
		comment.saveInBackground(new SaveCallback () {
			@Override
			public void done(ParseException arg0) {
				updateCalif(dialog);
			}
		});
	}
	
	private void updateComment(final Dialog dialog,final String text,final float calif){
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Comentarios");
        query.getInBackground(userComment.getCommentId(), new GetCallback<ParseObject>() {
        	public void done(final ParseObject object, ParseException e) {
        		if (e == null) {
	                object.put("comentario", text);
	                object.put("calificacion", calif);
	                object.saveInBackground(new SaveCallback() {
	                	public void done(ParseException e) {
	                		updateCalif(dialog);
	                	}
	                });
        		}else { 
        			e.printStackTrace();
        		}
        	}
        });
	}
	
	private void updatePostList() {
		listComments.addFooterView(footer);
		listComments.setAdapter(adapter);
		  // Create query for objects of type "Post"
		  ParseQuery<ParseObject> query = ParseQuery.getQuery("Comentarios");
		             
		  query.whereEqualTo("idLugar", ParseObject.createWithoutData("Lugares",lugarId));
		         
		  // Run the query  
		  query.findInBackground(new FindCallback<ParseObject>() {
		 
		    @Override
		    public void done(List<ParseObject> postList,
		        ParseException e) {
		      if (e == null) {
		    	  
		        // If there are results, update the list of posts
		        // and notify the adapter
		        lugaresList.clear();
		        for (ParseObject post : postList) {
		        	Comentario coment = new Comentario();
		        	coment.setCommentId(post.getObjectId());
		        	coment.setComment(post.getString("comentario"));
		        	coment.setUser(post.getString("userName"));
		        	coment.setCalif((float)post.getDouble("calificacion"));
		        	if(isactive){
			        	if(post.getString("userName").equals(user.getUsername())){
			        		userComment = coment;
			        	}
		        	}
		        	lugaresList.add(coment);
		        }
		        listComments.removeFooterView(footer);
	            // Pass the results into ListViewAdapter.java
	            adapter = new ComentarioAdapter(activity, lugaresList);
	            // Binds the Adapter to the ListView
	            listComments.setAdapter(adapter);
		      } else {
		        Log.d("Post retrieval", "Error: " + e.getMessage());
		      }
		    }
		                     
		  });
	}
	
	private void updateCalif(final Dialog dialog){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("movie", lugarId);
		ParseCloud.callFunctionInBackground("comentario", params , new FunctionCallback<Object>() {
			@Override  
			public void done(Object result, ParseException e) {
			    if (e == null) {
			      dialog.dismiss();
			      updatePostList();
			    }else{
			    	Log.i("Error", e.toString());
			    }
			  }
			});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Get item selected and deal with it
		switch (item.getItemId()) {
			case android.R.id.home:
				//called when the up affordance/carat in actionbar is pressed
				getActivity().onBackPressed();
				return true;
		}
		return true;
	
	}
	
	public void onStop(){
		super.onStop();
		Log.i("CV", "onstop");
	}
	
	public void onDestroy(){
		super.onDestroy();
		 Fragment f = (SupportMapFragment) getFragmentManager()
                 .findFragmentById(R.id.mapaLugar);
		if(f!=null){
			getFragmentManager().beginTransaction().remove(f).commit();
		}
	Log.i("CV", "onDestroy");
	}

}
