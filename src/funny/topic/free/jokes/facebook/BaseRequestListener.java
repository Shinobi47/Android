package funny.topic.free.jokes.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.util.Log;

import funny.topic.free.jokes.facebook.AsyncFacebookRunner.RequestListener;

/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 */
public abstract class BaseRequestListener implements RequestListener {

    @Override
    public void onFacebookError(FacebookError e, final Object state) {
        Log.e("Facebook", e.getMessage());
//        e.printStackTrace();
    }

    @Override
    public void onFileNotFoundException(FileNotFoundException e, final Object state) {
        Log.e("Facebook", e.getMessage());
//        e.printStackTrace();
    }

    @Override
    public void onIOException(IOException e, final Object state) {
        Log.e("Facebook", e.getMessage());
//        e.printStackTrace();
    }

    @Override
    public void onMalformedURLException(MalformedURLException e, final Object state) {
        Log.e("Facebook", e.getMessage());
//        e.printStackTrace();
    }

}
