package pr.kandru.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

/**
 * Created by pkkan on 3/28/2018.
 */

public class LoadingTextRequest extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        String query = getIntent().getStringExtra("QUERY");

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(query);
        new TextRequest(getApplicationContext(), getString(R.string.DialogFlowAPI)).execute(aiRequest);
    }

    private class TextRequest extends AsyncTask<AIRequest, Void, AIResponse> {

        AIConfiguration config;

        AIDataService aiDataService;

        public TextRequest(Context c, String apikey) {
            config = new AIConfiguration(apikey,
                    AIConfiguration.SupportedLanguages.English,
                    AIConfiguration.RecognitionEngine.System);

            aiDataService = new AIDataService(c, config);
        }

        @Override
        protected AIResponse doInBackground(AIRequest... requests) {
            final AIRequest request = requests[0];
            try {
                final AIResponse response = aiDataService.request(request);
                return response;
            } catch (AIServiceException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            if (aiResponse != null) {
                Log.e("TextConnection", aiResponse.toString());
                Intent intent = new Intent(getBaseContext(), ResultsActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
