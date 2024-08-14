package org.hnh.mv

internal data class Direction(val dx: Int, val dy: Int) {
    fun opposite(): Direction = Direction(-dx, -dy)

    companion object {
        val N = Direction(0, -1)
        val E = Direction(1, 0)
        val NE = Direction(1, 1)
        val NW = Direction(1, -1)
    }
}