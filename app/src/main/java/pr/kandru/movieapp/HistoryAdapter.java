package pr.kandru.movieapp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Search History
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private Context context;
    public HistoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) layout.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 3;         // Get exactly 1/3 to get 3 results in a row
        layout.setLayoutParams(lp);

        return new HistoryHolder(layout);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        public HistoryHolder (View view) {
            super(view);
            image = itemView.findViewById(R.id.resultImage);
            text = itemView.findViewById(R.id.resultText);
        }
    }
}
