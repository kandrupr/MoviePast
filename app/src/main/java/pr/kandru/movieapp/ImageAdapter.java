package pr.kandru.movieapp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Adapter for Image item in an Actor RequestType in InfoActivity
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder> {
    private Context context;
    private List<String> images;
    private onItemClicked onClick;

    /**
     * Click Interface, Pass URL as string
     */
    public interface onItemClicked {
        void onItemClick(String url);
    }

    /**
     * Constructor
     * @param context Application Context
     * @param images A list of Image URLs used for actors
     */
    public ImageAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    /**
     * Creates Holder Item to Parent RecyclerView
     * @param parent ViewGroup The RecyclerView which holds all of the Holder items
     * @param viewType Integer The position in the Adapter data
     * @return Holder object
     */
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) layout.getLayoutParams();
        lp.width = parent.getMeasuredWidth() / 3;       // Get exactly 1/3 to get 3 results on a page
        layout.setLayoutParams(lp);
        return new ImageAdapter.Holder(layout);
    }

    /**
     * Bind the item to view
     * @param holder Holder being and individual image
     * @param position Integer The position in the RecyclerView
     */
    @Override
    public void onBindViewHolder(final ImageAdapter.Holder holder, int position) {
        final int pos = position;
        if(images.get(position).equals("blank")) {  // No Image available, show error text
            holder.progress.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(R.string.error);
            holder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        } else {
            Picasso.with(context)       // Load the image
                    .load(images.get(position))
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                    .into(holder.image,  new Callback() {
                        @Override
                        public void onSuccess() {   // Image successfully loaded, show image
                            if (holder.progress != null) {
                                holder.text.setVisibility(View.GONE);
                                holder.progress.setVisibility(View.GONE);
                                holder.image.setVisibility(View.VISIBLE);
                                holder.image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onClick.onItemClick(images.get(pos));  // Image URL
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError() {     // Image failed to load, show error text
                            if (holder.progress != null) {
                                holder.progress.setVisibility(View.GONE);
                                holder.image.setVisibility(View.GONE);
                                holder.text.setVisibility(View.VISIBLE);
                                holder.text.setText(R.string.error);
                                holder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                            }
                        }
                    });
        }
    }

    /**
     * Number of items in our view
     * @return Integer number of items
     */
    @Override
    public int getItemCount() {
        return images.size();
    }

    /**
     * Set Adapater to recognize onClick event
     * @param onClick On Click Item
     */
    public void setOnClick(onItemClicked onClick) {
        this.onClick=onClick;
    }


    /**
     * Individual Items in our RecyclerView
     */
    public static class Holder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        final ProgressBar progress; // A progress spinner

        public Holder (View view) {
            super(view);
            image = itemView.findViewById(R.id.resultImage);
            text = itemView.findViewById(R.id.resultText);
            progress = itemView.findViewById(R.id.progressLoader);
        }
    }
}
