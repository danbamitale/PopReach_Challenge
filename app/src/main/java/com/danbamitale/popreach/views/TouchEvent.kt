package com.danbamitale.popreach.views

import android.graphics.PointF

sealed class TouchEvent {
    data class Press(val point: PointF): TouchEvent()
    data class LongPress(val point: PointF): TouchEvent()
}
