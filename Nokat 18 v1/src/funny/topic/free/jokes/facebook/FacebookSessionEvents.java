/**
 * 
 */
package funny.topic.free.jokes.facebook;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author d
 * 
 */
public class FacebookSessionEvents {
	private static LinkedList<AuthListener> mAuthListeners = new LinkedList();
	private static LinkedList<LogoutListener> mLogoutListeners = new LinkedList();

	public static void addAuthListener(AuthListener paramAuthListener) {
		boolean bool = mAuthListeners.add(paramAuthListener);
	}

	public static void addLogoutListener(LogoutListener paramLogoutListener) {
		boolean bool = mLogoutListeners.add(paramLogoutListener);
	}

	public static void onLoginError(String paramString) {
		Iterator localIterator = mAuthListeners.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return;
			((AuthListener) localIterator.next()).onAuthFail(paramString);
		}
	}

	public static void onLoginSuccess() {
		Iterator localIterator = mAuthListeners.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return;
			((AuthListener) localIterator.next()).onAuthSucceed();
		}
	}

	public static void onLogoutBegin() {
		Iterator localIterator = mLogoutListeners.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return;
			((LogoutListener) localIterator.next()).onLogoutBegin();
		}
	}

	public static void onLogoutFinish() {
		Iterator localIterator = mLogoutListeners.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return;
			((LogoutListener) localIterator.next()).onLogoutFinish();
		}
	}

	public static void removeAuthListener(AuthListener paramAuthListener) {
		boolean bool = mAuthListeners.remove(paramAuthListener);
	}

	public static void removeLogoutListener(LogoutListener paramLogoutListener) {
		boolean bool = mLogoutListeners.remove(paramLogoutListener);
	}

	public abstract interface AuthListener {
		public abstract void onAuthFail(String paramString);

		public abstract void onAuthSucceed();
	}

	public abstract interface LogoutListener {
		public abstract void onLogoutBegin();

		public abstract void onLogoutFinish();
	}
}
