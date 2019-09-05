package pr.kandru.movieapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * A Custom EditText class to handle user interactions with an EditText
 */
public class SearchEditText extends android.support.v7.widget.AppCompatEditText {
    private ConstraintLayout searchBar, searchButtons;  // Different layouts on the layout that has the EditText
    private TextView searchText;
    private SearchEditText inputText;
    private final Context context;
    private final InputMethodManager imm; // Text Input Manager

    /**
     * Constructor
     * @param context Application Context
     * @param attrs EditText attributes
     */
    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context.getApplicationContext();
        imm = this.context.getSystemService(InputMethodManager.class);
    }

    /**
     * Initially, view will still be building. Set siblings of EditText at a later time
     * @param v Entire Layout View
     */
    public void setSiblings(View v) {
        searchBar = v.findViewById(R.id.search_bar_layout);
        searchButtons = v.findViewById(R.id.search_button_layout);
        searchText = v.findViewById(R.id.textSearch);
        inputText = v.findViewById(R.id.textInput);
    }

    /**
     * Handles what to do when a user doesn't search but simply presses back
     * @param keyCode Integer
     * @param event Keyboard Event
     * @return Boolean True to handle custom event
     */
    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event) {
        if(this.getVisibility() == VISIBLE) {
            searchButtons.setVisibility(VISIBLE);
            searchText.setVisibility(VISIBLE);
            searchBar.setVisibility(INVISIBLE);
            inputText.setText("");
            if(imm != null)     // Hide keyboard
                imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
        return true;
    }

    /**
     * When a user presses enter on a text search
     * @param actionCode Integer What the user does
     */
    @Override
    public void onEditorAction(int actionCode) {
        if(actionCode == EditorInfo.IME_ACTION_SEARCH) {    // User presses search
            final String input = this.getText().toString();
            if(!input.isEmpty()){   // Not empty
                searchButtons.setVisibility(VISIBLE);   // Set everything back to normal so it looks the same on return
                searchText.setVisibility(VISIBLE);
                searchBar.setVisibility(INVISIBLE);
                inputText.setText("");
//
                if(imm != null)     // Hide keyboard
                    imm.hideSoftInputFromWindow(this.getWindowToken(), 0);

                // Get text input and build URL
                final String type = ((TextView)searchBar.findViewById(R.id.textType)).getText().toString().toLowerCase();
                String[] fields = {input, type};
                String url = URLBuilder.getInstance(context).buildFromTitle(fields);

                // NEW ACTIVITY INPUT, URL, TYPE
                Intent intent = new Intent(context, LoadingAPIRequest.class);
                intent.putExtra("QUERY", input);
                intent.putExtra("URL", url);
                intent.putExtra("TYPE", type);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        }
    }

    /**
     * Handles what happens when someone types
     * @param text CharSequence The current text
     * @param start Integer
     * @param lengthBefore Integer Length of string before
     * @param lengthAfter Integer Length of string after
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if(searchBar != null) {     // If the text input is not available
            if (lengthAfter == 0)   // No text to text to show clear button
                searchBar.findViewById(R.id.cancelImage).setVisibility(INVISIBLE);
            else if (lengthBefore != 1 && lengthAfter == 1) // Length is 0 or greater than one, and only has 1 character
                // This check ensures that we are not constantly making it visible but doing it once
                searchBar.findViewById(R.id.cancelImage).setVisibility(VISIBLE);
        }
    }
}
