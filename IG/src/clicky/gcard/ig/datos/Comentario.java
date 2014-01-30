package clicky.gcard.ig.datos;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Comentarios")
public class Comentario extends ParseObject{
	
	private String commentId;
	private String comment;
	private String fbId = null;
	private String user;
	private float calif;
	public String getComment() {
		return comment;
	}
	
	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public float getCalif() {
		return calif;
	}
	public void setCalif(float calif) {
		this.calif = calif;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		if(fbId.equals(""))
			this.fbId = null;
		else
			this.fbId = fbId;
	}
	
	public String getComentario(){
		return getString("comentario");
	}
	public String getFacebook(){
		ParseUser user = getParseUser("usuario");
		return user.getString("fbId");
	}
	public String getUserName() {
		return getParseUser("usuario").getUsername();
	}
	public float getRate(){
		return (float)getDouble("calificacion");
	}
	public static ParseQuery<Comentario> getQuery() {
		return ParseQuery.getQuery(Comentario.class);
	}
	

}