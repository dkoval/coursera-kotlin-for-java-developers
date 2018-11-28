package games.game2048

import board.Cell
import board.GameBoard
import java.util.*

interface Game2048Initializer<T> {
    fun nextValue(board: GameBoard<T?>): Pair<Cell, T>?
}

object RandomGame2048Initializer : Game2048Initializer<Int> {
    private val random = Random()
    private fun generateRandomStartValue(): Int =
            if (random.nextInt(10) == 9) 4 else 2

    /*
     * Generate a random value and a random cell (among free cells)
     * that given value should be added to.
     * The value should be 2 for 90% cases, and 4 for the rest of the cases.
     * Use the 'generateRandomStartValue' function above.
     * If the board is full return null.
     */
    override fun nextValue(board: GameBoard<Int?>): Pair<Cell, Int>? {
        return board.find { it -> it == null }?.let { cell -> Pair(cell, generateRandomStartValue()) }
    }
}
