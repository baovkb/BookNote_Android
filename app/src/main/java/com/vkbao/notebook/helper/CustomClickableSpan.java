package com.vkbao.notebook.helper;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class CustomClickableSpan extends ClickableSpan {
    private CustomImageSpan customImageSpan;

    public CustomClickableSpan(CustomImageSpan customImageSpan) {
        this.customImageSpan = customImageSpan;
    }

    @Override
    public void onClick(@NonNull View view) {

    }

    private boolean isTouchInIcon(float x, float y) {
        if (customImageSpan.getIconRect() == null) return false;
        return customImageSpan.getIconRect().contains((int) x, (int) y);
    }

    public boolean runOnIconClick(float x, float y) {
        if (!isTouchInIcon(x, y)) {
            return false;
        }
        if (customImageSpan.getOnIconClick() != null) {
            customImageSpan.getOnIconClick().run();
        }

        return true;
    }
}
