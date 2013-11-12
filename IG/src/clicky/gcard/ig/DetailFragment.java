package clicky.gcard.ig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clicky.gcard.ig.adapters.ComentarioAdapter;
import clicky.gcard.ig.datos.Comentario;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

public class DetailFragment extends Fragment{
	
private String lugarId;
private Button btnCom;
private ListView listComments;
private ParseUser user;

private ComentarioAdapter adapter = null;
private List<Comentario> lugaresList = null;
private View footer;

private Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    lugarId = getArguments() != null ? getArguments().getString("lugarId") : null;
	    user = ParseUser.getCurrentUser();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
	
		View view = inflater.inflate(R.layout.detail_layout,null,false);
		
		btnCom = (Button)view.findViewById(R.id.btnComment);
		listComments = (ListView)view.findViewById(R.id.listComments);
		
		footer = ((LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loading_item, null, false);
		
		lugaresList = new ArrayList<Comentario>();
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
		
		btnAceptar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setComment(dialog,editComent.getText().toString(), calificacion.getRating());
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
		        	coment.setComment(post.getString("comentario"));
		        	coment.setUser(post.getString("userName"));
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
			    }else{
			    	Log.i("Error", e.toString());
			    }
			  }
			});
	}

}
