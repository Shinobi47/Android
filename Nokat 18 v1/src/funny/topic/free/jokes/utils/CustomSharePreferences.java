/**
 * 
 */
package funny.topic.free.jokes.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Custom SharePreferences
 * 
 * @author ThangTB
 *
 */
public class CustomSharePreferences {

	private Context mContext;
	private SharedPreferences mSharedPreferences; 
	private Editor edit;
	
	/**
	 * @param c
	 */
	public CustomSharePreferences(Context c) {
		// TODO Auto-generated constructor stub
		this.mContext = c;
		
		mSharedPreferences = mContext.getSharedPreferences("savehistory", Context.MODE_PRIVATE);
		edit = mSharedPreferences.edit();
	}
	
	/**
	 * 
	 * get SharePreferences String
	 * 
	 * @param key
	 * @return
	 */
	public String getPreferencesString(String key){
		return mSharedPreferences.getString(key, "");
	}
	
	/**
	 * get SharePreferences int
	 * 
	 * @param key
	 * @return
	 */
	public int getPreferencesInt(String key){
		return mSharedPreferences.getInt(key, 0);
		
	}
	/**
	 * set SharePreferences String
	 * 
	 * @param key
	 * @param value
	 */
	public void setSharePreferencesString(String key, String value){
		edit.putString(key, value);
		edit.commit();
	}
	
	/**
	 * set SharePreferences integer
	 * @param key
	 * @param value
	 */
	public void setSharePreferencesInt(String key, int value){
		edit.putInt(key, value);
		edit.commit();
	}
}
