package funny.topic.free.jokes.activity;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.benchaos.nokat.maroc.R;

import funny.topic.free.jokes.colorPicker.ColorPickerDialog2;
import funny.topic.free.jokes.colorPicker.ColorPickerDialog2.OnColorChangedListener;
import funny.topic.free.jokes.facebook.Facebook;
import funny.topic.free.jokes.facebook.Utility;
import funny.topic.free.jokes.utils.CustomSharePreferences;
import funny.topic.free.jokes.utils.WriteLog;

// TODO: Auto-generated Javadoc
/**
 * AbstractContentACtivity.
 *
 * @author ThangTB
 */
public abstract class AbstractContentActivity extends AbstractActivity{
	
	/** The tab bar. */
	public LinearLayout tabBar;
	
	/** The activity. */
	AbstractActivity activity;
	public String mAccessToken = null;
    public long mAccessExpires = 0;
    public CustomSharePreferences customSharePreferences;
	/* (non-Javadoc)
	 * @see com.Starwarsguy.Starwars.Abstract.AbstractActivity#initView()
	 */
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		activity = this;
		customSharePreferences = new CustomSharePreferences(getApplicationContext());
		mAccessToken = customSharePreferences.getPreferencesString(Facebook.TOKEN);
		WriteLog.d("AbstractActivity", "access token = "+mAccessToken);
		String ex = customSharePreferences.getPreferencesString(Facebook.EXPIRES);
		if (ex ==null || ex.equals("")) {
			mAccessExpires = 0;
		}else{
			mAccessExpires = Long.parseLong(ex);
		}
		
		WriteLog.d("AbstractActivity", "access expires = "+mAccessExpires);
		Utility.mFacebook = new Facebook(this.getString(R.string.facebook_app_id),customSharePreferences,mAccessToken,mAccessExpires);
		
	}
	/**
	 * Gets the resource ID of the view layout.
	 * @return Resource ID.
	 */
//	protected abstract int getTitleString();
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.dashboard_menu, menu);
		Log.e("ThangTB", "Create menu");
		
		return super.onCreateOptionsMenu(menu);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.g_menu_settings:
			Intent intentSetting = new Intent(getApplicationContext(), SettingActivity.class);
			startActivity(intentSetting);
			break;
		case R.id.g_menu_help:
			Intent intentHelp = new Intent(getApplicationContext(), HelpActivity.class);
			startActivity(intentHelp);
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
		
	private void showColorPicker(int paramInt){
		// initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
		ColorPickerDialog2 dialog2 = new ColorPickerDialog2(AbstractContentActivity.this, onColorChangedListener, paramInt);
		dialog2.show();
	}
	
	OnColorChangedListener onColorChangedListener = new OnColorChangedListener() {

		@Override
		public void colorChanged(int color) {
			// TODO Auto-generated method stub
			Log.e("ThangTB", "colorChanged = "+color);
		}
		
		
	};
	/* (non-Javadoc)
	 * @see com.Starwarsguy.Starwars.Abstract.AbstractActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		
		super.onDestroy();
	}

}
