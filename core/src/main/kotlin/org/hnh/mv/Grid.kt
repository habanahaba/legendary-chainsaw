package org.hnh.mv

import org.hnh.mv.GridUpdate.*

interface Grid<T> {
    val width: Int
    val height: Int

    fun dropToken(col: Int, token: T): GridUpdate
    fun tokenAt(row: Int, col: Int): T?
    fun isValidRow(row: Int): Boolean
    fun isValidColumn(col: Int): Boolean

    companion object {
        operator fun <T> invoke(width: Int, height: Int, lineLength: Int): Grid<T> = GridImpl(
            width, height, lineLength
        )
    }
}

class GridImpl<T>(override val width: Int, override val height: Int, private val lineLength: Int) : Grid<T> {
    init {
        require(width > 0) { "Width must be positive." }
        require(height > 0) { "Height must be positive." }
        require(lineLength > 0) { "Pattern length must be positive." }
        require(lineLength <= width) { "Pattern length must be <= grid width." }
        require(lineLength <= height) { "Pattern length must be <= grid height." }
    }

    private val columns = Array(this.width) { mutableListOf<T>() }
    private var freeSlots = this.height * this.width

    override fun dropToken(col: Int, token: T): GridUpdate {
        if (!isValidColumn(col)) {
            return NotAllowed
        }

        val column = columns[col]

        if (column.size == height) {
            return NotAllowed
        }

        column.add(token)
        freeSlots--

        return when (val d = detectLine(height - column.size, col, token)) {
            LineIncomplete -> {
                if (freeSlots == 0) GridFull else LineIncomplete
            }
            else -> d
        }
    }

    override fun tokenAt(row: Int, col: Int): T? {
        require(isValidColumn(col)) {
            "Column $col is out of bounds"
        }

        require(isValidRow(row)) {
            "Row $row is out of bounds"
        }

        val column = columns[col]
        val mirroredRow = height - 1 - row

        return if (column.size <= mirroredRow) {
            null
        } else {
            column[mirroredRow]
        }
    }

    override fun isValidRow(row: Int): Boolean = row >= 0 && row < this.height

    override fun isValidColumn(col: Int): Boolean = col >= 0 && col < this.width

    // Brute-force implementation of line detection, automatic "no hire" from any bigtech
    private fun detectLine(row: Int, col: Int, token: T): GridUpdate {
        for (d in listOf(Direction.N, Direction.E, Direction.NE, Direction.NW)) {
            var curRow = row
            var curCol = col
            var len = 0

            fun trace(d: Direction) {
                while (
                    isValidRow(curRow)
                    && isValidColumn(curCol)
                    && tokenAt(curRow, curCol) == token
                    && len < lineLength
                ) {
                    len++
                    curRow += d.dy
                    curCol += d.dx
                }
            }

            trace(d)

            val od = d.opposite()
            curRow = row + od.dy
            curCol = col + od.dx

            trace(od)

            if (len >= lineLength) {
                return LineComplete
            }
        }

        return LineIncomplete
    }
}