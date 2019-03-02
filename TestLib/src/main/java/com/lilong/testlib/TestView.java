package com.lilong.testlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TestView extends View {

    private Paint paint;
    private Context context;

    public TestView(Context context) {
        super(context);
        init(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(80);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String text = context.getString(R.string.drawText);
        float textWidth = paint.measureText(text);
        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2, paint);
    }
}
