package com.danbamitale.popreach.utils

import android.graphics.*


fun Path.overlaps(path: Path): Boolean {
    val temp = Path()
    temp.addPath(this)

    temp.op(path, Path.Op.INTERSECT) // get difference with our PathToCheck
    return  !temp.isEmpty // if out path cover temp path we get empty path in result
}

fun Path.removePath(path: Path) {
    this.op(path, Path.Op.DIFFERENCE)
}


fun Path.containsPoint(point: PointF): Boolean {

    val x = point.x
    val y = point.y

    val tempPath = Path() // Create temp Path

    tempPath.moveTo(x, y) // Move cursor to point

    val rectangle = RectF(
        (x - 1), (y - 1), (x + 1),
        (y + 1)
    ) // create rectangle with size 2xp

    tempPath.addRect(rectangle, Path.Direction.CW) // add rect to temp path
    tempPath.op(this, Path.Op.DIFFERENCE) // get difference with current path

    return  tempPath.isEmpty // if out path cover temp path we get empty path in result
}