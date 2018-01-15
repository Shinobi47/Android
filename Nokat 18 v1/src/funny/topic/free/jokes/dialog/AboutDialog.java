/**
 * 
 */
package funny.topic.free.jokes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.benchaos.nokat.maroc.R;

/**
 * @author ThangTB
 *
 */
public class AboutDialog extends Dialog {

	/**
	 * @param context
	 */
	public AboutDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_window);
		
		Button btnClose = (Button)findViewById(R.id.close_btn);
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutDialog.this.dismiss();
			}
		});
	}
}
