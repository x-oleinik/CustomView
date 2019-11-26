package io.realmagic.customview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyView extends View {
    private final static Paint BACKGROUND_CIRCLE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final static Paint FILLED_ARC_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final static Paint TEXT_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final float STROKE_WIDTH = 25f;
    private static final float RADIUS = 400f;
    private static final RectF ARC_RECT = new RectF(STROKE_WIDTH/2,STROKE_WIDTH/2, 2*RADIUS , 2*RADIUS );
    private static final float MAX_ANGLE = 360f;
    private static final float START_ANGLE = -90f;
    private static final float TEXT_SIZE = 128f;

    private Rect mTextBounds = new Rect();
    private int mProgress;
    @ColorInt
    private int mFillColor;


    public MyView(Context context) {
        this(context, null, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public int getmProgress() {
        return mProgress;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate(); //just Draw it again
        //requestLayout();  //measure+layout+Draw - 2much of work
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawCircle(RADIUS + STROKE_WIDTH / 2, RADIUS + STROKE_WIDTH / 2, RADIUS, BACKGROUND_CIRCLE_PAINT);
        canvas.drawArc(ARC_RECT, 0 , MAX_ANGLE, false, BACKGROUND_CIRCLE_PAINT);
        canvas.drawArc(ARC_RECT, START_ANGLE , MAX_ANGLE * mProgress / 100, false, FILLED_ARC_PAINT);
        //canvas.drawLine(0, RADIUS + STROKE_WIDTH / 2, 2*RADIUS + STROKE_WIDTH /2, RADIUS + STROKE_WIDTH/2, TEXT_PAINT);
        //canvas.drawLine( RADIUS + STROKE_WIDTH / 2, 0, RADIUS + STROKE_WIDTH /2, 2*RADIUS + STROKE_WIDTH/2, TEXT_PAINT);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        final String progressString = formatString(mProgress);
        getTextBounds(progressString);
        float x = ARC_RECT.width() / 2f - mTextBounds.width() / 2f - mTextBounds.left + ARC_RECT.left;
        float y = ARC_RECT.height() / 2f + mTextBounds.height() / 2f - mTextBounds.bottom + ARC_RECT.top;
        canvas.drawText(progressString, x, y, TEXT_PAINT);
    }

    private String formatString(int progress) {
        return String.format("%d %%", progress);
    }

    private void getTextBounds(@NonNull String progressString) {
        TEXT_PAINT.getTextBounds(progressString, 0, progressString.length(), mTextBounds);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs){
        extractAttributes(context, attrs);
        configureBackground();
        configureFrontArc();
        configureText();
    }

    private void extractAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            final Resources.Theme theme = context.getTheme();
            final TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.MyView, 0, R.style.FinanceProgressViewDefault);
            try {
                mProgress = typedArray.getInt(R.styleable.MyView_Progress, 0);
                mFillColor = typedArray.getColor(R.styleable.MyView_fillColor, Color.GREEN);
            }
            finally {
                typedArray.recycle();
            }
        }
    }

    private void configureText() {
        TEXT_PAINT.setColor(mFillColor);
        TEXT_PAINT.setStyle(Paint.Style.FILL);
        TEXT_PAINT.setTextSize(TEXT_SIZE);
    }

    private void configureFrontArc() {
        FILLED_ARC_PAINT.setColor(mFillColor);
        FILLED_ARC_PAINT.setStyle(Paint.Style.STROKE);
        FILLED_ARC_PAINT.setStrokeWidth(STROKE_WIDTH);
    }

    private void configureBackground() {
        BACKGROUND_CIRCLE_PAINT.setColor(Color.GRAY);
        BACKGROUND_CIRCLE_PAINT.setStrokeWidth(STROKE_WIDTH);
        BACKGROUND_CIRCLE_PAINT.setStyle(Paint.Style.STROKE);
    }

}
