package board

data class CellImpl<T>(override val i: Int, override val j: Int, var value: T? = null) : Cell

open class SquareBoardImpl<T>(final override val width: Int) : SquareBoard {
    protected val cells: List<CellImpl<T>>

    init {
        if (width <= 0) throw IllegalArgumentException("Width must be a positive number, was: $width")
        cells = IntRange(1, width).flatMap { i -> IntRange(1, width).map { j -> CellImpl<T>(i, j) } }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if (i <= width && i <= width) cells[to1DIndex(i, j)] else null
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j) ?: throw IllegalArgumentException(
                "Cell ($i, $j) is out of $width x $width board boundaries")
    }

    protected fun to1DIndex(row: Int, col: Int): Int {
        require(row >= 1) { "row must be 1-based index, was: $row" }
        require(col >= 1) { "col must be 1-based index, was: $col" }
        return (row - 1) * width + (col - 1)
    }

    override fun getAllCells(): Collection<Cell> {
        return cells
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return getRange(i, jRange) { row, col -> to1DIndex(row, col) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return getRange(j, iRange) { col, row -> to1DIndex(row, col) }
    }

    private fun getRange(fixedCoord: Int, range: IntProgression, indexer: (Int, Int) -> Int): List<Cell> {
        val (start, end) = restrictToBoardBoundaries(range)
        return IntProgression.fromClosedRange(start, end, range.step).map { fluentCoord ->
            val index = indexer(fixedCoord, fluentCoord)
            cells[index]
        }
    }

    private fun restrictToBoardBoundaries(range: IntProgression): Pair<Int, Int> {
        return if (range.step > 0)
            Pair(maxOf(range.first, 1), minOf(range.last, width))
        else
            Pair(minOf(range.first, width), maxOf(range.last, 1))
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            Direction.UP -> if (this.i > 1) cells[to1DIndex(this.i - 1, this.j)] else null
            Direction.DOWN -> if (this.i < width) cells[to1DIndex(this.i + 1, this.j)] else null
            Direction.LEFT -> if (this.j > 1) cells[to1DIndex(this.i, this.j - 1)] else null
            Direction.RIGHT -> if (this.j < width) cells[to1DIndex(this.i, this.j + 1)] else null
        }
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl<T>(width), GameBoard<T> {

    override fun get(cell: Cell): T? {
        return cells[to1DIndex(cell.i, cell.j)].value
    }

    override fun set(cell: Cell, value: T?) {
        cells[to1DIndex(cell.i, cell.j)].value = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cells.filter(cellMatcher(predicate))
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cells.find(cellMatcher(predicate))
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cells.any(cellMatcher(predicate))
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cells.all(cellMatcher(predicate))
    }

    private fun cellMatcher(predicate: (T?) -> Boolean): (CellImpl<T>) -> Boolean {
        return { cell -> predicate(cell.value) }
    }
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl<Nothing>(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)
