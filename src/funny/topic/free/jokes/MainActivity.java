package funny.topic.free.jokes;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.benchaos.nokat.maroc.R;
import funny.topic.free.jokes.activity.AbstractContentActivity;
import funny.topic.free.jokes.activity.FavouristList;
import funny.topic.free.jokes.activity.QuoteList;
import funny.topic.free.jokes.activity.SearchActivity;
import funny.topic.free.jokes.activity.SettingActivity;
import funny.topic.free.jokes.db.DataHeper;
import funny.topic.free.jokes.db.entity.QOD;
import funny.topic.free.jokes.utils.CustomSharePreferences;
import funny.topic.free.jokes.utils.WriteLog;

public class MainActivity extends AbstractContentActivity{

	private ImageButton btn_author;
	private ImageButton btn_quotes;
	private ImageButton btn_fav;
	private ImageButton btn_facebook;
	private ImageButton btn_search;
	
	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 10;
	private DataHeper dataHeper;
	
	private TextView tv_body;
	private PendingIntent mAlarmSender;
	
	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractContentActivity#initView()
	 */
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		 AdView adView = (AdView)this.findViewById(R.id.adView);
		    AdRequest adRequest = new AdRequest.Builder().build();
		    adView.loadAd(adRequest);
		super.initView();
		CustomSharePreferences sharePreferences = new CustomSharePreferences(getApplicationContext());
		
		btn_author = (ImageButton)findViewById(R.id.d_authors_btn);
		btn_quotes = (ImageButton)findViewById(R.id.d_quotes_btn);
		btn_fav = (ImageButton)findViewById(R.id.d_favs_btn);
		
		tv_body = (TextView)findViewById(R.id.d_qod_body);
		btn_facebook = (ImageButton)findViewById(R.id.actionbar_facebook_btn);
		btn_search = (ImageButton)findViewById(R.id.actionbar_search_btn);
		
		dataHeper = new DataHeper(getApplicationContext());
		btn_author.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WriteLog.d("ThangTB", "onclick image");
				Intent i = new Intent(getApplicationContext(), SettingActivity.class);
				startActivity(i);
			}
		});
		
		btn_quotes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WriteLog.d("ThangTB", "onclick image");
				Intent i = new Intent(getApplicationContext(), QuoteList.class);
				startActivity(i);
			}
		});
		
		btn_fav.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						WriteLog.d("ThangTB", "onclick image");
						Intent i = new Intent(getApplicationContext(), FavouristList.class);
						startActivity(i);
					}
				});
		btn_facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				facebook();
			}
		});
		
		btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentSearch = new Intent(getApplicationContext(), SearchActivity.class);
				startActivity(intentSearch);
			}
		});
		
		mAlarmSender = PendingIntent.getService(MainActivity.this,
	                0, new Intent(MainActivity.this, AlarmService_Service.class), 0);
		 // We want the alarm to go off 30 seconds from now.
        long firstTime = SystemClock.elapsedRealtime();

        // Schedule the alarm!
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime, 30*1000, mAlarmSender);
	}

	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractActivity#getViewLayoutId()
	 */
	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.dashboard;
	}
	
	
	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		new GetData().execute();
		super.onResume();
	}

	/**
	 * get data task
	 * @author ThangTB
	 *
	 */
	private class GetData extends AsyncTask<Object, Object, Object>{

		private QOD quoteOfDay;
		public GetData() {
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			quoteOfDay = dataHeper.getQOD();
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			if (quoteOfDay!=null) {
				tv_body.setText(Html.fromHtml(quoteOfDay.getBody()));
			}
			super.onPostExecute(result);
		}
	}
	
	public void facebook() {
		Uri localUri = Uri.parse(getResources().getString(R.string.facebook_fanpage));
	    Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
	    startActivity(localIntent);

	}
}
