/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package funny.topic.free.jokes;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.benchaos.nokat.maroc.R;

import funny.topic.free.jokes.db.DataHeper;
import funny.topic.free.jokes.db.entity.Quote;
import funny.topic.free.jokes.utils.Constants;
import funny.topic.free.jokes.utils.CustomSharePreferences;
import funny.topic.free.jokes.utils.WriteLog;

/**
 * This is an example of implementing an application service that will run in
 * response to an alarm, allowing us to move long duration work out of an
 * intent receiver.
 * 
 * @see AlarmService
 * @see AlarmService_Alarm
 */
public class AlarmService_Service extends Service {
    NotificationManager mNM;
    DataHeper dataHeper;
    CustomSharePreferences s;
    private Quote quote;
    
    int day = 1000*60*60*24;
//  int day = 30000;
    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        dataHeper = new DataHeper(getApplicationContext());
        s = new CustomSharePreferences(getApplicationContext());
        // show the icon in the status bar
        new GetData().execute();

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        Thread thr = new Thread(null, mTask, "AlarmService_Service");
        thr.start();
    }

    @Override
    public void onDestroy() {
        // Cancel the notification -- we use the same ID that we had used to start it
        mNM.cancel(R.string.widget_name);

        // Tell the user we stopped.
//        WriteLog.d("ThangTB-AlarmService_Service", "has change notification------------------");
    }

    /**
     * The function that runs in our worker thread
     */
    Runnable mTask = new Runnable() {
        public void run() {
//        	int day = 1000*60*60*24;
            long endTime = System.currentTimeMillis() + day;
            while (System.currentTimeMillis() < endTime) {
                synchronized (mBinder) {
                    try {
//                        mBinder.wait(endTime - System.currentTimeMillis());
                    	mBinder.wait(day);
                    } catch (Exception e) {
                    }
                }
            }
            // Done with our work...  stop the service!
            AlarmService_Service.this.stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
      CharSequence text = "Doctor Jokes SF";

        // Set the icon, scrolling text and timestamp
      Notification notification = new Notification(R.drawable.icon, text,System.currentTimeMillis());
      
      Intent notifyIntent = new Intent(this, MainActivity.class);
     // Sets the Activity to start in a new, empty task
     notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
     
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
          notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the info for the views that show in the notification panel.
        //notification.setLatestEventInfo(this, quote.getBody(), text, contentIntent);

        notification.flags |=Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
      //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.sound = soundUri;
        
        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.cancelAll();
        mNM.notify(0, notification);
    }

    /**
     * This is the object that receives interactions from clients.  See RemoteService
     * for a more complete example.
     */
    private final IBinder mBinder = new Binder() {
        @Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
		        int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };
    
    /**
	 * get data task
	 * @author ThangTB
	 *
	 */
	private class GetData extends AsyncTask<Object, Object, Object>{

		
		public GetData() {
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			String sTotal = s.getPreferencesString(Constants.TotalQuote);
			if (sTotal.equals("") || sTotal == null) {
				sTotal = "0";
			}
			int total = Integer.parseInt(sTotal);
			if (total==0) {
				total = dataHeper.getTotalQuotesNoFilter();
				s.setSharePreferencesString(Constants.TotalQuote, String.valueOf(total));
			}
			quote = dataHeper.getQuoteRandom(total);
			
			dataHeper.SaveQuoteOfDay(quote.getId(), quote.getBody());
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			if (s.getPreferencesString(Constants.ShowNotice).equals("1")) {
				showNotification();
			}
			Intent intent = new Intent(getApplicationContext(),Widget.class);
			intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");

			// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
			// since it seems the onUpdate() is only fired on that:
			AppWidgetManager mgr = AppWidgetManager.getInstance(getApplicationContext());

			int[] ids = mgr.getAppWidgetIds(new ComponentName(getApplicationContext(), Widget.class));
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
			sendBroadcast(intent);
			WriteLog.d("ThangTB-AlarmService_Service", "has change notification------------------");
			super.onPostExecute(result);
		}
	}
}

