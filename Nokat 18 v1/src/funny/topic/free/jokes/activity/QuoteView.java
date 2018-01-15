/**
 * 
 */
package funny.topic.free.jokes.activity;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.benchaos.nokat.maroc.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import funny.topic.free.jokes.MainActivity;
import funny.topic.free.jokes.Widget;
import funny.topic.free.jokes.db.DataHeper;
import funny.topic.free.jokes.db.entity.Quote;
import funny.topic.free.jokes.dialog.DialogLoading;
import funny.topic.free.jokes.facebook.AsyncFacebookRunner;
import funny.topic.free.jokes.facebook.DialogError;
import funny.topic.free.jokes.facebook.Facebook;
import funny.topic.free.jokes.facebook.FacebookError;
import funny.topic.free.jokes.facebook.FacebookSessionEvents;
import funny.topic.free.jokes.facebook.FacebookWall;
import funny.topic.free.jokes.facebook.Utility;
import funny.topic.free.jokes.facebook.Facebook.DialogListener;
import funny.topic.free.jokes.utils.Constants;
import funny.topic.free.jokes.utils.WriteLog;

/**
 * @author ThangTB
 *
 */
public class QuoteView extends AbstractContentActivity{

//	view
	private ImageButton btn_action_share;
	private ImageButton btn_action_report;
	
	private TextView tv_body;
	
	private Button btn_next;
	private Button btn_pre;
	private Button btn_random;
	private Button btn_fav;
	// param
	private Quote quote;
	private DataHeper dataHeper;
	
	private ScrollView mScrollView;
	private LinearLayout ll_containt;
	
	/**
	 * left in
	 */
	Animation localAnimation1;
	/**
	 * right in
	 */
	Animation localAnimation2;
	/**
	 * bottom in
	 */
	Animation localAnimation3;
	
	 private static final int SWIPE_MIN_DISTANCE = 70;
	 private static final int SWIPE_MAX_OFF_PATH = 100;
	 private static final int SWIPE_THRESHOLD_VELOCITY = 90;
	    
	 private GestureDetector gestureDetector;
	 View.OnTouchListener gestureListener;
	 
	 private RelativeLayout parentView;
	 private ProgressDialog dialogLoading;
	 ArrayList<String> ListString;
	 
	 private int flagList=0;
	 int pos;
	 
