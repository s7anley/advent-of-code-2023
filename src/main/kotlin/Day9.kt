import Resources.resourceAsListOfString

private fun calculateDifferenceSequence(line: String, selector: (List<Long>) -> Long): List<Long> {
    var numbers = extractNumbers(line)
    val result = mutableListOf(selector(numbers))

    while (numbers.size > 1) {
        numbers = numbers.windowed(2).map { (prev, curr) -> curr - prev }
        result.add(selector(numbers))

        if (numbers.all { it == 0L }) break
    }

    return result.toList()
}

private fun solvePart1(input: List<String>): Long {
    return input.sumOf { line -> calculateDifferenceSequence(line) { it.last() }.sum() }
}

private fun solvePart2(input: List<String>): Long {
    return input.sumOf { line ->
        val firstItems = calculateDifferenceSequence(line) { it.first() }.asReversed()
        var value = firstItems.first()
        firstItems.drop(1).forEach { currentValue -> value = currentValue - value }
        value
    }
}

fun main() {
    val example = resourceAsListOfString("day9-example.txt")
    val puzzleInput = resourceAsListOfString("day9.txt")

    val firstCases =
        listOf(
            Case(example, 114L),
            Case(puzzleInput, 1868368343L),
        )

    testCases(firstCases) { input -> solvePart1(input) }

    val secondCases =
        listOf(
            Case(example, 2L),
            Case(puzzleInput, 1022L),
        )

    testCases(secondCases) { input -> solvePart2(input) }
}
