/**
 * 
 */
package funny.topic.free.jokes.activity;

import java.util.ArrayList;
import com.google.android.gms.ads.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.benchaos.nokat.maroc.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import funny.topic.free.jokes.MainActivity;
import funny.topic.free.jokes.adapter.CustomArrayAdapter;
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
public class QuoteList extends AbstractContentActivity {

	Spinner spinnerPage;
	ListView lv;
	
	ImageButton btn_expand;
	ImageButton btn_logo;
	Button btn_next;
	Button btn_pre;
	
	TextView tv_empty;
	QuoteAdapter adapter;
	DataHeper dataHeper;
	ArrayList<Quote> listData;
	private ProgressDialog dialogLoading;
	
	private boolean isSort = false;
	private int iStart =0;
	int total;
	ArrayList<String> listSpinerPage;
	
	private int currentPostion =0;
	private boolean hasGetTotal = false;
	private boolean firstStart = true; 
	 private InterstitialAd interstitial;
	 public void displayInterstitial() {
		    if (interstitial.isLoaded()) {
		      interstitial.show();
		    }
		  }
	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractActivity#getViewLayoutId()
	 */
	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.full_quotes_index;
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
	    interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId("ca-app-pub-4644082534578545/5014242913");

	    // Créez la demande d'annonce.
	    AdRequest adRequest1 = new AdRequest.Builder().build();

	    // Lancez le chargement de l'interstitiel.
	    interstitial.loadAd(adRequest1);
		super.initView();
		
		spinnerPage = (Spinner)findViewById(R.id.fqi_page_no);
		lv = (ListView)findViewById(R.id.fqi_ListView);
		btn_expand = (ImageButton)findViewById(R.id.fqi_expand_quotes_btn);
		btn_logo = (ImageButton)findViewById(R.id.actionbar_logo_btn);
		btn_next = (Button)findViewById(R.id.fqi_next_btn);
		btn_pre = (Button)findViewById(R.id.fqi_previous_btn);
	
		tv_empty = (TextView)findViewById(R.id.empty);
		
		dataHeper = new DataHeper(getApplicationContext());
		listData = new ArrayList<Quote>();
		listSpinerPage = new ArrayList<String>();
		
		btn_expand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (adapter.isExpand()) {
					adapter.setExpand(false);
					adapter.notifyDataSetChanged();
					btn_expand.setImageResource(R.drawable.actionbar_ic_resize);
					btn_expand.setAdjustViewBounds(true);
				}else{
					adapter.setExpand(true);
					adapter.notifyDataSetChanged();
					btn_expand.setImageResource(R.drawable.actionbar_ic_resize_pressed);
					btn_expand.setAdjustViewBounds(true);
				}
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), QuoteView.class);
				i.putExtra(Constants.Bundle_quote, listData.get(pos));
				currentPostion = pos;
				startActivity(i);
				displayInterstitial();
			}
		});
		
		btn_logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				QuoteList.this.finish();
			
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractActivity#onResume()
	 */
	@Override
	protected void onResume() {
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
			dialogLoading = DialogLoading.Loading(activity,"Loading jokes...");
			dialogLoading.show();
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			if (!hasGetTotal) {
				total = dataHeper.getTotalQuotesNoFilter();
				if (total>0) {
					if (total>=50) {
						for (int i = 1; i <= total-50; i+=50) {
							String s = (i)+" - "+ (i+49);
							listSpinerPage.add(s);
						}
						
						String s1 = listSpinerPage.get(listSpinerPage.size()-1);
						String s2 = s1.substring(s1.indexOf("-")+1); 
						int num =Integer.parseInt(s2.trim());
						if ( num<total) {
							listSpinerPage.add((num)+" - "+ total);
						}
					}else{
						listSpinerPage.add(1+" - "+ total);
					}
					
				}
				hasGetTotal = true;
			}
			
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			if (total>0) {
				new GetQuoteData(iStart, null).execute();
				if (firstStart) {
					tv_empty.setVisibility(View.GONE);
					//list
					CustomArrayAdapter spinnerAdapter = new CustomArrayAdapter(getApplicationContext(), 
							R.layout.spinner_item_style, 
							listSpinerPage);
					spinnerPage.setAdapter(spinnerAdapter);
					spinnerPage.setOnItemSelectedListener(onItemSelectedListener);
				}
				
			}else{
				tv_empty.setVisibility(View.VISIBLE);
				dialogLoading.dismiss();
			}
			
			firstStart = false;
			super.onPostExecute(result);
		}
	}
	
	OnItemSelectedListener onItemSelectedListener =new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long id) {
			// TODO Auto-generated method stub
			String sStart = listSpinerPage.get(pos);
			iStart = Integer.parseInt(sStart.substring(0, sStart.indexOf("-")-1).trim());
			new GetQuoteData(iStart, null).execute();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	OnItemSelectedListener onSpinnerSortItemSelectedListener =new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long id) {
			// TODO Auto-generated method stub
			String sStart;
			if (!isSort) {
				isSort =true;
				return;
			}else{
				sStart = listSpinerPage.get(spinnerPage.getSelectedItemPosition());
				iStart = Integer.parseInt(sStart.substring(0, sStart.indexOf("-")-1).trim());
				new GetQuoteData(iStart, null).execute();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	
	/**
	 * get data task
	 * @author ThangTB
	 *
	 */
	private class GetQuoteData extends AsyncTask<Object, Object, Object>{
		private int start;
		private String sort;
		public GetQuoteData(int start, String sort) {
			this.start = start;
			this.sort = sort;
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (null == dialogLoading) {
				dialogLoading = DialogLoading.Loading(activity,"Loading jokes...");
				dialogLoading.show();
			}else if (!dialogLoading.isShowing()) {
				dialogLoading = DialogLoading.Loading(activity,"Loading jokes...");
				dialogLoading.show();
			}
			
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			listData = dataHeper.getQuoteByLimit(start, 50, sort);
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
					WriteLog.d(QuoteList.this.getLocalClassName(), "click");
					if (item.getIs_favourist()==0) {
						dataHeper.AddFavourites(String.valueOf(item.getId()));
						listData.get(pos).setIs_favourist(1);
					}else{
						dataHeper.DeleteFavourites(String.valueOf(item.getId()));
						listData.get(pos).setIs_favourist(0);
					}
					
					adapter.setData(listData);
					adapter.notifyDataSetChanged();
					
				}
			});
			if (listData.size()==0) {
				tv_empty.setVisibility(View.VISIBLE);
			}else{
				tv_empty.setVisibility(View.GONE);
			}
			lv.setAdapter(adapter);
			lv.setSelection(currentPostion);
			currentPostion =0;
			if (dialogLoading.isShowing()) {
				dialogLoading.dismiss();
			}
			
			super.onPostExecute(result);
		}
	}
}
