package games.gameOfFifteen

import org.junit.Assert
import org.junit.Test

class TestGameOfFifteenHelper {
    private fun testPermutation(permutation: List<Int>, isEven: Boolean) {
        Assert.assertEquals("This permutation should be ${if (isEven) "even" else "odd"}: $permutation", isEven,
                isEven(permutation))
    }

    @Test
    fun testExample0() = testPermutation((1..15).toList(), isEven = true)

    @Test
    fun testExample1() = testPermutation(listOf(1, 2, 3, 4), isEven = true)

    @Test
    fun testExample2() = testPermutation(listOf(2, 1, 4, 3), isEven = true)

    @Test
    fun testExample3() = testPermutation(listOf(4, 3, 2, 1), isEven = true)

    @Test
    fun testExample5() = testPermutation(listOf(1, 2, 4, 3), isEven = false)

    @Test
    fun testExample6() = testPermutation(listOf(1, 4, 3, 2), isEven = false)
}