package funny.topic.free.jokes.facebook;

import funny.topic.free.jokes.facebook.Facebook.DialogListener;


/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 */
public abstract class BaseDialogListener implements DialogListener {

    @Override
    public void onFacebookError(FacebookError e) {
        e.printStackTrace();
    }

    @Override
    public void onError(DialogError e) {
        e.printStackTrace();
    }

    @Override
    public void onCancel() {
    }

}