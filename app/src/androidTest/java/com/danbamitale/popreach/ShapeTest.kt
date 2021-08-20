package com.danbamitale.popreach

import android.graphics.PointF
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.danbamitale.popreach.models.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ShapeTest {

    @Test
    fun testSquareContainsPoint() {
        val point = PointF(10f, 20f)
        val shape = Square(point, 20)

        assertEquals(true, shape.containsPoint(PointF(11f, 21f)))
        assertEquals(true, shape.containsPoint(PointF(14f, 24f)))
        assertEquals(true, shape.containsPoint(PointF(27f, 38f)))
        assertEquals(true, shape.containsPoint(PointF(29f, 39f)))

        assertEquals(false, shape.containsPoint(PointF(9f, 19f)))
        assertEquals(false, shape.containsPoint(PointF(31f, 41f)))
    }

    @Test
    fun testCircleContainsPoint() {
        val point = PointF(10f, 20f)
        val shape = Circle(point, 10f)

        assertEquals(true, shape.containsPoint(PointF(10f, 20f)))
        assertEquals(true, shape.containsPoint(PointF(15f, 27f)))
        assertEquals(true, shape.containsPoint(PointF(10f, 16f)))
    }

    @Test
    fun testTriangleContainsPoint() {
        val point = PointF(10f, 20f)
        val shape = Triangle(point, 20f, 20f)

        assertEquals(true, shape.containsPoint(PointF(10f, 20f)))
        assertEquals(true, shape.containsPoint(PointF(15f, 27f)))
        assertEquals(true, shape.containsPoint(PointF(2f, 20f)))
    }

    @Test
    fun testShapeOverlap() {
        val square1 = Square(PointF(30f, 30f), 20)
        val square2 = Square(PointF(45f, 45f), 20)
        val square3 = Square(PointF(60f, 60f), 20)

        assertEquals(true, square1.overlapsWith(square2))
        assertEquals(false, square1.overlapsWith(square3))
        assertEquals(true, square2.overlapsWith(square3))
    }
}