	 private boolean isLoadingData = false;
	/* (non-Javadoc)
	 * @see funny.doctor.free.jokes.activity.AbstractActivity#getViewLayoutId()
	 */
	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.quote_preview;
		
	}

	/* (non-Javadoc)
	 * @see funny.doctor.free.jokes.activity.AbstractContentActivity#initView()
	 */
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		AdView adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
		super.initView();
		ListString = new ArrayList<String>();
		
		Bundle b = getIntent().getExtras();
		if (b.containsKey(Constants.Bundle_quote)) {
			this.quote = (Quote) b.get(Constants.Bundle_quote); 
		}else{
			this.finish();
		}
		
		if (b.containsKey(Constants.Bundle_CateList)) {
			ListString = b.getStringArrayList(Constants.Bundle_CateList);
			pos = b.getInt(Constants.Bundle_pos, 0);
			flagList =1;
		}else if (b.containsKey(Constants.Bundle_FavList)) {
			ListString = b.getStringArrayList(Constants.Bundle_FavList);
			pos = b.getInt(Constants.Bundle_pos, 0);
			flagList =2;
		}else if (b.containsKey(Constants.Bundle_AuthorList)) {
			ListString = b.getStringArrayList(Constants.Bundle_AuthorList);
			pos = b.getInt(Constants.Bundle_pos, 0);
			flagList =3;
		}else{
			flagList=0;
		}
		
		 localAnimation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_in);
		 localAnimation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_right_in);
		 localAnimation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_bottom_in);
		
		 parentView = (RelativeLayout)findViewById(R.id.qp_main_parent);
		 
		 
		dataHeper = new DataHeper(getApplicationContext());
		
		btn_action_share = (ImageButton)findViewById(R.id.actionbar_share_btn);
		btn_action_report = (ImageButton)findViewById(R.id.actionbar_report_btn);
		
		tv_body = (TextView)findViewById(R.id.qp_body);
		
		btn_next = (Button)findViewById(R.id.qp_next_btn);
		btn_pre = (Button)findViewById(R.id.qp_previous_btn);
		btn_random = (Button)findViewById(R.id.qp_random_btn);
		btn_fav = (Button)findViewById(R.id.qp_favourite);
		
		mScrollView = (ScrollView)findViewById(R.id.qp_body_wrapper);
		ll_containt = (LinearLayout)findViewById(R.id.qp_body_wrapper_container);
		
		if (quote.getIs_favourist()==1) {
			btn_fav.setBackgroundResource(R.drawable.qp_fav_btn_active);
		}else{
			btn_fav.setBackgroundResource(R.drawable.qp_fav_btn_normal);
		}
		
		tv_body.setText(Html.fromHtml(quote.getBody()));
		
		//set setting sharepreference
		 int colorQuote = customSharePreferences.getPreferencesInt(SettingActivity
					.KEY_QUOTE_FONT_COLOR);
			int fontSize = customSharePreferences.getPreferencesInt(SettingActivity
					.KEY_FONT_SIZE);
			int background = customSharePreferences.getPreferencesInt(SettingActivity
					.KEY_BACKGROUND_IMAGE);
			
			if (background ==0) {
				parentView.setBackgroundResource(R.drawable.bg_default);
			}else if (background ==1) {
				parentView.setBackgroundResource(R.drawable.bg_default_stripes);
			}else if (background ==2) {
				parentView.setBackgroundResource(R.drawable.bg_default_bubbles);
			}
			
			if (colorQuote!=0) {
				tv_body.setTextColor(colorQuote);
			}
			if (fontSize==0 || fontSize ==SettingActivity.FontDefault) {
				tv_body.setTextSize(TypedValue.COMPLEX_UNIT_SP, SettingActivity.FontDefault);
			}else{
				tv_body.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
			}
			
		final ImageButton btn_logo = (ImageButton)findViewById(R.id.actionbar_logo_btn);
		btn_logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				QuoteView.this.finish();
			}
		});
		
		//btn netx quote
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isLoadingData) {
					return;
				}else{
					isLoadingData = true;
				}
				new getDataNew(2,flagList,false).execute();
			}
		});
		//btn previous quote
		btn_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isLoadingData) {
					return;
				}else{
					isLoadingData = true;
				}
				new getDataNew(1,flagList,false).execute();
			}
		});
		//btn random quote
		btn_random.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isLoadingData) {
					return;
				}else{
					isLoadingData = true;
				}
				new getDataNew(3,flagList,false).execute();
			}
		});
