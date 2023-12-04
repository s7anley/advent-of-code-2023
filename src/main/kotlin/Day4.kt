import Resources.resourceAsListOfString

fun toIntSet(text: String): Set<Int> = text.split(" ").mapNotNull { it.toIntOrNull() }.toSet()

private fun processInput(input: List<String>, updateResult: (cardId: Int, matchedCount: Int) -> Unit) {
    input.forEachIndexed { index, line ->
        val cardId = index + 1
        val parts = line.split(":", "|").drop(1)
        if (parts.size < 2) {
            throw IllegalArgumentException("Line format is incorrect.")
        }

        val winningNumbers = toIntSet(parts[0])
        val numbers = toIntSet(parts[1])
        val matchedNumbersCount = winningNumbers.intersect(numbers).size
        if (matchedNumbersCount > 0) {
            updateResult(cardId, matchedNumbersCount)
        }
    }
}

private fun solvePart1(input: List<String>): Int {
    var result = 0

    processInput(input) { _, matchedNumbersCount -> result += 1 shl matchedNumbersCount - 1 }

    return result
}

private fun solvePart2(input: List<String>): Int {
    val keys = 1..input.size
    val result = keys.associateWith { 1 }.toMutableMap()

    processInput(input) { cardId, matchedNumbersCount ->
        for (copyCardId in cardId + 1 until cardId + matchedNumbersCount + 1) {
            val numberOfCopies = result[cardId]!!
            result[copyCardId] = result.getOrDefault(copyCardId, 1) + numberOfCopies
        }
    }

    return result.values.sum()
}

fun main() {
    val example = resourceAsListOfString("day4-example.txt")
    val puzzleInput = resourceAsListOfString("day4.txt")

    val firstCases =
        listOf(
            Case(example, 13),
            Case(puzzleInput, 25010),
        )

    testCases(firstCases) { input -> solvePart1(input) }

    val secondCases =
        listOf(
            Case(example, 30),
            Case(puzzleInput, 9924412),
        )

    testCases(secondCases) { input -> solvePart2(input) }
}
