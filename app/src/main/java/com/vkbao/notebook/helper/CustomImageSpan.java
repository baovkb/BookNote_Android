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
    private String uniqueString;

    public CustomImageSpan(Drawable drawable, Drawable iconDrawable, String uniqueString) {
        super(drawable, ALIGN_BOTTOM);
        this.iconDrawable = iconDrawable;
        this.uniqueString = uniqueString;
    }

    public void setOnIconClick(Runnable runnable) {
        this.onIconClick = runnable;
    }


    public String getUniqueString() {
        return uniqueString;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x, int top, int y, int bottom,
                     @NonNull Paint paint) {
        // Draw main image
        Drawable drawable = getDrawable();
        int imgWidth = Math.abs(drawable.getBounds().right - drawable.getBounds().left);
        int imgHeight = Math.abs(drawable.getBounds().top - drawable.getBounds().bottom);
        //save state
        canvas.save();
        //dịch y lên giá trị bằng đô cao hình ảnh chính => bottom của ảnh sẽ trùng với line
        int transY = bottom - drawable.getBounds().bottom;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();

        // Draw icon
        int iconSize = Math.abs(iconDrawable.getBounds().right - iconDrawable.getBounds().left);
        int iconX = (int) (x + imgWidth - iconSize);
        int iconY = (int) (transY);

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
