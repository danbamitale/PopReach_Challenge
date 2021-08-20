package com.danbamitale.popreach.models

import android.graphics.Path
import android.graphics.PointF

data class Triangle(val point: PointF, val width: Float, val height: Float): Shape {

    override fun getPath(): Path {
       val path = Path()
        with (path) {
            moveTo(point.x, point.y - height)//top
            lineTo(point.x  - width, point.y + height)
            lineTo(point.x + width, point.y + height)
            lineTo(point.x, point.y - height)
            close()
        }

        return path
    }

    override fun getOrigin(): PointF {
        return point
    }
}
