/**
 * 
 */
package funny.topic.free.jokes;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.text.Html;
import android.widget.RemoteViews;

import com.benchaos.nokat.maroc.R;
import funny.topic.free.jokes.db.DataHeper;
import funny.topic.free.jokes.db.entity.QOD;
import funny.topic.free.jokes.utils.WriteLog;

/**
 * @author ThangTB
 *
 */
public class Widget extends AppWidgetProvider{
	
	RemoteViews remoteViews;
	AppWidgetManager appWidgetManager;
	ComponentName thisWidget;
	QOD entity;
	DataHeper dataHeper;
	
	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		this.appWidgetManager = appWidgetManager;
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		thisWidget = new ComponentName(context, Widget.class);
		
		dataHeper = new DataHeper(context);
		
		WriteLog.d("ThagnTB-widget", "update widget");
		getQuoteOfDay(remoteViews);
		this.appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		
	}
	
	private void getQuoteOfDay(RemoteViews remoteViews){
		entity = dataHeper.getQOD();
		
		remoteViews.setTextViewText(R.id.wg_body, Html.fromHtml(entity.getBody()));
	}

}
