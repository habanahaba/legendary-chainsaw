package org.hnh.mv

sealed interface Outcome {
    data object Draw : Outcome
    data object RepeatTurn : Outcome
    data object NextTurn: Outcome
    data class Win(val player: Player) : Outcome
}
