package br.com.tolive.simplewalletpro.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import br.com.tolive.simplewalletpro.R;

public class GraphView extends View {
    private static final int PADDING = 50;

    private ArrayList<Paint> mColors;
    RectF rect;

    public GraphView(Context context) {
        this(context, null);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(context.getResources().getColor(R.color.snow));

        ArrayList<Paint> mColors = new ArrayList<Paint>();

        rect = new RectF();

        setFocusable(true);
    }

    public void setColors(ArrayList<Paint> mColors) {
        this.mColors = mColors;
        this.invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(changed) {
            View view = getRootView();
            if(view != null) {
                if (right < bottom) {
                    rect.set(left + PADDING, top + PADDING, right - PADDING, right - PADDING);
                } else {
                    rect.set(left + PADDING, top + PADDING, bottom + PADDING, bottom + PADDING);
                }
            }
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mColors != null) {
            int size = mColors.size();
            for (int i = 0; i < size; i++) {
                canvas.drawArc(rect, 0 + 90 * i, 90, true, mColors.get(i));
            }
        }
    }
}