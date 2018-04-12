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
        assertEquals("?api_key=" + api, "?api_key=" + appContext.getString(R.string.TMDBAPI));
    }

    @Test
    public void descriptorByYear_isInvalid() throws Exception {
        HashMap<String, String> info = new HashMap<>();
        info.put("Descriptor", "Top_Rated".toLowerCase());
        info.put("Year", "2019");
        info.put("Type", "TV".replace("\"", "").toLowerCase());
        URLBuilder builder = new URLBuilder(appContext);
        String result = builder.buildDescriptorByYear(info);
        assertEquals(result, "invalid");
    }

    @Test
    public void descriptorByYear_isValid() throws Exception {
        HashMap<String, String> info = new HashMap<>();
        info = new HashMap<>();
        info.put("Descriptor", "Top_Rated".toLowerCase());
        info.put("Year", "2018");
        info.put("Type", "TV".replace("\"", "").toLowerCase());
        URLBuilder builder = new URLBuilder(appContext);
        String result = builder.buildDescriptorByYear(info);
        assertEquals(result, "https://api.themoviedb.org/3/discover/tv?api_key=" + api + "&sort_by=vote_average.desc&air_date.gte=2018-1-1&air_date.lte=2018-12-31&vote_count.gte=50&with_original_language=en&language=en-US&page=1");
    }

    @Test
    public void movie_isValid() throws Exception {
        HashMap<String, String> info = new HashMap<>();
        info = new HashMap<>();
        info.put("Title", "Black Panther");
        URLBuilder builder = new URLBuilder(appContext);
        String result = builder.buildMovie(info);
        assertEquals(result, "https://api.themoviedb.org/3/search/movie?api_key=" + api + "&region=US&query=Black%20Panther&language=en-US&page=1");
    }

    @Test
    public void movieGenre_isValid() throws Exception {
        HashMap<String, String> info = new HashMap<>();
        info = new HashMap<>();
        info.put("MovieGenre", "Animation");
        URLBuilder builder = new URLBuilder(appContext);
        String result = builder.buildMovieGenre(info);
        assertEquals(result, "https://api.themoviedb.org/3/discover/movie?api_key=" + api + "&sort_by=revenue.desc&region=US&with_genres=16&language=en-US&page=1");
    }

    @Test
    public void movieGenreByYear_isValid() throws Exception {
        HashMap<String, String> info = new HashMap<>();
        info = new HashMap<>();
        info.put("MovieGenre", "Science Fiction");
        info.put("Year", "2015");
        URLBuilder builder = new URLBuilder(appContext);
        String result = builder.buildMovieGenre(info);
        assertEquals(result, "https://api.themoviedb.org/3/discover/movie?api_key=" + api + "&sort_by=revenue.desc&region=US&with_genres=878&primary_release_year=2015&language=en-US&page=1");
    }

    @Test
    public void movieGenre_isInvalid() throws Exception {
        HashMap<String, String> info = new HashMap<>();
        info = new HashMap<>();
        info.put("MovieGenre", "Science Fiction");
        info.put("Year", "2019");
        info.put("Type", "TV".replace("\"", "").toLowerCase());
        URLBuilder builder = new URLBuilder(appContext);
        String result = builder.buildMovieGenre(info);
        assertEquals(result, "invalid");
    }
}
