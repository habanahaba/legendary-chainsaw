package org.hnh.mv

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.hnh.mv.GridUpdate.*
import org.junit.jupiter.api.Test

// Note that in the grid tests, we are not following the game rules - we have up to 4 token types,
// and those are thrown in out of order. That's ok, as we are focusing on
// pattern detection and boundary checks
class GridImplTest {
    @Test
    fun dropTokensUntilGridFull() {
        val grid = Grid<Any>(2, 2, 2)

        grid.dropToken(0, "a") shouldBe LineIncomplete
        grid.dropToken(0, "b") shouldBe LineIncomplete
        grid.dropToken(0, "a") shouldBe NotAllowed

        grid.dropToken(1, "a") shouldBe LineComplete
        grid.dropToken(1, "d") shouldBe GridFull
        grid.dropToken(1, "d") shouldBe NotAllowed
    }

    @Test
    fun detection() {
        Grid<Any>(2, 2, 2).also { grid ->
            grid.dropToken(0, "a") shouldBe LineIncomplete
            grid.dropToken(0, "a") shouldBe LineComplete
        }

        Grid<Any>(2, 2, 2).also { grid ->
            grid.dropToken(0, "a") shouldBe LineIncomplete
            grid.dropToken(1, "a") shouldBe LineComplete
        }

        Grid<Any>(2, 2, 2).also { grid ->
            grid.dropToken(1, "a") shouldBe LineIncomplete
            grid.dropToken(0, "b") shouldBe LineIncomplete
            grid.dropToken(0, "a") shouldBe LineComplete
        }

        Grid<Any>(2, 2, 2).also { grid ->
            grid.dropToken(0, "a") shouldBe LineIncomplete
            grid.dropToken(1, "b") shouldBe LineIncomplete
            grid.dropToken(1, "a") shouldBe LineComplete
        }

        Grid<Any>(3, 3, 3).also { grid ->
            grid.dropToken(0, "a") shouldBe LineIncomplete
            grid.dropToken(0, "b") shouldBe LineIncomplete
            grid.dropToken(0, "a") shouldBe LineIncomplete
            grid.dropToken(2, "a") shouldBe LineIncomplete
            grid.dropToken(1, "b") shouldBe LineIncomplete
            grid.dropToken(1, "a") shouldBe LineComplete
        }
    }

    @Test
    fun tokenAt() {
        val grid = Grid<Any>(2, 1, 1)

        grid.dropToken(0, "")

        grid.tokenAt(0, 0) shouldBe ""
        grid.tokenAt(0, 1).shouldBeNull()
    }

    @Test
    fun isValid() {
        val grid = Grid<Any>(1, 1, 1)

        grid.isValidRow(0).shouldBeTrue()
        grid.isValidRow(1).shouldBeFalse()

        grid.isValidColumn(0).shouldBeTrue()
        grid.isValidColumn(1).shouldBeFalse()
    }

    @Test
    fun lineLengthExceedsLimits() {
        shouldThrow<IllegalArgumentException> {
            Grid<Any>(2, 1, 2)
        }.message shouldBe "Pattern length must be <= grid height."

        shouldThrow<IllegalArgumentException> {
            Grid<Any>(1, 2, 2)
        }.message shouldBe "Pattern length must be <= grid width."
    }
}