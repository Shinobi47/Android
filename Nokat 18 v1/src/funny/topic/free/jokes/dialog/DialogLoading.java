/**
 * 
 */
package funny.topic.free.jokes.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

import funny.topic.free.jokes.utils.WriteLog;


/**
 * @author ThangTB
 *
 */
public class DialogLoading {

	
	public static ProgressDialog Loading(Context context, String mess){
		
		ProgressDialog dialogdownload = new ProgressDialog(context);
	 	dialogdownload.setMessage(mess);
	 	dialogdownload.setIndeterminate(true);
	 	dialogdownload.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(event.getAction()==KeyEvent.ACTION_DOWN)
				{
					if (keyCode == KeyEvent.KEYCODE_BACK) 
					{
						WriteLog.d("ThangTB", "back action in dialog");
						return true;
					}	
					if(keyCode == KeyEvent.KEYCODE_MENU)
					{
						return true;
					}
				}
				return false;
			}
		});
       return dialogdownload;
       
		
	}

}
