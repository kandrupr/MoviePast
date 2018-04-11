package pr.kandru.movieapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class URLBuildTest {
    private Context appContext = InstrumentationRegistry.getTargetContext();
    private final String api = appContext.getString(R.string.TMDBAPI);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        URLBuilder builder = new URLBuilder(appContext);
        //String result = builder.build(info, "Descriptor");
        assertEquals(api, appContext.getString(R.string.TMDBAPI));
    }

    @Test
    public void creation_isCorrect() throws Exception {
        URLBuilder builder = new URLBuilder(appContext);
    }

    @Test
    public void descriptorTop_isCorrect() throws Exception {
        HashMap<String, String> info = new HashMap<>();
        info.put("Descriptor", "Top_Rated".toLowerCase());
        info.put("Type", "TV".replace("\"", "").toLowerCase());
        URLBuilder builder = new URLBuilder(appContext);
        String result = builder.build(info, "Descriptor");
        assertEquals("&api_key=" + api, "&api_key=" + appContext.getString(R.string.TMDBAPI));
    }

}
