package funny.topic.free.jokes.activity;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

// TODO: Auto-generated Javadoc
/**
 * Abstract Activity.
 *
 * @author ThangTB
 */
public abstract class AbstractActivity extends Activity{
	
	/**
	 * The listener interface for receiving activityStateChanged events.
	 * The class that is interested in processing a activityStateChanged
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addActivityStateChangedListener<code> method. When
	 * the activityStateChanged event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ActivityStateChangedEvent
	 */
	public static interface ActivityStateChangedListener {
		
		/**
		 * On changed.
		 *
		 * @param activity the activity
		 */
		public void onChanged(AbstractActivity activity);
	}

	/**
	 * OnResume event listener for the activity.
	 */
	private Set<ActivityStateChangedListener> onResumeListeners = new HashSet<AbstractActivity.ActivityStateChangedListener>();

	/**
	 * OnPause event listener for the activity.
	 */
	private Set<ActivityStateChangedListener> onPauseListeners = new HashSet<AbstractActivity.ActivityStateChangedListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// You initialize the view.
		setContentView(getViewLayoutId());
		initView();

		// Add to the history of activity management.
		ActivityHistoryManager.addNewActivity(this);

	}

	/**
	 * Gets the resource ID of the view layout.
	 * @return Resource ID.
	 */
	protected abstract int getViewLayoutId();

	/**
	 * You initialize the view.
	 */
	protected void initView() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();

		for (ActivityStateChangedListener listener : onPauseListeners) {
			listener.onChanged(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		for (ActivityStateChangedListener listener : onResumeListeners) {
			listener.onChanged(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Removed from the management activities explicitly.
		
		
		ActivityHistoryManager.removeFromActivityHistory(this);
	}

	/**
	 * Adds the on resume event listener.
	 *
	 * @param addListener the add listener
	 */
	public void addOnResumeEventListener(ActivityStateChangedListener addListener) {
		this.onResumeListeners.add(addListener);
	}

	/**
	 * Removes the on resume event listener.
	 *
	 * @param removeListener the remove listener
	 */
	public void removeOnResumeEventListener(ActivityStateChangedListener removeListener) {
		this.onResumeListeners.remove(removeListener);
	}

	/**
	 * Adds the on pause event listener.
	 *
	 * @param addListener the add listener
	 */
	public void addOnPauseEventListener(ActivityStateChangedListener addListener) {
		this.onPauseListeners.add(addListener);
	}

	/**
	 * Removes the on pause event listener.
	 *
	 * @param removeListener the remove listener
	 */
	public void removeOnPauseEventListener(ActivityStateChangedListener removeListener) {
		this.onPauseListeners.remove(removeListener);
	}

}
