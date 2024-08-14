package org.hnh.mv

import org.hnh.mv.GridUpdate.*
import org.hnh.mv.Outcome.*

// For simplicity, we use the player objects as grid tokens
typealias Token = Player

interface Game {
    val grid: Grid<Token>
    val currentPlayer: Player
    val outcome: Outcome?

    fun turn(col: Int): Outcome

    companion object {
        operator fun invoke(
            playerA: Player,
            playerB: Player,
            gridWidth: Int,
            gridHeight: Int,
            lineLength: Int
        ): Game = GameImpl(playerA, playerB, gridWidth, gridHeight, lineLength)
    }
}

private class GameImpl(
    private val playerA: Player,
    private val playerB: Player,
    gridWidth: Int,
    gridHeight: Int,
    patternLength: Int
) : Game {
    private var _outcome: Outcome? = null
    private var _currentPlayer: Player = playerA

    override val outcome: Outcome?
        get() = _outcome

    override val currentPlayer
        get() = _currentPlayer

    override val grid = Grid<Player>(gridWidth, gridHeight, patternLength)

    init {
        require(playerA !== playerB) { "Players must be different" }
    }

    override fun turn(col: Int): Outcome {
        // Game's already finished, no way back
        if (outcome != null) {
            return outcome!!
        }

        return when (grid.dropToken(col, _currentPlayer)) {
            LineComplete -> {
                _outcome = Win(_currentPlayer)
                _outcome!!
            }
            LineIncomplete -> {
                turnFinished()
                NextTurn
            }
            GridFull -> {
                _outcome = Draw
                _outcome!!
            }
            NotAllowed -> RepeatTurn
        }
    }

    private fun turnFinished() {
        _currentPlayer = if (_currentPlayer == playerA) playerB else playerA
    }
}

