package com.xiaopo.flying.sticker;

import android.view.MotionEvent;

/**
 ** DATE: 2022/10/12
 ** Author:tangerine
 ** Description:
 **/
public class RotateIconEvent implements StickerIconEvent{
    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {
        stickerView.rotateSticker(event);
    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        if (stickerView.getOnStickerOperationListener() != null) {
            stickerView.getOnStickerOperationListener()
                    .onStickerZoomFinished(stickerView.getCurrentSticker());
        }
    }
}
