package games.gameOfFifteen

import java.util.*

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized first 15 cells on a board
     * (the last cell is empty)
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    override val initialPermutation by lazy {
        val random = Random()
        var permutation: List<Int>
        do {
            permutation = (1..15).shuffled(random)
        } while (!isEven(permutation))
        permutation
    }
}

