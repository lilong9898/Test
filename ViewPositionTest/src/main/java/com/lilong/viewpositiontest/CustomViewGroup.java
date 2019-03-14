package com.lilong.viewpositiontest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import static com.lilong.viewpositiontest.MainActivity.TAG;

public class CustomViewGroup extends RelativeLayout {

    private Context context;
    private Paint paint;
    private String name;

    /**
     * 是否在{@link #onDraw(Canvas)}中平移canvas
     */
    private boolean translateCanvas = false;

    /**
     * 子View是否已经添加过了
     * */
    private boolean childViewAddedAlready = false;

    public CustomViewGroup(Context context) {
        super(context);
        init(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        name = getResources().getResourceEntryName(getId());
        setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(80);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, name + "is clicked");
            }
        });
    }

    public void setTranslateCanvas(boolean translateCanvas) {
        this.translateCanvas = translateCanvas;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!childViewAddedAlready){
            View v = new View(context);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(getMeasuredWidth() / 3, getMeasuredHeight() / 3);
            lp.addRule(CENTER_IN_PARENT);
            v.setLayoutParams(lp);
            v.setBackgroundColor(0x5000ff00);
            addView(v);
            childViewAddedAlready = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float textWidth = paint.measureText(name);
        canvas.save();
        if(translateCanvas){
            canvas.translate(width/3, 0);
        }
        canvas.drawText(name, width / 2 - textWidth / 2, height / 2, paint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float rawX = event.getRawX();
        Log.i(TAG, name + ": x = " + x + ", rawX = " + rawX + ", left = " + getLeft() + ", x = "  +getX() + ", scrollX = " + getScrollX());
        View child = getChildAt(0);
        Log.i(TAG, "child left = " + child.getLeft() + ", child x = " + child.getX() + ", child scrollX = " + child.getScrollX());
        return true;
    }

}
