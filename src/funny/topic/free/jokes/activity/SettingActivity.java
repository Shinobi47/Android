/**
 * 
 */
package funny.topic.free.jokes.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benchaos.nokat.maroc.R;

import funny.topic.free.jokes.colorPicker.ColorPickerDialog2;
import funny.topic.free.jokes.colorPicker.ColorPickerDialog2.OnColorChangedListener;
import funny.topic.free.jokes.dialog.AboutDialog;
import funny.topic.free.jokes.facebook.AsyncFacebookRunner;
import funny.topic.free.jokes.facebook.DialogError;
import funny.topic.free.jokes.facebook.Facebook;
import funny.topic.free.jokes.facebook.FacebookError;
import funny.topic.free.jokes.facebook.FacebookSessionEvents;
import funny.topic.free.jokes.facebook.SessionStore;
import funny.topic.free.jokes.facebook.Utility;
import funny.topic.free.jokes.facebook.Facebook.DialogListener;
import funny.topic.free.jokes.utils.Constants;
import funny.topic.free.jokes.utils.WriteLog;

/**
 * @author ThangTB
 *
 */
public class SettingActivity extends AbstractContentActivity implements OnClickListener{

	  public static final String KEY_AD_NOTIFICATION = "display_notification_ads";
	  public static final String KEY_BACKGROUND_IMAGE = "background_image";
	  public static final String KEY_DATA_BACKUP = "data_backup";
	  public static final String KEY_FONT_SIZE = "quote_font_size";
	  public static final String KEY_LARGE_WIDGET_SETTINGS = "large_widget_settings";
	  public static final String KEY_NOTIFICATION = "qod_show_in_notifications";
	  public static final String KEY_QUOTE_FONT_COLOR = "quote_font_color";
	  public static final String KEY_RESET_CHANGES = "reset_changes";
	  public static final String KEY_WIDGET_SETTINGS = "widget_settings";
	  
	  private boolean isQuoteColor = true;
	  
	  AboutDialog aboutDialog;
	  
	  public static final int FontDefault = 17;
	  public static final int FontSmall = 14;
	  public static final int FontLage = 22;
	  
