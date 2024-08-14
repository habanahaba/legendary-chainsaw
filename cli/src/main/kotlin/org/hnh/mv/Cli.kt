package org.hnh.mv

import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.input
import com.varabyte.kotter.foundation.input.onInputEntered
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.Color
import com.varabyte.kotter.foundation.text.ColorLayer.BG
import com.varabyte.kotter.foundation.text.color
import com.varabyte.kotter.foundation.text.text
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.runtime.MainRenderScope
import com.varabyte.kotterx.grid.Cols
import com.varabyte.kotterx.grid.grid
import com.varabyte.kotterx.text.Justification.CENTER
import org.hnh.mv.Outcome.Draw
import org.hnh.mv.Outcome.Win

fun main() {
    GameCli.run()
}

object GameCli {
    private val playerA = object : Player {
        override val name = "Jane Doe"
    }

    private val playerB = object : Player {
        override val name = "Joe Bloggs"
    }

    private val playerColors: Map<Player, Color> = mapOf(
        playerA to Color.BRIGHT_BLUE,
        playerB to Color.BRIGHT_YELLOW
    )

    fun run() {
        val game = Game(playerA, playerB, gridWidth = 6, gridHeight = 7, lineLength = 4)
        session {
            val nextCol = liveVarOf(-1)
            section {
                if (nextCol.value != -1) {
                    game.turn(nextCol.value)
                    nextCol.value = -1
                }

                renderGrid(game)

                when (val o = game.outcome) {
                    is Win -> {
                        scopedState {
                            text("And our winner is ")
                            color(playerColors.getValue(o.player))
                            textLine("${o.player.name}!!!")
                        }

                        textLine("Press Q to quit...")
                    }

                    is Draw -> {
                        textLine("We've finished with a draw!!!")
                        textLine("Press Q to quit...")
                    }

                    else -> {
                        scopedState {
                            text("Choose your column, ")
                            color(playerColors.getValue(game.currentPlayer))
                            textLine(game.currentPlayer.name)
                            input()
                        }
                    }
                }
            }.runUntilSignal {
                onKeyPressed {
                    if (key == Keys.Q) {
                        signal()
                    }
                }
                onInputEntered {
                    val maybeCol = input.toIntOrNull()
                    if (maybeCol != null && game.grid.isValidColumn(maybeCol)) {
                        nextCol.value = maybeCol
                    }
                    clearInput()
                }
            }
        }
    }

    private fun MainRenderScope.renderGrid(game: Game) {
        grid(Cols.uniform(game.grid.width, 4)) {
            for (row in 0 until game.grid.height) {
                for (col in 0 until game.grid.width) {
                    val token = game.grid.tokenAt(row, col)
                    cell(row, col, justification = CENTER) {
                        if (token != null) {
                            color(color = playerColors.getValue(token), layer = BG)
                            textLine(" ".repeat(2))
                        }
                    }
                }
            }
        }
    }
}