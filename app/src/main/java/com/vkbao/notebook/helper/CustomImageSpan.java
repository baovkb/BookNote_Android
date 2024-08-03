package com.vkbao.notebook.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.util.Log;

import androidx.annotation.NonNull;

public class CustomImageSpan extends ImageSpan {
    private Drawable iconDrawable;
    private Rect iconRect;
    private Runnable onIconClick;
    private int start;
    private int end;
    String replaceString;

    public CustomImageSpan(Drawable drawable, Drawable iconDrawable, int start, int end, String replacedString) {
        super(drawable, ALIGN_BOTTOM);
        this.iconDrawable = iconDrawable;
        this.start = start;
        this.end = end;
        this.replaceString = replacedString;
    }

    public void setOnIconClick(Runnable runnable) {
        this.onIconClick = runnable;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getReplaceString() {
        return replaceString;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x, int top, int y, int bottom,
                     @NonNull Paint paint) {
        // Draw main image
        Drawable drawable = getDrawable();
        //save state
        canvas.save();
        //dịch y lên giá trị bằng đô cao hình ảnh chính => bottom của ảnh sẽ trùng với line
        int transY = bottom - drawable.getBounds().bottom;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();

        // Draw icon
        int iconSize = iconDrawable.getIntrinsicWidth();
        int iconX = (int) (x + drawable.getIntrinsicWidth() - iconSize);
        int iconY = top;

        iconDrawable.setBounds(iconX, iconY, iconX + iconSize, iconY + iconSize);
        iconDrawable.draw(canvas);

        // Update iconRect for touch detection
        this.iconRect = new Rect(iconX, iconY, iconX + iconSize, iconY + iconSize);
    }

    public Rect getIconRect() {
        return iconRect;
    }

    public Runnable getOnIconClick() {
        return onIconClick;
    }
}
