package board

import org.junit.Assert
import org.junit.Test

class TestSquareBoard {

    private fun Cell?.asString() = if (this != null) "($i, $j)" else ""

    private fun Collection<Cell>.asString() = joinToString(prefix = "[", postfix = "]") { it.asString() }

    @Test
    fun testAllCells() {
        val board = createSquareBoard(2)
        val cells = board.getAllCells().sortedWith(compareBy<Cell> { it.i }.thenBy { it.j })
        Assert.assertEquals("[(1, 1), (1, 2), (2, 1), (2, 2)]", cells.asString())
    }

    @Test
    fun testCell() {
        val board = createSquareBoard(2)
        val cell = board.getCellOrNull(1, 2)
        Assert.assertEquals(1, cell?.i)
        Assert.assertEquals(2, cell?.j)
    }

    @Test
    fun testNoCell() {
        val board = createSquareBoard(2)
        val cell = board.getCellOrNull(3, 3)
        Assert.assertEquals(null, cell)
    }

    @Test
    fun testRow() {
        val board = createSquareBoard(2)
        val row = board.getRow(1, 1..2)
        Assert.assertEquals("[(1, 1), (1, 2)]", row.asString())
    }

    @Test
    fun testRowReversed() {
        val board = createSquareBoard(2)
        val row = board.getRow(1, 2 downTo 1)
        Assert.assertEquals("[(1, 2), (1, 1)]", row.asString())
    }

    @Test
    fun testRowWrongRange() {
        val board = createSquareBoard(2)
        val row = board.getRow(1, 1..10)
        Assert.assertEquals("[(1, 1), (1, 2)]", row.asString())
    }

    @Test
    fun testNeighbour() {
        val board = createSquareBoard(2)
        with(board) {
            val cell = getCellOrNull(1, 1)
            Assert.assertNotNull(cell)
            Assert.assertEquals(null, cell!!.getNeighbour(Direction.UP))
            Assert.assertEquals(null, cell.getNeighbour(Direction.LEFT))
            Assert.assertEquals("(2, 1)", cell.getNeighbour(Direction.DOWN).asString())
            Assert.assertEquals("(1, 2)", cell.getNeighbour(Direction.RIGHT).asString())
        }
    }
}
