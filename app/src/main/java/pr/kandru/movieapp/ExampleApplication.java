package pr.kandru.movieapp;


import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by pkkan on 4/20/2018.
 * Leak Canary class to look for memory leaks
 */
public class ExampleApplication extends Application {
    private static ExampleApplication instance ;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        refWatcher = LeakCanary.install(this);
    }

    public void mustDie(Object object) {
        if (refWatcher != null) {
            refWatcher.watch(object);
        }
    }
}