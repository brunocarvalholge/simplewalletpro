package br.com.tolive.simplewalletpro.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

<<<<<<< HEAD
import br.com.tolive.simplewalletpro.R;

public class GraphView extends View {

    private Paint red;
    private Paint yellow;
    private Paint green;
    RectF rectRed;
    RectF rectYellow;
    RectF rectGreen;
=======
import java.util.ArrayList;

import br.com.tolive.simplewalletpro.R;

public class GraphView extends View {
    private static final int PADDING = 50;

    private ArrayList<Paint> mColors;
    RectF rect;
>>>>>>> 7dacfe87cd95cb22be7f3409f617af625c8d3778

    public GraphView(Context context) {
        this(context, null);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(context.getResources().getColor(R.color.snow));

<<<<<<< HEAD
        red = new Paint();
        red.setColor(getResources().getColor(R.color.red));

        yellow = new Paint();
        yellow.setColor(getResources().getColor(R.color.yellow));

        green = new Paint();
        green.setColor(getResources().getColor(R.color.green));

        //rectRed = new RectF(0, 0, getLayoutParams().width, getLayoutParams().height);
        //rectYellow = new RectF(0, 0, getLayoutParams().width, getLayoutParams().height);
        //rectGreen = new RectF(0, 0, getLayoutParams().width, getLayoutParams().height);
=======
        ArrayList<Paint> mColors = new ArrayList<Paint>();

        rect = new RectF();
>>>>>>> 7dacfe87cd95cb22be7f3409f617af625c8d3778

        setFocusable(true);
    }

<<<<<<< HEAD
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(right < bottom) {
            rectRed = new RectF(left+50, top+50, right-50, right-50);
            rectYellow = new RectF(left+50, top+25, right-50, right-75);
        } else {
            rectRed = new RectF(left, top, bottom, bottom);
            rectYellow = new RectF(left, top, bottom, bottom);
=======
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
>>>>>>> 7dacfe87cd95cb22be7f3409f617af625c8d3778
        }
        //rectGreen = new RectF(left, top, right, bottom);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
<<<<<<< HEAD
        canvas.drawArc(rectRed, 0, 90, true, red);
        canvas.drawArc(rectRed, 90, 90, true, yellow);
        canvas.drawArc(rectYellow, 180, 180, true, green);
=======
        if(mColors != null) {
            int size = mColors.size();
            for (int i = 0; i < size; i++) {
                canvas.drawArc(rect, 0 + 90 * i, 90, true, mColors.get(i));
            }
        }
>>>>>>> 7dacfe87cd95cb22be7f3409f617af625c8d3778
        //canvas.drawCircle(100, 110, 20, red);
    }
}