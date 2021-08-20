package com.danbamitale.popreach.models

import android.graphics.PointF

object ShapeFactory {

    enum class Type {
        Square, Circle, Triangle
    }

    fun createShape(type: Type, point: PointF): Shape {
        return  when (type) {
            Type.Square -> Square(point, defaultWidth)
            Type.Circle -> Circle(point, defaultWidth.toFloat()/2)
            Type.Triangle -> Triangle(point, defaultWidth.toFloat()/2, defaultWidth.toFloat()/2)
        }
    }
}