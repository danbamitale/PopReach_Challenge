package com.danbamitale.popreach.models

import android.graphics.Path
import android.graphics.PointF

data class Circle(val point: PointF, val radius: Float): Shape {
    override fun getPath(): Path {
        val x = point.x //+ radius/2 //needed to transform point so the generated shape covers approximate area that the square would cover
        val y = point.y// + radius/2
        val path = Path()
        path.addCircle(point.x, point.y, radius, Path.Direction.CW)
        return path
    }

    override fun getOrigin(): PointF {
        return point
    }
}
