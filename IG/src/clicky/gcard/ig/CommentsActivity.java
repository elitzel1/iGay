package clicky.gcard.ig;

import java.util.List;

import clicky.gcard.ig.datos.Comentario;

import com.facebook.widget.ProfilePictureView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentsActivity extends ActionBarActivity{
	
	private LinearLayout layout;
	private ListView lista;
	private View footer;
	
	private ParseQueryAdapter<Comentario> adapter;
	
	private String lugarId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comentarios_list_layout);
		
		setUpBar();
		
		layout = (LinearLayout)findViewById(R.id.layout);
		lista = (ListView)findViewById(R.id.lista);
		footer = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
	               .inflate(R.layout.loading_item, null, false);
		
		lugarId = getIntent().getExtras().getString("lugar");
		
		layout.addView(footer);
		
		getComments();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Get item selected and deal with it
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return true;
	
	}
	
	public void setUpBar(){
		ActionBar bar = getSupportActionBar();
		bar.setTitle("Comentarios");
		bar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.moradof)));
		bar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void getComments(){
		ParseQueryAdapter.QueryFactory<Comentario> factory = new ParseQueryAdapter.QueryFactory<Comentario>() {
			public ParseQuery<Comentario> create() {
				ParseQuery<Comentario> query = Comentario.getQuery();
				query.include("usuario");
				query.whereEqualTo("idLugar", ParseObject.createWithoutData("Lugares",lugarId));
				query.orderByDescending("updatedAt");
				return query;
			}
		};
			 
		// Pass the factory into the ParseQueryAdapter's constructor.
		adapter = new ParseQueryAdapter<Comentario>(this, factory){
			@Override
			public View getItemView(Comentario comment, View view, ViewGroup parent) {
				if (view == null) {
					view = View.inflate(getContext(), R.layout.comentario_item, null);
		        }
		        ProfilePictureView pic = (ProfilePictureView)view.findViewById(R.id.userProfilePicture);
		        TextView user = (TextView)view.findViewById(R.id.txtUser);
		        TextView comentario = (TextView)view.findViewById(R.id.txtComment);
		        RatingBar rate = (RatingBar)view.findViewById(R.id.rateComentario);
		        
		        pic.setProfileId(comment.getFacebook());
		        user.setText(comment.getUserName());
		        comentario.setText(comment.getComentario());
		        rate.setRating(comment.getRate());
		        return view;
			}
	    };
		
	    // Disable pagination, we'll manage the query limit ourselves
	    adapter.setPaginationEnabled(true);
	    adapter.setObjectsPerPage(10);
		
	    
		// Perhaps set a callback to be fired upon successful loading of a new set of ParseObjects.
		adapter.addOnQueryLoadListener(new OnQueryLoadListener<Comentario>() {
			@Override
			public void onLoading() {
				// Trigger any "loading" UI
			}
			 
			@Override
			public void onLoaded(List<Comentario> arg0, Exception arg1) {
				// TODO Auto-generated method stub
				layout.removeView(footer);
			}
		});
		lista.setAdapter(adapter);
	}

}