//		btn favourist
		btn_fav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (quote.getIs_favourist()==1) {
					quote.setIs_favourist(0);
					dataHeper.DeleteFavourites(String.valueOf(quote.getId()));
					btn_fav.setBackgroundResource(R.drawable.qp_fav_btn_normal);
				}else{
					quote.setIs_favourist(1);
					dataHeper.AddFavourites(String.valueOf(quote.getId()));
					btn_fav.setBackgroundResource(R.drawable.qp_fav_btn_active);
				}
			}
		});
		
		//set touch
		
		 // Gesture detection
        gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        
        tv_body.setOnTouchListener(gestureListener);
        ll_containt.setOnTouchListener(gestureListener);
        mScrollView.setOnTouchListener(gestureListener);
        
        
        btn_action_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CharSequence[] arrayOfCharSequence = new CharSequence[4];
			    arrayOfCharSequence[0] = "Share on Facebook";
			    arrayOfCharSequence[1] = "Share with other apps";
			    arrayOfCharSequence[2] = "Copy to clipboard";
			    arrayOfCharSequence[3] = "Set Joke Of The Day";
			    AlertDialog.Builder builder = new AlertDialog.Builder(QuoteView.this);
			    builder.setTitle("Sharing options");
			    builder.setItems(arrayOfCharSequence, onclickdialog);
			    builder.create().show();
			}
		});
	}
	
	
	DialogInterface.OnClickListener onclickdialog = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int pos) {
			// TODO Auto-generated method stub
			if (pos==0) {
				facebook();
			}else if(pos ==1){
				String body = quote.getBody();
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_SUBJECT,  QuoteView.this.getString(R.string.email_fav_subject));
				i.putExtra(Intent.EXTRA_TEXT   ,  body);
				try {
				    startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(QuoteView.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
			}else if(pos ==2){
				ClipboardManager localClipboardManager = (ClipboardManager)QuoteView.this.getApplicationContext().getSystemService("clipboard");
				String body = quote.getBody();
			    localClipboardManager.setText(body);
			    Toast.makeText(QuoteView.this.getApplicationContext(), "Message copied to clipboard", 0).show();
			}else if(pos ==3){
				dataHeper.SaveQuoteOfDay(quote.getId(), quote.getBody());
				Intent intent = new Intent(getApplicationContext(),Widget.class);
				//update widget
				intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");

				// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
				// since it seems the onUpdate() is only fired on that:
				AppWidgetManager mgr = AppWidgetManager.getInstance(getApplicationContext());

				int[] ids = mgr.getAppWidgetIds(new ComponentName(getApplicationContext(), Widget.class));
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
				sendBroadcast(intent);
				WriteLog.d("ThangTB-AlarmService_Service", "has change notification------------------");
				
				dialog.dismiss();
			}
		}
	};
	
	private class getDataNew extends AsyncTask<Object, Object, Object>{
		/**
		 * 1 is pre. 
		 * 2 is next. 
		 * 3 is random. 
		 */
		private int action;
		
		private int list;
		private boolean istouch;
		private Quote mQuote;
		/**
		 * 1 is pre. 
		 * 2 is next. 
		 * 3 is random.
		 * 
		 * @param action
		 */
		public getDataNew(int action, int list , boolean istouch) {
			this.action = action;
			this.list = list;
			this.istouch = istouch;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			switch (list) {
			case 0:
				if (action==1) {
					if (quote.getId() !=1) {
						mQuote = dataHeper.getPreQuote(quote.getId());
						if (mQuote!=null) {
							quote = mQuote;
						}
					}
					
				}else if(action ==2){
					mQuote = dataHeper.getNextQuote(quote.getId());
					if (mQuote!=null) {
						quote = mQuote;
					}
				}else if(action ==3){
					int total = dataHeper.getTotalQuotesNoFilter();
					mQuote = dataHeper.getQuoteRandom(total);
					if (mQuote!=null) {
						quote = mQuote;
					}
				}
				break;
				
			case 1:
			case 3:
			case 2:
				if (action==1) {
					if (pos!= 0) {
						pos = pos-1;
						mQuote = dataHeper.getQuoteById(Integer.parseInt(ListString.get(pos)));
						if (mQuote!=null) {
							quote = mQuote;
						}
					}
					
				}else if(action ==2){
					if (pos<(ListString.size()-1)) {
						pos = pos+1;
						mQuote = dataHeper.getQuoteById(Integer.parseInt(ListString.get(pos)));
						if (mQuote!=null) {
							quote = mQuote;
						}
					}
					
				}else if(action ==3){
					Random rand = new Random(); 
					int a =  rand.nextInt(ListString.size());
					pos = a;
					
					mQuote = dataHeper.getQuoteById(Integer.parseInt(ListString.get(pos)));
					if (mQuote!=null) {
						quote = mQuote;
					}
				}
				break;

			default:
				break;
			}
			
			
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			
			if (quote.getIs_favourist()==1) {
				btn_fav.setBackgroundResource(R.drawable.qp_fav_btn_active);
			}else{
				btn_fav.setBackgroundResource(R.drawable.qp_fav_btn_normal);
			}
			tv_body.setText(Html.fromHtml(quote.getBody()));
			
			if (action==1) {
				if (istouch) {
					mScrollView.startAnimation(localAnimation2);
				}else{
					mScrollView.startAnimation(localAnimation1);
				}
			}else if(action ==2){
				if (istouch) {
					mScrollView.startAnimation(localAnimation1);
				}else{
					mScrollView.startAnimation(localAnimation2);
				}
			}else{
				mScrollView.startAnimation(localAnimation3);
			}
			
			isLoadingData = false;
			super.onPostExecute(result);
		}
	}
	
	 class MyGestureDetector extends SimpleOnGestureListener {
	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	            try {
	            	WriteLog.d("ThangTB", "e1.getX() =  "+e1.getX()+" e2.getX() = "+e2.getX()+" velocityX =  "+velocityX+" velocityY =  "+velocityY);
	            	
	                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                    return false;
	                // right to left swipe
	                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                	WriteLog.d("ThangTB", "LEFT - e1.getX() =  "+e1.getX()+" e2.getX() = "+e2.getX()+" velocityX =  "+velocityX+" velocityY =  "+velocityY);
	                	
	                    new getDataNew(2,flagList,true).execute();
	                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                	WriteLog.d("ThangTB", "RIGHT - e1.getX() =  "+e1.getX()+" e2.getX() = "+e2.getX()+" velocityX =  "+velocityX+" velocityY =  "+velocityY);
	                    new getDataNew(1,flagList,true).execute();
	                }
	            	
	            } catch (Exception e) {
	                // nothing
	            }
	            return false;
	        }

	    }
	
	 
	 String[] permissions = { "offline_access", "publish_stream", "user_photos",
				"publish_checkins", "photo_upload" };
	 final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 10;
	 public void facebook() {
			// Instantiate the asynrunner object for asynchronous api calls.
			Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

			// restore session if one exists
//			SessionStore.restore(Utility.mFacebook, this);
			FacebookSessionEvents.addAuthListener(new FbAPIsAuthListener());

			if (Utility.mFacebook.isSessionValid()) {
				// requestUserData();
				WriteLog.d("Facebook", "has login");
				
				try {
					new PostFace().execute();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(this, "Posting Failed", Toast.LENGTH_LONG).show();
				}
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
				try {
	        			new PostFace().execute();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(QuoteView.this, "Posting Failed", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onAuthFail(String error) {
				WriteLog.e( "FACEBOOK ", "Login Failed: " + error);
			}
		}

		private final class LoginDialogListener implements DialogListener {

			/* (non-Javadoc)
			 * @see funny.doctor.free.jokes.facebook.Facebook.DialogListener#onComplete(android.os.Bundle)
			 */
			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub
				customSharePreferences.setSharePreferencesString(Facebook.TOKEN, values.getString(Facebook.TOKEN));
				customSharePreferences.setSharePreferencesString(Facebook.EXPIRES, values.getString(Facebook.EXPIRES));
				FacebookSessionEvents.onLoginSuccess();
			}

			/* (non-Javadoc)
			 * @see funny.doctor.free.jokes.facebook.Facebook.DialogListener#onFacebookError(funny.doctor.free.jokes.facebook.FacebookError)
			 */
			@Override
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
				FacebookSessionEvents.onLoginError(e.getMessage());
			}

			/* (non-Javadoc)
			 * @see funny.doctor.free.jokes.facebook.Facebook.DialogListener#onError(funny.doctor.free.jokes.facebook.DialogError)
			 */
			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
				FacebookSessionEvents.onLoginError(e.getMessage());
			}

			/* (non-Javadoc)
			 * @see funny.doctor.free.jokes.facebook.Facebook.DialogListener#onCancel()
			 */
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				FacebookSessionEvents.onLoginError("Action Canceled");
			}
		}
		
	/* (non-Javadoc)
		 * @see funny.doctor.free.jokes.activity.AbstractContentActivity#onDestroy()
		 */
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			
			super.onDestroy();
		}
		
		
		private class PostFace extends AsyncTask<Object, Object, Object>{

			private boolean r;
			/* (non-Javadoc)
			 * @see android.os.AsyncTask#onPreExecute()
			 */
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				dialogLoading = DialogLoading.Loading(QuoteView.this,"Posting ...");
				dialogLoading.show();
				super.onPreExecute();
			}
			/* (non-Javadoc)
			 * @see android.os.AsyncTask#doInBackground(Params[])
			 */
			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				FacebookWall wall = new FacebookWall(QuoteView.this);
    			r = wall.postWall(quote.getBody());
    			
				return null;
			}
			
			/* (non-Javadoc)
			 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
			 */
			@Override
			protected void onPostExecute(Object result) {
				// TODO Auto-generated method stub
				dialogLoading.dismiss();
				if (r) {
    				Toast.makeText(QuoteView.this, "Posted Successfully", Toast.LENGTH_LONG).show();
				}
    			else{
    				Toast.makeText(QuoteView.this, "Posting Failed", Toast.LENGTH_LONG).show();
    			}
				super.onPostExecute(result);
			}
		}
}
