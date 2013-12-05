package clicky.gcard.ig;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.PushService;

import android.app.Application;

public class ParseApplication extends Application {
	 
	@Override
    public void onCreate() {
            super.onCreate();

            Parse.initialize(this, "e54qWo0yYhMhpGpmMFUcVrUtArRjBunDebYM6tgf","r2H8dYGS6bIgZErZgkyN8mL2WbP1Ul9vxfXEqdJy");

            // Set your Facebook App Id in strings.xml
            ParseFacebookUtils.initialize(getResources().getString(R.string.facebookId));
            
            PushService.setDefaultPushCallback(this, MainActivity.class);

    }
 
}