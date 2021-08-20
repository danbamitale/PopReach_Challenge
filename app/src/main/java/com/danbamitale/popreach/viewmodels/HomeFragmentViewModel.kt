package com.danbamitale.popreach.viewmodels

import android.graphics.PointF
import androidx.lifecycle.ViewModel
import com.danbamitale.popreach.models.*
import com.danbamitale.popreach.models.ShapeOperation
import kotlinx.coroutines.flow.*
import java.lang.Exception
import java.lang.RuntimeException
import java.security.SecureRandom
import java.util.*
import kotlin.random.Random

class HomeFragmentViewModel : ViewModel() {
    private val random = SecureRandom().apply {
        setSeed(System.nanoTime())
    }
    private var viewHeight = 1600
    private var viewWidth = 900

    private val backStack =  Stack<Shape>()
    val shapes: List<Shape> = backStack

    private val _error: MutableStateFlow<Exception?> = MutableStateFlow(null)
    val onError: Flow<Exception?> = _error

    private val lastOperation = MutableStateFlow<ShapeOperation?>(null)
    val operation: StateFlow<ShapeOperation?> = lastOperation


    fun setViewDimension(width: Int, height: Int) {
        viewWidth = width - defaultWidth
        viewHeight = height - defaultWidth
    }


    fun resetState() {
        lastOperation.value = null
        _error.value = null
    }

    private fun getRandomPoint(): PointF {
        val x =  random.nextFloat() * viewWidth
        val y =  random.nextFloat() * viewHeight
        return PointF(x, y)
    }

    private fun overlaps(shape: Shape): Boolean {
        return backStack.any {
            it.overlapsWith(shape)
        }
    }

    fun draw(type: ShapeFactory.Type) {
        val shape = ShapeFactory.createShape(type, getRandomPoint())

        if (!overlaps(shape)) {
            backStack.push(shape)
            lastOperation.value = ShapeOperation.Create(shape)
        } else  {
            _error.tryEmit(RuntimeException("Overlap! Please try again."))
        }
    }

    fun transform(point: PointF) {
        backStack.forEachIndexed {i, old ->
            if (old.containsPoint(point)) {
                val now = old.transform()
                lastOperation.value = ShapeOperation.Transform(old, now)
                backStack.removeAt(i)
                backStack.push(now)
                return
            }
        }
    }

    fun delete(point: PointF) {
        backStack.forEachIndexed {i, shape ->
            if (shape.containsPoint(point)) {
                lastOperation.value = ShapeOperation.Delete(shape)
                backStack.removeAt(i)
                return
            }
        }
    }

    fun undo() {
        lastOperation.value?.let {
            when (it) {
                is ShapeOperation.Create -> backStack.pop()
                is ShapeOperation.Delete -> backStack.push(it.shape)
                is ShapeOperation.Transform -> {
                    backStack.pop()
                    backStack.push(it.old)
                }
            }
            lastOperation.value = null
        }
    }

    fun <T: Shape> deleteAll(type: Class<T>) {
        val typedShapes = backStack.filter {
            it.javaClass == type
        }

        if (typedShapes.isNotEmpty()) {
            backStack.removeAll(typedShapes)
//            lastOperation.value = ShapeOperation.DeleteAll(typedShapes) //not necessary with current implementation
        }
    }

}