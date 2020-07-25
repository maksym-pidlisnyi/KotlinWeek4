package board

import board.Direction.*
import board.GameBoard as GameBoard

fun createSquareBoard(width: Int): SquareBoard {
    val sq = SquareBoardImpl(width)
    sq.cells = createEmptyBoard(width)
    return sq
}

fun <T> createGameBoard(width: Int): GameBoard<T> {
    val gb = GameBoardImpl<T>(width)
    gb.cells = createEmptyBoard(width)
    gb.cells.forEach { it.forEach { cell: Cell -> gb.cellsV += cell to null } }
    return gb
}


//redo
fun createEmptyBoard(width: Int) : Array<Array<Cell>> {
    var board = arrayOf<Array<Cell>>()
    for (i in 1..width) {
        var arr = arrayOf<Cell>()
        for (j in 1..width) {
            arr += CellImpl(i, j)
        }
        board += arr
    }
    return board
}

open class SquareBoardImpl(override val width: Int) : SquareBoard {
    var cells : Array<Array<Cell>> = arrayOf(arrayOf())

    override fun getCellOrNull(i: Int, j: Int) =
            when {
                i > width || j > width || i <= 0 || j <= 0 -> null
                else -> getCell(i, j)
            }

    override fun getCell(i: Int, j: Int): Cell {
        return cells[i - 1][j - 1]
    }

    override fun getAllCells() =
            IntRange(1, width).flatMap {
                i: Int -> IntRange(1, width)
                    .map { j : Int -> getCell(i, j) } } .toList()

    override fun getRow(i: Int, jRange: IntProgression) =
            when {
                jRange.last > width -> IntRange(jRange.first, width).map { j: Int -> getCell(i, j) }.toList()
                else -> jRange.map { j : Int -> getCell(i, j) }.toList()
            }

    override fun getColumn(iRange: IntProgression, j: Int) =
            when {
                iRange.last > width -> IntRange(iRange.first, width).map { i: Int -> getCell(i, j) }.toList()
                else -> iRange.map { i : Int -> getCell(i, j) }.toList()
            }

    override fun Cell.getNeighbour(direction: Direction): Cell? =
            when(direction) {
                UP -> getCellOrNull(i - 1, j)
                DOWN -> getCellOrNull(i + 1, j)
                LEFT -> getCellOrNull(i, j - 1)
                RIGHT -> getCellOrNull(i, j + 1)
            }
}

class GameBoardImpl<T>(override val width: Int) : GameBoard<T>, SquareBoardImpl(width) {

    val cellsV = mutableMapOf<Cell, T?>()

    override fun get(cell: Cell): T? = cellsV[cell]

    override fun set(cell: Cell, value: T?) {
        cellsV += cell to value
    }

    override fun filter(predicate: (T?) -> Boolean) = cellsV.filterValues { predicate.invoke(it) }.keys

    override fun find(predicate: (T?) -> Boolean) = cellsV.filter { predicate.invoke(it.value) }.keys.first()

    override fun any(predicate: (T?) -> Boolean) = cellsV.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean) = cellsV.values.all(predicate)
}

data class CellImpl(override val i: Int, override val j: Int) : Cell {
    override fun toString()= "($i, $j)"
}