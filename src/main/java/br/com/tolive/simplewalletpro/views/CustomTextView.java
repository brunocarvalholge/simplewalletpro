package br.com.tolive.simplewalletpro.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import br.com.tolive.simplewalletpro.R;

/**
 * Custom TextView to use Roboto font
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String font = a.getString(R.styleable.CustomTextView_font);
            if (font!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + font);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
