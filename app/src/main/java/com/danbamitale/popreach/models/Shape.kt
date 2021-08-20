package com.danbamitale.popreach.models

import android.graphics.*
import com.danbamitale.popreach.utils.containsPoint
import com.danbamitale.popreach.utils.overlaps


const val defaultWidth = 200

interface Shape {
    fun getPath(): Path
    fun getOrigin(): PointF
}


fun Shape.containsPoint(point: PointF): Boolean = getPath().containsPoint(point)

fun Shape.overlapsWith(shape: Shape) = this.getPath().overlaps(shape.getPath())

fun Shape.transform(): Shape {
    val point = getOrigin()
    val type = when (this) {
        is Square -> ShapeFactory.Type.Circle
        is Circle -> ShapeFactory.Type.Triangle
        is Triangle -> ShapeFactory.Type.Square
        else -> throw RuntimeException("not yet handled")
    }

    return ShapeFactory.createShape(type, point)
}