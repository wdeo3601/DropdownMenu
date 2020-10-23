package com.wdeo3601.dropdownmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class DrawableCenterTextView extends AppCompatTextView {
    public DrawableCenterTextView(Context context) {
        this(context, null);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable[] drawables = getCompoundDrawables();
        Drawable leftDrawable = drawables[0]; //drawableLeft
        Drawable rightDrawable = drawables[2];//drawableRight
        if (leftDrawable != null || rightDrawable != null) {
            //1,获取text的width
            float textWidth = getPaint().measureText(getText(), 0, getText().length());
            //2,获取Drawable padding
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth;
            float bodyWidth;
            if (leftDrawable != null) {
                //3,获取drawable的宽度
                drawableWidth = leftDrawable.getIntrinsicWidth();
            } else {
                drawableWidth = rightDrawable.getIntrinsicWidth();
            }
            //4,获取绘制区域的总宽度
            bodyWidth = textWidth + drawablePadding + drawableWidth;
            if (bodyWidth < getWidth()) {
                if (leftDrawable != null)
                    setPadding(0, getPaddingTop(), 0, getPaddingBottom());
                else
                    //图片居右设置padding
                    setPadding(0, getPaddingTop(), (int) (getWidth() - bodyWidth), getPaddingBottom());

                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }
}
