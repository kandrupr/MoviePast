package pr.kandru.movieapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Custom Dialog Fragment for an actor's image
 */
public class ImageDialog extends DialogFragment {
    private String url;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Set Image URL
     * @param url String Poster URL
     */
    public void setURL(String url) {
        this.url = url.replace("92", "185");    // Get larger image to display
    }

    /**
     * Create the dialog view
     * @param inflater LayoutInflater
     * @param container Dialog Container
     * @param savedInstanceState Bundle
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actor_image_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final ProgressBar progress = view.findViewById(R.id.progressLoader);
        final ImageView image = view.findViewById(R.id.resultImage);
        final ImageView cancel = view.findViewById(R.id.cancelImage);
        Picasso.with(view.getContext())
            .load(url)
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
            .into(image,  new Callback() {
                @Override
                public void onSuccess() {
                    if (progress != null) {
                        progress.setVisibility(View.GONE);   // Hide progressbar
                        image.setVisibility(View.VISIBLE);   // Show Image
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                            }
                        });
                    }
                }

                @Override
                public void onError() {
                    getDialog().cancel();
                }
            });
        return view;
    }

    /**
     * After the View is created
     */
    @Override
    public void onResume() {
        super.onResume();
        // Set new scale factors
        int width = (int) (getResources().getDisplayMetrics().widthPixels/1.15);
        int height = (int) (getResources().getDisplayMetrics().heightPixels/1.5);
        getDialog().getWindow().setLayout(width, height);
    }
}
