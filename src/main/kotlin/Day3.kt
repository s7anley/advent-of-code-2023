import Resources.resourceAsListOfString

private data class Schematic(val data: List<String>) {
    val rowsCount: Int by lazy { data.size }
    val colsCount: Int by lazy { data.firstOrNull()?.length ?: 0 }
}

private data class Symbol(val row: Int, val col: Int)

private fun processInput(input: Schematic, updateResult: (Int, Symbol) -> Unit, matchCondition: (Char) -> Boolean) {
    var symbol: Symbol? = null
    var currentNumber = ""

    fun handleNumber(updateResult: (Int, Symbol) -> Unit) {
        if (symbol != null && currentNumber.isNotEmpty()) {
            val number = currentNumber.toInt()
            updateResult(number, symbol!!)
        }

        symbol = null
        currentNumber = ""
    }

    input.data.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, char ->
            if (char.isDigit()) {
                if (symbol == null) {
                    symbol = findAdjacentSymbol(input, rowIndex, colIndex, matchCondition)
                }

                currentNumber += char.toString()
            } else {
                handleNumber(updateResult)
            }
        }

        handleNumber(updateResult)
    }
}

private fun findAdjacentSymbol(
    schematic: Schematic,
    currentRow: Int,
    currentCol: Int,
    matchCondition: (Char) -> Boolean
): Symbol? {
    val directions = arrayOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1, -1 to -1, -1 to 1, 1 to -1, 1 to 1)

    for ((rowDirection, colDirection) in directions) {
        val newRow = currentRow + rowDirection
        val newCol = currentCol + colDirection

        if (newRow in 0 until schematic.rowsCount && newCol in 0 until schematic.colsCount) {
            val charAtCell = schematic.data[newRow][newCol]
            if (matchCondition(charAtCell)) {
                return Symbol(newRow, newCol)
            }
        }
    }

    return null
}

private fun solvePart1(input: Schematic): Int {
    var result = 0
    val matchSymbols: (Char) -> Boolean = { !it.isDigit() && it != '.' }
    val updateResult: (Int, Symbol) -> Unit = { number, _ -> result += number }

    processInput(input, updateResult, matchSymbols)

    return result
}

private fun solvePart2(input: Schematic): Int {
    val result: MutableMap<Symbol, MutableList<Int>> = mutableMapOf()
    val matchGears: (Char) -> Boolean = { it == '*' }
    val updateResult: (Int, Symbol) -> Unit = { number, symbol ->
        result.getOrPut(symbol) { mutableListOf() }.add(number)
    }

    processInput(input, updateResult, matchGears)

    return result.values.filter { it.size > 1 }.sumOf { numbers -> numbers.reduce(Int::times) }
}

fun main() {
    val example = Schematic(resourceAsListOfString("day3-example.txt"))
    val puzzleInput = Schematic(resourceAsListOfString("day3.txt"))

    val firstCases =
        listOf(
            Case(example, 4361),
            Case(puzzleInput, 551094),
        )

    testCases(firstCases) { input -> solvePart1(input) }

    val secondCases =
        listOf(
            Case(example, 467835),
            Case(puzzleInput, 80179647),
        )

    testCases(secondCases) { input -> solvePart2(input) }
}
