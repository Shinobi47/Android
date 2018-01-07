package funny.topic.free.jokes.activity;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import android.app.Activity;

// TODO: Auto-generated Javadoc
/**
 * 
 * The stack management activities.
 *	Android In general, old activity on the activity stack is automatically destroyed, will be freed from memory.
 *	Regardless of long, short-term, OutOfMemoryError occurs when you start a large amount of activity.
 *	In this ActivityHistoryManager, to explicitly manage the activity stack, so that the memory be freed reliably.
 * 
 * @author ThangTB
 *
 */
public final class ActivityHistoryManager {

	/** The Constant TAG. */
	private final static String TAG = ActivityHistoryManager.class.getSimpleName();
	
	/** The Constant ACTIVITY_QUEUE_SIZE. */
	private final static int ACTIVITY_QUEUE_SIZE = 10;
	
	/** The Constant ACTIVITY_TIMEOUT. */
	private final static long ACTIVITY_TIMEOUT = TimeUnit.SECONDS.toMillis(60 * 60 * 2); // Timeout Activity History (2 hours).

	/**
	 * Maintains a history of activity.
	 */
	public static Queue<Activity> ActivityHistory = new ConcurrentLinkedQueue<Activity>();

	/**
	 * Holds the timing activity is operated at the end.
	 */
	private static long lastModificationTime = 0;

	/**
	 * Prohibits the instantiation.
	 */
	private ActivityHistoryManager() {
	}

	/**
	 * Add a new activity.
	 * 
	 * @param newActivity
	 *            New Activity
	 */
	public static synchronized void addNewActivity(Activity newActivity) {
		ActivityHistory.add(newActivity);
		
		if (ACTIVITY_QUEUE_SIZE < ActivityHistory.size()) {
			Activity oldestActivity = ActivityHistory.poll();
			oldestActivity.finish();
		}
//		for(Activity act:ActivityHistory){
//		}
		// To update the date and time the last operation.
		lastModificationTime = System.currentTimeMillis();
	}

	/**
	 * Explicitly remove an activity from the activity history.
	 * 
	 * @param removeActivity
	 *            Activity object to be removed.
	 */
	public static synchronized void removeFromActivityHistory(Activity removeActivity) {
		if (ActivityHistory.contains(removeActivity)) {
			ActivityHistory.remove(removeActivity);
		}

		// To update the date and time the last operation.
		lastModificationTime = System.currentTimeMillis();
	}

	/**
	 * Shut down all the activities.
	 */
	public static void shutdownAllActivity() {
		for (Activity activity : ActivityHistory) {
			activity.finish();
		}
		ActivityHistory.clear();

		// To update the date and time the last operation.
		lastModificationTime = System.currentTimeMillis();
	}

	/**
	 * Make sure that the time has not been a long time after the operation.
	 *
	 * @return true, if successful
	 */
	public static boolean confirmNoLongerOperation() {
		// Make sure that the timeout.
		long duration = System.currentTimeMillis() - lastModificationTime;
		if (duration > ACTIVITY_TIMEOUT) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns whether the activity history is empty. True if it is empty.
	 *
	 * @return true, if is empty activity history
	 */
	public static boolean isEmptyActivityHistory() {
		return null == ActivityHistory || ActivityHistory.isEmpty();
	}
}
