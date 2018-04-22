package pr.kandru.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by pkkan on 4/21/2018.
 */

public class SearchEditText extends android.support.v7.widget.AppCompatEditText {
    private ConstraintLayout searchBar, searchButtons;
    private TextView searchText;
    private SearchEditText inputText;
    private Context context;
    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context.getApplicationContext();
    }

    public void setSiblings(View v) {
        searchBar = v.findViewById(R.id.search_bar_layout);
        searchButtons = v.findViewById(R.id.search_button_layout);
        searchText = v.findViewById(R.id.textSearch);
        inputText = v.findViewById(R.id.textInput);
    }

    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event) {
        if(this.getVisibility() == VISIBLE) {
            searchButtons.setVisibility(VISIBLE);
            searchText.setVisibility(VISIBLE);
            searchBar.setVisibility(INVISIBLE);
            inputText.setText("");
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
            }
        }
        return true;
    }

    @Override
    public void onEditorAction(int actionCode) {
        if(actionCode == EditorInfo.IME_ACTION_SEARCH) {
            final String input = this.getText().toString();
            if(!input.isEmpty()){
                searchButtons.setVisibility(VISIBLE);
                searchText.setVisibility(VISIBLE);
                searchBar.setVisibility(INVISIBLE);
                inputText.setText("");

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                }

                final String type = ((TextView)searchBar.findViewById(R.id.textType)).getText().toString().toLowerCase();
                HashMap<String,String> params = new HashMap<String, String>(){{
                        put("Type", type);
                        put("Title", input);
                }};
                String url = URLBuilder.getInstance(context).buildFromTitle(params);
                // NEW ACTIVITY URL, INPUT, TYPE
                Intent intent = new Intent(context, LoadingAPIRequest.class);
                intent.putExtra("QUERY", input);
                intent.putExtra("URL", url);
                intent.putExtra("TYPE", type);
                context.startActivity(intent);
            }
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if(searchBar != null) {
            if (lengthAfter == 0) {
                searchBar.findViewById(R.id.cancelImage).setVisibility(INVISIBLE);
            } else if (lengthBefore != 1 && lengthAfter == 1) {
                searchBar.findViewById(R.id.cancelImage).setVisibility(VISIBLE);
            }
        }
    }
}
