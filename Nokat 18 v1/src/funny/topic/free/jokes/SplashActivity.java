/**
 * 
 */
package funny.topic.free.jokes;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.benchaos.nokat.maroc.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import funny.topic.free.jokes.db.DataHeper;
import funny.topic.free.jokes.db.entity.QOD;
import funny.topic.free.jokes.db.entity.Quote;
import funny.topic.free.jokes.utils.Constants;
import funny.topic.free.jokes.utils.CustomSharePreferences;

/**
 * @author ThangTB
 *
 */
public class SplashActivity extends Activity {
	private ImageView imgLoading;
	DataHeper dataHeper;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash_screen);
		AdView adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
//		 ChangeActivity();
		
		dataHeper = new DataHeper(getApplicationContext());
		new ChangeActivity().execute();
	}
	
	
	private class ChangeActivity extends AsyncTask<Object, Object, Object>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			CustomSharePreferences s = new CustomSharePreferences(getApplicationContext());
			QOD qod = dataHeper.getQOD();
			Date date = new Date();
			int day = 1000*60*60*24;
			if ((date.getTime()-qod.getTime())>=day) {
				String sTotal = s.getPreferencesString(Constants.TotalQuote);
				if (sTotal.equals("") || sTotal == null) {
					sTotal = "0";
				}
				int total = Integer.parseInt(sTotal);
				if (total==0) {
					total = dataHeper.getTotalQuotesNoFilter();
					s.setSharePreferencesString(Constants.TotalQuote, String.valueOf(total));
				}
					
				
				Quote quote = dataHeper.getQuoteRandom(total);
				
				dataHeper.SaveQuoteOfDay(quote.getId(), quote.getBody());
			}
			
			try {
				new Thread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
            intent.setClass(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            // finish the current activity
            SplashActivity.this.finish();
			super.onPostExecute(result);
		}
		
	}
	 private void ChangeActivity(){
			//Create an object of type SplashHandler
	        SplashHandler mHandler = new SplashHandler();

	        // Create a Message object
	        Message msg = new Message();
	        
	        //Assign a unique code to the message.
	        //Later, this code will be used to identify the message in Handler class.
	        msg.what = 0;
	        
	        // Send the message with a delay of 3 seconds(3000 = 3 sec).
	        mHandler.sendMessageDelayed(msg, 3000);
		}
		
		// Handler class implementation to handle the message
	    /**
		 * The Class SplashHandler.
		 */
		private class SplashHandler extends Handler {
	        
	        //This method is used to handle received messages
	        /* (non-Javadoc)
	         * @see android.os.Handler#handleMessage(android.os.Message)
	         */
	        public void handleMessage(Message msg)
	          {
	            // switch to identify the message by its code
	            switch (msg.what)
	            {
	            default:
	            case 0:
	              super.handleMessage(msg);
	              
	              //Create an intent to start the new activity.
	              // Our intention is to start MainActivity
	              Intent intent = new Intent();
	              intent.setClass(SplashActivity.this,MainActivity.class);
	              startActivity(intent);
	              // finish the current activity
	              SplashActivity.this.finish();
	            }
	          }
		}

}
