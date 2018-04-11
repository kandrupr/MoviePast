package pr.kandru.movieapp;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class URLBuilderTest {
    @Test
    public void creation_isCorrect() throws Exception {
        URLBuilder builder = new URLBuilder();
    }

    public void descriptorTop_isCorrect() throws Exception {
        HashMap<String, String> info = new HashMap<>();
        info.put("Descriptor", "Top_Rated".toLowerCase());
        info.put("Type", "TV".replace("\"", "").toLowerCase());
        URLBuilder builder = new URLBuilder();
        String result = builder.build(info, "Descriptor");
        assertEquals(result, "https://api.themoviedb.org/3/tv/top_rated?api_key" + R.string.TMDBAPI + "&language=en-US&page=1");
    }

    public void descriptorByYear_isCorrect() throws Exception {
        URLBuilder builder = new URLBuilder();
    }
}