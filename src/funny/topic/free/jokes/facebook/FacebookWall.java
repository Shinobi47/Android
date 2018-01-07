package funny.topic.free.jokes.facebook;

import java.net.URLEncoder;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import funny.topic.free.jokes.utils.WriteLog;


/**
*
* Class facebook API;post something on wall.(A)
* @author ThangTB (B)
* @since 2012-03-14 (C)
* @version 1.0.0 (D)
*
*/

public class FacebookWall {
//	private static final String KEY = "facebook-session";
//	private static final String TOKEN = "access_token";
//	private static final String UID = "user_id";
	
	private Context mContext;
	private String mAccessToken = "";
	private String mUId = "";
	
	public FacebookWall(Context context) {
		this.mContext = context;
		mAccessToken = Utility.mFacebook.getAccessToken();
		Log.e("ThangTB", "class FacebookWall - accessToken = "+ mAccessToken);
		mUId = Utility.userUID;
	}
	
	/**
	 * post message onto wall of facebook
	 * @param status (int) : level of player
	 * @param message (String) : message display on wall
	 * @return
	 */	
	public boolean postWall(String message){
		return postWall(message, null, null);
	}
	
	/**
	 * post message onto wall of facebook
	 * @param message (String) : message display on wall
	 * @param title (String) : title of message display on wall
	 * @param caption of message display on wall
	 * @return
	 */	
	public boolean postWall(String message,String title,String caption){
		
//		String url = formatStringToPost(message);
		WriteLog.i("ThangTB", message);
		return confirmPostWall(message);
		
	}
	
	
	/**
	 * format string message to post onto wall of facebook
	 * @param message (String) display on wall
	 * @return string url after format
	 */
	private String formatStringToPost(String message){
		String url = "https://api.facebook.com/method/stream.publish?" + "message=" + URLEncoder.encode(message) + "&attachment=&target_id=" + mUId + "&uid=" + mUId + "&access_token=" + mAccessToken;
		return url;
	}
	
	/**
	 * execute posting message onto wall
	 * @param url (String) after format
	 * @return true if success
	 */
	private boolean confirmPostWall(String url){
		boolean b = false;
		/*try {
			WriteLog.i("ThangTB", "url: " + url);
        	HttpGet httpGet = new HttpGet(url);
        	
        	HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 15000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);			
			HttpResponse httpResponse = httpclient.execute(httpGet);
			WriteLog.i("ThangTB", "httpRespone: " + httpResponse.toString());
			
		} catch (Exception e) {
			WriteLog.i("ThangTB", "try catch .......................");
			e.printStackTrace();
			return false;
		}*/
		
//		try{
//	        Bundle parameters = new Bundle();
//	        parameters.putString("message", url);
//	        String  response = Utility.mFacebook.request("me/feed",parameters,"POST");
//	        Log.v("response", response);
//	    }
//	    catch(Exception e){}
	    
	    try {
//            String response = Utility.mFacebook.request("me");
	    	String response="";
            Bundle parameters = new Bundle();
            parameters.putString("link", "");
            parameters.putString("name", "");
            parameters.putString("caption", "");
            
            parameters.putString("message", url);
            parameters.putString("description", "Quotes");
            response = Utility.mFacebook.request("me/feed", parameters, "POST");
            WriteLog.d("Tests", "got response: " + response);
            if (response == null || response.equals("") || response.equals("false")) {
            	b = false;
            	WriteLog.v("Error", "Blank response");
            }else{b = true;}
     } catch(Exception e) {
    	 b = false;
//         e.printStackTrace();
     }
		return b;
	}
}

