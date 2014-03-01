package clicky.gcard.ig;

import java.util.HashMap;
import java.util.List;

import clicky.gcard.ig.datos.Comentario;
import clicky.gcard.ig.utils.ImageLoader;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
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

import android.net.Uri;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetallesActivity extends ActionBarActivity {
        
        //Datos del lugar

        private String lugarId,nombre,descripcion,direccion,estado,categoria,imagen;

        private float calificacion;
        private double lat,longitud;
      
        private LinearLayout layout;
        private TextView txtDesc,txtDir,txtEdo,txtVacio;
        private RatingBar ratingLugar;
        private Button btnCom,btnMas;
       	private ImageView imgLugar;
        private GoogleMap mapa;
        private int FLAG_MENU=0;
        private ImageLoader imgLoader;
        private ParseUser user;
        private Comentario userComment = null;
        private boolean isactive=false;
        @SuppressWarnings("unused")
		private List<Comentario> lugaresList = null;
        private View footer;

        ShareActionProvider mShareActionProvider;
        private UiLifecycleHelper uiHelper;
        
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(new ColorDrawable(getResources().getColor(R.color.moradof)))
                .headerLayout(R.layout.header_fa)
                .contentLayout(R.layout.detail_layout);
                setContentView(helper.createView(this));
                helper.initActionBar(this);
                setUpSomething();

                uiHelper = new UiLifecycleHelper(this, callback);
                uiHelper.onCreate(savedInstanceState);

                imgLoader = new ImageLoader(this);
                
                layout = (LinearLayout)findViewById(R.id.comentarios);

                ImageView imga = (ImageView)findViewById(R.id.imgtipo);

                imgLugar = (ImageView)findViewById(R.id.image_header);

                txtDesc = (TextView)findViewById(R.id.txtDesc);
                txtDir = (TextView)findViewById(R.id.txtDir);
                txtEdo = (TextView)findViewById(R.id.txtEdo);
                txtVacio = (TextView)findViewById(R.id.vacio);
                
                ratingLugar = (RatingBar)findViewById(R.id.rateLugar);
                
                btnCom = (Button)findViewById(R.id.btnComment);
                btnMas = (Button)findViewById(R.id.btnComentarios);
                
                txtVacio.setVisibility(View.GONE);
                btnMas.setVisibility(View.GONE);

                

                imga.setImageResource(setupCategory(categoria));

                txtDesc.setText(descripcion);
                txtDir.setText(direccion);
          
                txtEdo.setText(estado);
                
                ratingLugar.setRating(calificacion);
                
                if(imagen != null){
                	imgLoader.DisplayImage(imagen, imgLugar);
                }
                
                footer = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                		.inflate(R.layout.loading_item, null, false);
                
                setUpMap();
              
                updatePostList();
                btnCom.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        	if(user != null)
                        		showCommentDialog();
                            else
                            	Toast.makeText(getBaseContext(), "You must sign in to comment", Toast.LENGTH_SHORT).show();
                        }
                });
                
                btnMas.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(DetallesActivity.this,CommentsActivity.class);
						i.putExtra("lugar", lugarId);
						startActivity(i);
						
					}
				});
                
                setUpBar();
        }
        
        private int setupCategory(String cat){
        	int id = 0;
        

    		if(cat.equals("Bares y Antros")){
    			id = R.drawable.nsantro;
        		}else{
        			if(cat.equals("Comida")){
        				id=R.drawable.nsrestaurante;
        			
        			}else{
        				if(cat.equals("Cafeteria")){
        					id=R.drawable.nscafe;
        			
        				}else{
        					if(cat.equals("Hotel")){
        						id=R.drawable.nshotel;
        	
        					}else{
        						if(cat.equals("Cultural")){
        							id=R.drawable.nscultural;
        			
        						}else{
        							if(cat.equals("Tienda")){
        								id=R.drawable.nstienda;
        			
        							}else{
        								if(cat.equals("Cuidado Personal")){
        									id=R.drawable.nscpersonal;
        		
        								}
        							}
        						}
        					}
        				}
        			}
        		}
	return id;
        }
        
        public void setUpSomething(){

                Intent i = getIntent();
                Bundle b = i.getBundleExtra("datos");
                if(b!=null){
                		categoria = b.getString("categoria");
                		Log.i("Cat", categoria);
                        lugarId = b.getString("lugarId");
                        nombre = b.getString("nombre");
                        descripcion = b.getString("descripcion");
                        direccion = b.getString("direccion");
                        estado = b.getString("estado");
                        calificacion = b.getFloat("calificacion");
                        lat = b.getDouble("latitud");
                        longitud = b.getDouble("longitud");
                        imagen = b.getString("imagen");
                }
        
 
            user = ParseUser.getCurrentUser();
            if(user!=null)
            	isactive=true;
        }
        
        public void setUpBar(){
                ActionBar bar = getSupportActionBar();
                bar.setTitle(nombre);
                bar.setDisplayHomeAsUpEnabled(true);
                
        }
        
        
        
        private void setUpMap(){

                mapa =((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapaLugar)).getMap();
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,longitud),15));
                mapa.getUiSettings().setAllGesturesEnabled(false);
                mapa.getUiSettings().setZoomControlsEnabled(false);

                mapa.addMarker(new MarkerOptions().position(new LatLng(lat,longitud)));
                
                mapa.setOnMapClickListener(new OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point){
                    String uri = "geo:"+lat+","+longitud+"?q="+lat+","+longitud+"&z=15";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
            }
        });
                mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                                String uri = "geo:"+lat+","+longitud+"?q="+lat+","+longitud+"&z=15";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                                return false;
                        }
                });
                
        }
        
        private void showCommentDialog(){
                final Dialog dialog = new Dialog(this,R.style.ThemeDialogCustom);
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
                                Button btn = (Button)v;
                                btn.setText("Guardando Comentario");
                                btn.setEnabled(false);
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

      
                ParseObject comment = ParseObject.create("Comentarios");

                comment.put("usuario", ParseUser.createWithoutData(ParseUser.class, user.getObjectId()));
                comment.put("userName", user.getUsername());
                if(user.get("fbId") != null)
                	comment.put("fbId", user.get("fbId"));
                
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
        	layout.addView(footer);
        	// Create query for objects of type "Post"
        	ParseQuery<ParseObject> query = ParseQuery.getQuery("Comentarios");
                         
        	query.whereEqualTo("idLugar", ParseObject.createWithoutData("Lugares",lugarId));
            query.setLimit(5);
        	// Run the query  
        	query.findInBackground(new FindCallback<ParseObject>() {
                 
        		@Override
        		public void done(List<ParseObject> postList, ParseException e) {
        			layout.removeView(footer);
        			if (e == null) {
                              
        				int pos = postList.size();
        				// If there are results, update the list of posts
                        // and notify the adapter
                        if(postList.size() == 5){
                        	pos = 4;
                        	btnMas.setVisibility(View.VISIBLE);
                        }
                        if(pos == 0)
                        	txtVacio.setVisibility(View.VISIBLE);
                        for (int i = 0; i < pos; i++) {
                        	ParseObject post = postList.get(i);
                        	Comentario coment = new Comentario();
                            coment.setCommentId(post.getObjectId());
                            coment.setComment(post.getString("comentario"));
                            coment.setUser(post.getString("userName"));
                            if(post.getString("fbId") != null)
                            	coment.setFbId(post.getString("fbId"));
                            coment.setCalif((float)post.getDouble("calificacion"));
                            if(isactive){
                            	if(post.getString("userName").equals(user.getUsername())){
                            		userComment = coment;
                                }
                            }
                            addCommentView(coment);
                        }
        			} else {
        				Log.d("Post retrieval", "Error: " + e.getMessage());
        			}
            	}
                                     
              });
        }

        
        private void updateCalif(final Dialog dialog){
	        HashMap<String, Object> params = new HashMap<String, Object>();
	        params.put("lugar", lugarId);
	        ParseCloud.callFunctionInBackground("comentario", params , new FunctionCallback<Object>() {
	        	@Override  
	            public void done(Object result, ParseException e) {
	        		if (e == null) {
	        			dialog.dismiss();
	                    ratingLugar.setRating(Float.valueOf(result.toString()));
	                    //updatePostList();
	                }else{
	                	Log.i("Error", e.toString());
	                }
	        	}
	    	});
        }
        
        private void addCommentView(Comentario comentario){
        	LayoutInflater inflater =
        		    (LayoutInflater)this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        	View view = inflater.inflate( R.layout.comentario_item, null );
        	
        	ProfilePictureView userProfilePicture = (ProfilePictureView) view.findViewById(R.id.userProfilePicture);
            TextView user = (TextView) view.findViewById(R.id.txtUser);
            TextView comment = (TextView) view.findViewById(R.id.txtComment);
            RatingBar calificacion = (RatingBar) view.findViewById(R.id.rateComentario);
            
            userProfilePicture.setProfileId(comentario.getFbId());
            user.setText(comentario.getUser());
            comment.setText(comentario.getComment());
            calificacion.setRating(comentario.getCalif());
            
            layout.addView(view);
        }
        
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        	 // Inflate the menu items for use in the action bar
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.detalles, menu);
            
            if(FLAG_MENU==1){
            	MenuItem item = menu.findItem(R.id.menu_item_share);
            	item.setVisible(false);
            }
            return super.onCreateOptionsMenu(menu); 
        }
        
