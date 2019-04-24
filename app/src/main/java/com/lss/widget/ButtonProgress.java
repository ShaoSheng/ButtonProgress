package com.lss.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

public class ButtonProgress extends View {

    private static final String TAG = "ButtonProgress";

    private Context mContext;

    private int mProgress;
    private String mPro_Text = "";
    private String mText = "按钮一";
    private Paint paint_Text;// 文字画笔
    private Paint paint_Progress;//进度百分比

    private Drawable drawable_Background;
    private Drawable drawable_Progress;
    //dimension
    private float textSize = 25;
    private int textColor = Color.BLACK;

    private OnButtonProgressChangeListener mListener;

    public ButtonProgress(Context context) {
        super(context);

        ini(context, null);
    }

    public ButtonProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        ini(context, attrs);
    }

    public ButtonProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ini(context, attrs);
    }

    private void ini(Context context, AttributeSet attrs) {
        //
        this.mContext = context;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonProgress);
            this.textSize = typedArray.getFloat(R.styleable.ButtonProgress_textSize, 25);
            this.mText = typedArray.getString(R.styleable.ButtonProgress_text);
            this.textColor = typedArray.getColor(R.styleable.ButtonProgress_textColor, Color.BLACK);
            this.drawable_Background = typedArray.getDrawable(R.styleable.ButtonProgress_backgroundDrawable);
            this.drawable_Progress = typedArray.getDrawable(R.styleable.ButtonProgress_progressDrawable);
        }
        this.paint_Text = new Paint();
        this.paint_Text.setAntiAlias(true);//抗锯齿
        this.paint_Text.setTextSize(textSize);//字体大小
        this.paint_Text.setColor(textColor);//字体颜色

        this.paint_Progress = new Paint();
        this.paint_Progress.setAntiAlias(true);//抗锯齿
        this.paint_Progress.setTextSize(textSize);//字体大小
        this.paint_Progress.setColor(Color.WHITE);//字体颜色

        if (this.drawable_Background == null)
            this.drawable_Background = this.mContext.getDrawable(R.drawable.background);
        if (this.drawable_Progress == null)
            this.drawable_Progress = this.mContext.getDrawable(R.drawable.progress);
        if (mText == null)
            mText = "";
    }

    public void setOnButtonProgressChangeListener(OnButtonProgressChangeListener l) {
        mListener = l;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘画背景图片
        drawable_Background.setBounds(0, 0, this.getWidth(), this.getHeight());
        drawable_Background.draw(canvas);

        drawable_Progress.setBounds(0, 0, mProgress * this.getWidth() / 100, getHeight());
        drawable_Progress.draw(canvas);

        Rect rect = new Rect();
        paint_Text.getTextBounds(this.mText, 0, this.mText.length(), rect);
        int text_width = rect.width();
        int text_height = rect.height();

        rect = new Rect();
        paint_Progress.getTextBounds(this.mPro_Text, 0, this.mPro_Text.length(), rect);
        int pro_width = rect.width();

        // 绘画文字，居中显示
        canvas.drawText(this.mText, (this.getWidth() - text_width) / 2, (this.getHeight() + text_height) / 2, paint_Text);

        canvas.drawText(this.mPro_Text, (slide_X - pro_width) / 2, (this.getHeight() + text_height) / 2, paint_Progress);
    }

    public void setDrawable_Background(Drawable drawable) {
        this.drawable_Background = drawable;
    }

    public void setDrawable_Progress(Drawable drawable) {
        this.drawable_Progress = drawable;
    }

    public void setText(String text) {
        this.mText = text;
    }

    private void process(float x, float y) {
        // 计算Progress
        if (x > this.getWidth()) {
            x = this.getWidth();
        } else if (x < 0) {
            x = 0;
        }

        if (y > this.getHeight()) {
            y = this.getHeight();
        } else if (y < 0) {
            y = 0;
        }

        this.mProgress = (int) x * 100 / this.getWidth();
        mPro_Text = this.mProgress + " %";

        if (mListener != null)
            mListener.onProgressChanged(this, this.mProgress);

        Log.e(TAG, "mProgress = " + this.mProgress);
    }

    private float slide_X;
    private float start_X;
    private Calendar down_time;
    private boolean enabled_pro = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        boolean bol = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录按下的坐标
                start_X = event.getX();
                down_time = Calendar.getInstance();
                if (mListener != null)
                    mListener.onStartTrackingTouch(this);
                enabled_pro = false;
                bol = true;
                break;
            case MotionEvent.ACTION_MOVE:
                slide_X = event.getX();
                // 记录移动的位置
                process(event.getX(), event.getY());
                bol = true;
                enabled_pro = true;
                break;
            case MotionEvent.ACTION_UP:
                // 按压小于1.2秒,并且未触发调光控制
                // 记录释放的位置
                process(event.getX(), event.getY());
                if (mListener != null)
                    mListener.onStopTrackingTouch(this);
                bol = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mListener != null)
                    mListener.onStopTrackingTouch(this);
                bol = false;
                break;
        }
        invalidate();
        return bol;
    }

    public interface OnButtonProgressChangeListener {

        void onStopTrackingTouch(ButtonProgress buttonProgress);

        void onStartTrackingTouch(ButtonProgress buttonProgress);

        void onProgressChanged(ButtonProgress buttonProgress, int progress);
    }
}
