package com.danbamitale.popreach.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.danbamitale.popreach.R
import com.danbamitale.popreach.models.Shape
import com.danbamitale.popreach.utils.removePath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class CanvasView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    private val longPressDelayMillis = 400
    private var pressedTime = 0L

    private val foreColor = ResourcesCompat.getColor(resources, R.color.canvas_color, null)
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.canvas_bg_color, null)

    private val drawing = Path()

    private val paint = Paint().apply {
        color = foreColor
        isDither = true
        isAntiAlias = true
    }

    private val touchHandler = MutableSharedFlow<TouchEvent>(1)
    val onTouch: Flow<TouchEvent> = touchHandler

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(backgroundColor)
        canvas.drawPath(drawing, paint)
    }

    fun draw(shape: Shape) {
        drawing.addPath(shape.getPath())
        invalidate()
    }

    fun remove(vararg shape: Shape) {
        shape.forEach {
            drawing.removePath(it.getPath())
        }
        invalidate()
    }

    fun drawAll(shapes: Collection<Shape>) {
        shapes.forEach {
            drawing.addPath(it.getPath())
        }
        invalidate()
    }

    fun clear() {
        drawing.rewind()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                pressedTime = System.currentTimeMillis()
                true
            }

            MotionEvent.ACTION_UP -> {
                val now =  System.currentTimeMillis()
                if (longPressDelayMillis <= (now - pressedTime)) { //long press
                    touchHandler.tryEmit(TouchEvent.LongPress(PointF(event.x, event.y)))
                } else {
                    touchHandler.tryEmit(TouchEvent.Press(PointF(event.x, event.y)))
                }
                true
            }

            else -> super.onTouchEvent(event)
        }
    }



}