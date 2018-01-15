/**
 * 
 */
package funny.topic.free.jokes.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.benchaos.nokat.maroc.R;
import funny.topic.free.jokes.db.entity.Quote;
import funny.topic.free.jokes.utils.WriteLog;

/**
 * @author ThangTB
 *
 */
public class FavQuoteAdapter extends BaseAdapter {

	private ArrayList<Quote> data;
	private Context mContext;
	private LayoutInflater l_Inflater;
	private boolean isExpand = false; 
	
	public FavQuoteAdapter(Context context, ArrayList<Quote> data) {
		this.data = data;
		this.mContext = context;
		l_Inflater = LayoutInflater.from(context);
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return data.get(pos);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		final Quote quote;
		quote = data.get(position);
		
			if (convertView == null) {
				convertView = l_Inflater.inflate(R.layout.quote_index_body, null);
				holder = new ViewHolder();
				
				holder.quote = (TextView)convertView.findViewById(R.id.qi_content);
				holder.fav = (CheckBox)convertView.findViewById(R.id.qi_fav_cbx);
				holder.qi_author = (TextView)convertView.findViewById(R.id.qi_author);
				convertView.setTag(holder);
			} 
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.qi_author.setVisibility(View.GONE);
			
			if (quote.getIs_favourist()==1) {
				holder.fav.setChecked(true);
			}else if(quote.getIs_favourist()==0){
				holder.fav.setChecked(false);
			}
			
			holder.fav.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onClickCheckBoxListener.OnClick(v, quote,position);
				}
			});
			
			WriteLog.d("ThangTB", "is expand = "+this.isExpand);
			if (!this.isExpand) {
				holder.quote.setMaxLines(2);
				holder.quote.setEllipsize(TruncateAt.END);
			}else{
				holder.quote.setMaxLines(10);
				holder.quote.setEllipsize(null);
			}
				holder.quote.setText(Html.fromHtml(quote.getBody()));
				
		return convertView;
	}
	
	static class ViewHolder{
		TextView quote;
		CheckBox fav;
		TextView qi_author;
	}

	
	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<Quote> data) {
		this.data = data;
	}
	
	/**
	 * @param isExpand the isExpand to set
	 */
	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}
	/**
	 * @return the isExpand
	 */
	public boolean isExpand() {
		return isExpand;
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#notifyDataSetChanged()
	 */
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	
	//interface Onclick checbox
	public interface OnClickCheckBoxListener{
		public void OnClick(View v, Quote item, int pos);
	}
	
	OnClickCheckBoxListener onClickCheckBoxListener;
	
	
	/**
	 * Set onclick image event
	 * @param onClickcheckboxListener
	 */
	public void setOnClickCheckBoxListener(OnClickCheckBoxListener onClickCheckBoxListener) {
		this.onClickCheckBoxListener = onClickCheckBoxListener;
	}

	
}
