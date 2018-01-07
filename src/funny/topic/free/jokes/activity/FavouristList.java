/**
 * 
 */
package funny.topic.free.jokes.activity;

import java.util.ArrayList;
import com.google.android.gms.ads.*;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.benchaos.nokat.maroc.R;

import funny.topic.free.jokes.MainActivity;
import funny.topic.free.jokes.adapter.QuoteAdapter;
import funny.topic.free.jokes.adapter.QuoteAdapter.OnClickCheckBoxListener;
import funny.topic.free.jokes.db.DataHeper;
import funny.topic.free.jokes.db.entity.Quote;
import funny.topic.free.jokes.dialog.DialogLoading;
import funny.topic.free.jokes.utils.Constants;
import funny.topic.free.jokes.utils.WriteLog;

/**
 * @author ThangTB
 *
 */
public class FavouristList extends AbstractContentActivity {

	private ListView lv;
	private ArrayList<Quote> listData;
	private DataHeper dataHeper;
	private ProgressDialog dialogLoading;
	private ImageButton btn_RemoveAll;
	private QuoteAdapter adapter;
	private static final int DIALOG_REMOVE_ALL = 0;
	
	TextView tv_empty;
	private int currentPostion =0;
	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractActivity#getViewLayoutId()
	 */
	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.favourite_index;
		
	}

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
		
		lv = (ListView)findViewById(R.id.fi_ListView);
		tv_empty = (TextView)findViewById(R.id.empty);
		btn_RemoveAll = (ImageButton)findViewById(R.id.fi_remove_all);
		listData = new ArrayList<Quote>();
		dataHeper = new DataHeper(getApplicationContext());
		
		final ImageButton btn_logo = (ImageButton)findViewById(R.id.actionbar_logo_btn);
		btn_logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				FavouristList.this.finish();
			}
		});
		
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), QuoteView.class);
				ArrayList<String> arrlist = new ArrayList<String>();
				for (int j = 0; j < listData.size(); j++) {
					arrlist.add(String.valueOf(listData.get(j).getId()));
				}
				i.putExtra(Constants.Bundle_pos, pos);
				i.putExtra(Constants.Bundle_FavList, arrlist);
				i.putExtra(Constants.Bundle_quote, listData.get(pos));
				currentPostion = pos;
				startActivity(i);
			}
		});
		
		btn_RemoveAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_REMOVE_ALL);
			}
		});

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

		public GetData() {
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogLoading = DialogLoading.Loading(activity,"Loading quotes...");
			dialogLoading.show();
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			listData = dataHeper.getQuoteByFav();
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			adapter = new QuoteAdapter(getApplicationContext(), listData);
			adapter.setOnClickCheckBoxListener(new OnClickCheckBoxListener() {
				
				@Override
				public void OnClick(View v, Quote item, int pos) {
					// TODO Auto-generated method stub
					WriteLog.d(FavouristList.this.getLocalClassName(), "click");
					if (item.getIs_favourist()==0) {
						dataHeper.AddFavourites(String.valueOf(item.getId()));
						listData.get(pos).setIs_favourist(1);
					}else{
						dataHeper.DeleteFavourites(String.valueOf(item.getId()));
						listData.get(pos).setIs_favourist(0);
					}
					
//					adapter.setData(listData);
//					adapter.notifyDataSetChanged();
					new GetData().execute();
					
				}
			});
			adapter.setExpand(true);
			lv.setAdapter(adapter);
			lv.setSelection(currentPostion);
			if (listData.size()==0) {
				tv_empty.setVisibility(View.VISIBLE);
			}else{
				tv_empty.setVisibility(View.GONE);
			}
			dialogLoading.dismiss();
			super.onPostExecute(result);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_REMOVE_ALL:
			return new AlertDialog.Builder(this)
			.setMessage(R.string.confirm_remove_fav)
			.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int whichButton) {
							new RemoveAllFav().execute();
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
	
	/**
	 * get data task
	 * @author ThangTB
	 *
	 */
	private class RemoveAllFav extends AsyncTask<Object, Object, Object>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogLoading = DialogLoading.Loading(activity,"Removing favourist...");
			dialogLoading.show();
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			dataHeper.DeleteAllFavourites();
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			dialogLoading.dismiss();
			listData.clear();
			tv_empty.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
		
	}
}
