package com.danbamitale.popreach.models

sealed class ShapeOperation {
    data class Create(val shape: Shape): ShapeOperation()
    data class Delete(val shape: Shape): ShapeOperation()
    data class Transform(val old: Shape, val now: Shape): ShapeOperation()
    data class DeleteAll(val shapes: List<Shape>): ShapeOperation()
}
