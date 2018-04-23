package pr.kandru.movieapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Our singleton for our Volley Requests
 */
public class Singleton {
    private static Singleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private Singleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Returns instance of the class
     * @param context Application Context
     * @return Singleton Return if available or create new instance
     */
    public static synchronized Singleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Singleton(context);
        }
        return mInstance;
    }

    /**
     * Returns instance of Request Queue
     * @return Return if available or create new instance
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Add a request
     * @param req Add the URL of a request
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}