//        
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            // Handle presses on the action bar items
//            switch (item.getItemId()) {
//            case android.R.id.home:
//            	Intent intent = NavUtils.getParentActivityIntent(this); 
//            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 
//            	NavUtils.navigateUpTo(this, intent);
//                 return true;
//         case R.id.menu_item_share:
//         		showShareDialog();
//                return true;
//
//            return super.onCreateOptionsMenu(menu); 
//        }
        
        
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle presses on the action bar items
            switch (item.getItemId()) {
            	case android.R.id.home:
            		Intent intent = NavUtils.getParentActivityIntent(this); 
            		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 
            		NavUtils.navigateUpTo(this, intent);
            		// NavUtils.getParentActivityIntent(this);
            		return true;
            	case R.id.menu_item_share:
            		Log.i("Share", "Menu");
            		//publishFeedDialog();
            		showShareDialog();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        
        private void showShareDialog(){

        	Log.i("Share", "showShareDialog");
        	if (FacebookDialog.canPresentShareDialog(getApplicationContext(), 
                    FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {

        	FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this).setName(nombre).setCaption(categoria)

        			.setDescription(descripcion).setPicture(imagen)
            .setLink("https://maps.google.com/?q="+lat+"+"+longitud)
            .build();
        	uiHelper.trackPendingDialogCall(shareDialog.present());
        	}
        	else{
        		FLAG_MENU = 1;
        		invalidateOptionsMenu();
        	}
        	
        	
        }        
             
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
                @Override
                public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                    Log.e("Activity", String.format("Error: %s", error.toString()));
                }

                @Override
                public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                    Log.i("Activity", "Success!");
                }
            });
        }
        
        private Session.StatusCallback callback = new Session.StatusCallback() {


            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                // TODO Auto-generated method stub

            }
        };
        
        @Override
        protected void onResume() {
            super.onResume();
            uiHelper.onResume();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            uiHelper.onSaveInstanceState(outState);
        }

        @Override
        public void onPause() {
            super.onPause();
            uiHelper.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            uiHelper.onDestroy();
        }
}