	  private static final int DIALOG_REMOVE_ALL = 1;
	  
	  
	  private TextView tvFontSize;
	  
	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractActivity#getViewLayoutId()
	 */
	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.setting;
	}
	
	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractContentActivity#initView()
	 */
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		
//		customSharePreferences = new CustomSharePreferences(getApplicationContext());
		LinearLayout llbgImage = (LinearLayout)findViewById(R.id.st_ll_backgroundImage);
		LinearLayout llquoteColor = (LinearLayout)findViewById(R.id.st_ll_quoteColor);
		LinearLayout llfontSize = (LinearLayout)findViewById(R.id.st_ll_fontSize);
		LinearLayout llReset = (LinearLayout)findViewById(R.id.st_ll_reset);
		LinearLayout llFaceBook = (LinearLayout)findViewById(R.id.st_ll_facebook);
		CheckBox chk = (CheckBox)findViewById(R.id.st_checkbox);
		LinearLayout tv_backup = (LinearLayout)findViewById(R.id.st_tv_backup);
		LinearLayout tv_about = (LinearLayout)findViewById(R.id.st_tv_about);
		
		llbgImage.setOnClickListener(this);
		llquoteColor.setOnClickListener(this);
		llfontSize.setOnClickListener(this);
		llReset.setOnClickListener(this);
		llFaceBook.setOnClickListener(this);
		tv_backup.setOnClickListener(this);
		tv_about.setOnClickListener(this);
		
		tvFontSize = (TextView)findViewById(R.id.tv_viewFontSize);
		int fontSize = customSharePreferences.getPreferencesInt(SettingActivity
				.KEY_FONT_SIZE);
		if (fontSize==0 || fontSize ==FontDefault) {
			tvFontSize.setText("Normal");
		}else if(fontSize ==FontSmall){
			tvFontSize.setText("Small");
		}else if(fontSize ==FontLage){
			tvFontSize.setText("Large");
		}
		
		if (customSharePreferences.getPreferencesString(Constants.ShowNotice).equals("1")) {
			chk.setChecked(true);
		}else{
			chk.setChecked(false);
			}
		
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					customSharePreferences.setSharePreferencesString(Constants.ShowNotice, "1");
				}else{
					customSharePreferences.setSharePreferencesString(Constants.ShowNotice, "0");
				}
				
			}
		});
	}
	
	
	private void showColorPicker(int paramInt){
		// initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
		ColorPickerDialog2 dialog2 = new ColorPickerDialog2(SettingActivity.this, onColorChangedListener, paramInt);
		dialog2.show();
	}
	
	OnColorChangedListener onColorChangedListener = new OnColorChangedListener() {

		@Override
		public void colorChanged(int color) {
			// TODO Auto-generated method stub
			if (isQuoteColor) {
				customSharePreferences.setSharePreferencesInt(KEY_QUOTE_FONT_COLOR, color);
			}
			
			Log.e("ThangTB", "colorChanged = "+color);
		}
		
		
	};
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.st_ll_backgroundImage:
			CharSequence[] arraybg = new CharSequence[3];
			arraybg[0] = "Leather";
			arraybg[1] = "Stripes";
			arraybg[2] = "Bubbles";
		    
		    int sizebg = customSharePreferences.getPreferencesInt(KEY_BACKGROUND_IMAGE);
		    
		    AlertDialog.Builder builderbg = new AlertDialog.Builder(SettingActivity.this);
		    builderbg.setTitle("Background image");
		    builderbg.setSingleChoiceItems(arraybg, sizebg, onclickdialogbgImage);
		    builderbg.create().show();
			break;
		
		case R.id.st_ll_quoteColor:
			isQuoteColor = true;
			showColorPicker(customSharePreferences.getPreferencesInt(KEY_QUOTE_FONT_COLOR));
			WriteLog.d("ThangTB", "color = "+customSharePreferences.getPreferencesInt(KEY_QUOTE_FONT_COLOR));
			break;

		case R.id.st_ll_fontSize:
			CharSequence[] arrayOfCharSequence = new CharSequence[3];
		    arrayOfCharSequence[0] = "Small";
		    arrayOfCharSequence[1] = "Normal";
		    arrayOfCharSequence[2] = "Large";
		    
		    int size = customSharePreferences.getPreferencesInt(KEY_FONT_SIZE);
		    int pos = 1;
		    if (size == 0 || size == FontDefault) {
				pos =1;
			}else if(size==FontSmall){
				pos =0;
			}else if(size==FontLage){
				pos =2;
			}
		    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
		    builder.setTitle("Font Size");
		    builder.setSingleChoiceItems(arrayOfCharSequence, pos, onclickdialog);
		    builder.create().show();
			break;
			
		case R.id.st_ll_reset:
			showDialog(DIALOG_REMOVE_ALL);
			break;
		case R.id.st_ll_facebook:
			facebook();
			break;
			
		case R.id.st_tv_backup:
			break;
			
		case R.id.st_tv_about:
			aboutDialog=new AboutDialog(SettingActivity.this);
			aboutDialog.show();
			break;
		default:
			break;
		}
	}
	
	
	DialogInterface.OnClickListener onclickdialog = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int pos) {
			// TODO Auto-generated method stub
			if (pos==0) {
				customSharePreferences.setSharePreferencesInt(KEY_FONT_SIZE, FontSmall);
				tvFontSize.setText("Small");
				dialog.dismiss();
			}else if(pos ==1){
				customSharePreferences.setSharePreferencesInt(KEY_FONT_SIZE, FontDefault);
				tvFontSize.setText("Normal");
				dialog.dismiss();
			}else if(pos ==2){
				customSharePreferences.setSharePreferencesInt(KEY_FONT_SIZE, FontLage);
				tvFontSize.setText("Large");
				dialog.dismiss();
			}
		}
	};
	
	DialogInterface.OnClickListener onclickdialogbgImage = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int pos) {
			// TODO Auto-generated method stub
			if (pos==0) {
				customSharePreferences.setSharePreferencesInt(KEY_BACKGROUND_IMAGE, 0);
				dialog.dismiss();
			}else if(pos ==1){
				customSharePreferences.setSharePreferencesInt(KEY_BACKGROUND_IMAGE, 1);
				dialog.dismiss();
			}else if(pos ==2){
				customSharePreferences.setSharePreferencesInt(KEY_BACKGROUND_IMAGE, 2);
				dialog.dismiss();
			}
		}
	};
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_REMOVE_ALL:
			return new AlertDialog.Builder(this)
			.setMessage(R.string.confirm_remove_pre)
			.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int whichButton) {
							customSharePreferences.setSharePreferencesInt(KEY_QUOTE_FONT_COLOR, 0);
							customSharePreferences.setSharePreferencesInt(KEY_FONT_SIZE, FontDefault);
							customSharePreferences.setSharePreferencesInt(KEY_BACKGROUND_IMAGE, 0);
							tvFontSize.setText("Normal");
						}
					})
			.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							return;
						}
					}).create();
		default:
			break;
		}
		return null;
	}
	
	 String[] permissions = { "offline_access", "publish_stream", "user_photos",
				"publish_checkins", "photo_upload" };
	 final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 10;
	 public void facebook() {
			// Instantiate the asynrunner object for asynchronous api calls.
			Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

			// restore session if one exists
			SessionStore.restore(Utility.mFacebook, this);
			FacebookSessionEvents.addAuthListener(new FbAPIsAuthListener());

			if (Utility.mFacebook.isSessionValid()) {
				WriteLog.d("Facebook", "has login");
				Toast.makeText(SettingActivity.this, "Facebook has login ", Toast.LENGTH_LONG).show();
			} else {
				Utility.mFacebook.authorize(this, permissions,
						AUTHORIZE_ACTIVITY_RESULT_CODE, new LoginDialogListener());
			}
		}

		/*
		 * The Callback for notifying the application when authorization succeeds or
		 * fails.
		 */

		public class FbAPIsAuthListener
				implements
				FacebookSessionEvents.AuthListener {

			@Override
			public void onAuthSucceed() {
				Toast.makeText(SettingActivity.this, "Login facebook sucessful! ", Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void onAuthFail(String error) {
				WriteLog.e( "FACEBOOK ", "Login Failed: " + error);
			}
		}

		private final class LoginDialogListener implements DialogListener {

			/* (non-Javadoc)
			 * @see com.android.jokesapp.jokes2.facebook.Facebook.DialogListener#onComplete(android.os.Bundle)
			 */
			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub
				customSharePreferences.setSharePreferencesString(Facebook.TOKEN, values.getString(Facebook.TOKEN));
				customSharePreferences.setSharePreferencesString(Facebook.EXPIRES, values.getString(Facebook.EXPIRES));
				FacebookSessionEvents.onLoginSuccess();
			}

			/* (non-Javadoc)
			 * @see com.android.jokesapp.jokes2.facebook.Facebook.DialogListener#onFacebookError(com.android.jokesapp.jokes2.facebook.FacebookError)
			 */
			@Override
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
				FacebookSessionEvents.onLoginError(e.getMessage());
			}

			/* (non-Javadoc)
			 * @see com.android.jokesapp.jokes2.facebook.Facebook.DialogListener#onError(com.android.jokesapp.jokes2.facebook.DialogError)
			 */
			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
				FacebookSessionEvents.onLoginError(e.getMessage());
			}

			/* (non-Javadoc)
			 * @see com.android.jokesapp.jokes2.facebook.Facebook.DialogListener#onCancel()
			 */
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				FacebookSessionEvents.onLoginError("Action Canceled");
			}
		}
		
}
