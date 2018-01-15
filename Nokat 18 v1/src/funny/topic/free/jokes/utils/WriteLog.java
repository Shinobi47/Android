package funny.topic.free.jokes.utils;

import android.os.Debug;
import android.util.Log;

public class WriteLog {
	private static boolean debug = true;
	public static void d(String tag, String msg) {
		if (!debug)
			return;
		Log.d(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (!debug)
			return;
		Log.e(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (!debug)
			return;
		Log.i(tag, msg);
	} 

	public static void v(String tag, String msg) {
		if (!debug)
			return;
		Log.v(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (!debug)
			return;
		Log.w(tag, msg);
	}
	/**
	 * TrungTT
	 * @param str
	 */
	public static void LogMem(String str){
		int usedMegs2 = (int) (Debug.getNativeHeapAllocatedSize() / 1048576L);
		int useMemKB = (int) (Debug.getNativeHeapAllocatedSize() / 1024L);
		Log.e("TrungTT", str+ " memory :  " + usedMegs2  + "("+ useMemKB + " KB)");
	}
}
