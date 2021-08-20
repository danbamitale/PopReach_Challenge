package com.danbamitale.popreach.models

import android.graphics.*

data class Square(val point: PointF, val width: Int): Shape {

    override fun getPath(): Path {
        val path = Path()

        val mid = width/2  //needed to manually draw the square to ensure point is centered in the square
                           // rather than at the top edge as in the platform implementation
        path.moveTo(point.x - mid, point.y - mid)
        path.lineTo(point.x + mid, point.y - mid) //--
        path.lineTo(point.x + mid, point.y + mid) // --|
        path.lineTo(point.x - mid, point.y + mid) // --|__
        path.lineTo(point.x - mid, point.y - mid) // --| __|

        return path
    }

    override fun getOrigin(): PointF {
        return point
    }
}
