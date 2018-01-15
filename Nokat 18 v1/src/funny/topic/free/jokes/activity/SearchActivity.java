/**
 * 
 */
package funny.topic.free.jokes.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.benchaos.nokat.maroc.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
public class SearchActivity extends AbstractContentActivity{

	private ImageButton btn_logo;
	private ImageButton btn_search;
	
	private EditText ed_searchText;
	private ListView lv;
	private TextView tv_empty;
	
	private DataHeper dataHeper;
	private QuoteAdapter adapter;
	private ArrayList<Quote> listData;
	
	private ProgressDialog dialogLoading;
	private boolean isQuotes = true;
	
	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractActivity#getViewLayoutId()
	 */
	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.search_index;
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
		
		btn_logo = (ImageButton)findViewById(R.id.actionbar_logo_btn);
		tv_empty = (TextView)findViewById(R.id.empty);
		lv = (ListView)findViewById(R.id.list);
		ed_searchText = (EditText)findViewById(R.id.si_search_box);
		btn_search = (ImageButton)findViewById(R.id.actionbar_search_btn);
		isQuotes = true;		
		ed_searchText.setText("");
		btn_logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				SearchActivity.this.finish();
			}
		});
		
		listData = new ArrayList<Quote>();
		dataHeper = new DataHeper(getApplicationContext());
		
		ed_searchText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				WriteLog.d("ThangTB", "keyCode =" +actionId);
				if (actionId == 6) {
					new GetdataSearchQuotes(ed_searchText.getText().toString().trim()).execute();
				}
				return false;
			}
		});
		
		ed_searchText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				WriteLog.d("ThangTB", "keyCode =" +keyCode);
				return false;
			}
		});
		
		btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(
					    INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				// TODO Auto-generated method stub
					new GetdataSearchQuotes(ed_searchText.getText().toString().trim()).execute();
			}
		});
		
		lv.setOnItemClickListener(onItemClickListener);
		
		
		
	}
	
	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int pos,
				long arg3) {
			// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), QuoteView.class);
				i.putExtra(Constants.Bundle_quote, listData.get(pos));
				startActivity(i);
		}
	};
	
	private class GetdataSearchQuotes extends AsyncTask<Object, Object, Object>{

		private String sSearch;
		/**
		 * 
		 */
		public GetdataSearchQuotes(String s) {
			this.sSearch = s;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialogLoading = DialogLoading.Loading(activity,"Loading jokes...");
			dialogLoading.show();
			super.onPreExecute();
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			listData = dataHeper.getSearchQuoteByString(sSearch);
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			adapter = new QuoteAdapter(getApplicationContext(), listData);
			adapter.setOnClickCheckBoxListener(new OnClickCheckBoxListener() {
				
				@Override
				public void OnClick(View v, Quote item, int pos) {
					// TODO Auto-generated method stub
					WriteLog.d(SearchActivity.this.getLocalClassName(), "click");
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
			
			dialogLoading.dismiss();
			super.onPostExecute(result);
		}
		
	}

}
