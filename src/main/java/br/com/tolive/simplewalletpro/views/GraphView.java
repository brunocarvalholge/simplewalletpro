package br.com.tolive.simplewalletpro.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.model.Category;

public class GraphView extends View {
    private static final int PADDING = 50;
    public static final int INIT_ANG = 0;

    private ArrayList<Category> mCategories;
    private ArrayList<Paint> mColors;
    private ArrayList<Float> mPercents;
    private TextView mEmptyGraph;
    RectF rect;

    public GraphView(Context context) {
        this(context, null);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(context.getResources().getColor(R.color.snow));

        ArrayList<Category> mCategories = new ArrayList<Category>();

        rect = new RectF();

        setFocusable(true);
    }

    public void setCategories(ArrayList<Category> categories) {
        this.mCategories = categories;
        this.mColors = new ArrayList<Paint>();
        Resources resources = getResources();
        TypedArray colors = resources.obtainTypedArray(R.array.categoryColors);
        for(Category category : mCategories){
            Paint paint = new Paint();
            paint.setColor(resources.getColor(colors.getResourceId(category.getColor(), resources.getColor(R.color.gray))));
            mColors.add(paint);
        }
        colors.recycle();
        this.invalidate();
    }

    public void setPercents(ArrayList<Float> percents) {
        this.mPercents = percents;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(changed) {
            View view = getRootView();
            if(view != null) {
                int extraPadding;
                if (right < bottom) {
                    extraPadding = ((this.getBottom() - this.getTop()) - (this.getRight() - this.getLeft()))/2;
                    rect.set(this.getLeft() + PADDING, this.getTop() + PADDING + extraPadding, this.getRight() - PADDING, this.getRight() - PADDING + extraPadding);
                } else {
                    extraPadding = ((this.getRight() - this.getLeft()) - (this.getBottom() - this.getTop()))/2;
                    rect.set(this.getLeft() + PADDING + extraPadding, this.getTop() + PADDING, this.getBottom() - PADDING + extraPadding, this.getBottom() - PADDING);
                }
                //rect.set(this.getLeft() + PADDING, this.getTop() + PADDING, this.getRight() - PADDING, this.getBottom() - PADDING);
            }
        }
        //super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            int size = mColors.size();
            Float total = mPercents.get(mPercents.size()-1);
            Float startAng = 0f;
            for (int i = 0; i < size; i++) {
                Float sweepAng = (mPercents.get(i) * 360) / total;
                canvas.drawArc(rect, INIT_ANG + startAng, sweepAng, true, mColors.get(i));
                startAng += sweepAng;
            }
        } catch (NullPointerException e){
            throw new RuntimeException(e);
        }
    }
}