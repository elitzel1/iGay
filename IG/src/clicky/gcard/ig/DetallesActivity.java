package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clicky.gcard.ig.adapters.ComentarioAdapter;
import clicky.gcard.ig.datos.Comentario;
import clicky.gcard.ig.datos.Lugares;
import clicky.gcard.ig.utils.ImageLoader;

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
import com.parse.GetDataCallback;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
        private String lugarId,nombre,descripcion,direccion,estado,imagen;
        private float calificacion;
        private double lat,longitud;

        private LinearLayout layout;
        private TextView txtNombre,txtDesc,txtDir,txtEdo;
        private RatingBar ratingLugar;
        private Button btnCom,btnMas;
       	private ImageView imgLugar;
        private GoogleMap mapa;

        private ImageLoader imgLoader;
        private ParseUser user;
        private Comentario userComment = null;
        private boolean isactive=false;
        private ComentarioAdapter adapter = null;
        private List<Comentario> lugaresList = null;
        private View footer;

        
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
                
                imgLoader = new ImageLoader(this);
                
                layout = (LinearLayout)findViewById(R.id.comentarios);
             //   txtNombre = (TextView)findViewById(R.id.txtNombre);
                imgLugar = (ImageView)findViewById(R.id.image_header);
                txtDesc = (TextView)findViewById(R.id.txtDesc);
                txtDir = (TextView)findViewById(R.id.txtDir);
                txtEdo = (TextView)findViewById(R.id.txtEdo);
                
                ratingLugar = (RatingBar)findViewById(R.id.rateLugar);
                
                btnCom = (Button)findViewById(R.id.btnComment);
                btnMas = (Button)findViewById(R.id.btnComentarios);
                btnMas.setVisibility(View.GONE);
               // listComments = (ListView)findViewById(R.id.listComments);
                /*
                txtNombre.setText(nombre);
                txtDesc.setText(descripcion);
                txtDir.setText(direccion);
                */
                txtDesc.setText("Capital de Inglaterra y Reino Unido, el más grande de los estados" +
                		" de ese país, por que YOLO");
                txtDir.setText("Calle #69, Colonia, Delegación");
                txtEdo.setText(estado);
                
                ratingLugar.setRating(calificacion);
                
                if(imagen != null){
                	imgLoader.DisplayImage(imagen, imgLugar);
                }
                
                footer = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
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

        public void setUpSomething(){
            Intent i = getIntent();
            Bundle b = i.getBundleExtra("datos");
            if(b!=null){
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
        //        bar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
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
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.detalles, menu);
                return true;
        }

        public boolean onOptionItemSelected (MenuItem item){
                switch(item.getItemId()){
                case android.R.id.home:
                
                       NavUtils.getParentActivityIntent(this);
                        
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }
}
