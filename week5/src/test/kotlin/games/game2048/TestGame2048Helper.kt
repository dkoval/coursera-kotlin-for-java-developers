package games.game2048

import org.junit.Assert
import org.junit.Test

class TestGame2048Helper {
    @Test
    fun testSample1() = testMerge(listOf("a", "a", "b"), listOf("aa", "b"))

    @Test
    fun testSample2() = testMerge(listOf("a", null), listOf("a"))

    @Test
    fun testSample3() = testMerge(listOf("b", null, "a", "a"), listOf("b", "aa"))

    @Test
    fun testSample4() = testMerge(listOf("a", "a", null, "a"), listOf("aa", "a"))

    @Test
    fun testSample5() = testMerge(listOf("a", null, "a", "a"), listOf("aa", "a"))

    private fun testMerge(input: List<String?>, expected: List<String?>) {
        val result = input.moveAndMergeEqual { it.repeat(2) }
        Assert.assertEquals("Wrong result for $input.moveAndMergeEqual()",
                expected, result)
    }
}