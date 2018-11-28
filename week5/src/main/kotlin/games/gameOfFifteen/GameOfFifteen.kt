package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'
 * (or choosing the corresponding run configuration).
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        board.initialize(initializer)
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        var seqNum = 1
        return board.all { it -> it == null || it == seqNum++ }
    }

    override fun processMove(direction: Direction) {
        with(board) {
            find { it == null }?.also { cell ->
                cell.getNeighbour(direction.reversed())?.also { neighbour ->
                    this[cell] = this[neighbour]
                    this[neighbour] = null
                }
            }
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run {
        val cell = getCell(i, j)
        this[cell]
    }
}

fun GameBoard<Int?>.initialize(initializer: GameOfFifteenInitializer) {
    initializer.initialPermutation.forEachIndexed { index, value ->
        val i = index / width + 1
        val j = index % width + 1
        val cell = getCell(i, j)
        this[cell] = value
    }
}
