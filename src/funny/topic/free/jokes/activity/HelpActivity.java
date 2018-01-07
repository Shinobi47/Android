/**
 * 
 */
package funny.topic.free.jokes.activity;

import com.benchaos.nokat.maroc.R;

/**
 * @author ThangTB
 *
 */
public class HelpActivity extends AbstractContentActivity {

	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractActivity#getViewLayoutId()
	 */
	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.help;
	}

	/* (non-Javadoc)
	 * @see com.android.jokesapp.jokes2.activity.AbstractContentActivity#initView()
	 */
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		HelpActivity.this.finish();
		super.onBackPressed();
	}
}
