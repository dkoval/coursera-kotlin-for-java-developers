package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game)
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game executing 'PlayGame2048'
 * (or choosing the corresponding run configuration).
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    initializer.nextValue(this)?.also { (cell, value) -> this[cell] = value }
}

/*
 * Move values in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column), in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val movedAndMerged = rowOrColumn
            .map { cell -> this[cell] }
            .moveAndMergeEqual { value -> value * 2 }

    val moved = movedAndMerged.isNotEmpty() && (movedAndMerged.size != rowOrColumn.size)
    if (moved) {
        rowOrColumn.forEach { cell -> this[cell] = null }
        movedAndMerged.forEachIndexed { index, value ->
            val cell = rowOrColumn[index]
            this[cell] = value
        }
    }
    return moved
}

/*
 * Move values by the rules of the 2048 game to the specified direction.
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    fun moveAt(rowOrColumnAt: (Int) -> List<Cell>): Boolean {
        return 1.rangeTo(width).fold(false) { moved, index ->
            val rowOrColumn = rowOrColumnAt(index)
            moveValuesInRowOrColumn(rowOrColumn) || moved
        }
    }
    return when (direction) {
        Direction.LEFT -> moveAt { i -> getRow(i, 1.rangeTo(width)) }
        Direction.RIGHT -> moveAt { i -> getRow(i, width.downTo(1)) }
        Direction.UP -> moveAt { j -> getColumn(1.rangeTo(width), j) }
        Direction.DOWN -> moveAt { j -> getColumn(width.downTo(1), j) }
    }
}
