import Resources.resourceAsListOfString

private val digits =
    mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
        "zero" to "0",
    )

private fun extractNumberFromString(s: String, extractor: (String) -> Pair<Int, String>?): String {
    val result = extractor(s)
    val matched = result?.second ?: return "0"

    return digits[matched] ?: matched
}

private fun solvePart1(input: List<String>): Int =
    input.sumOf { line ->
        (extractNumberFromString(line) { line.findAnyOf(digits.values) } +
                extractNumberFromString(line) { line.findLastAnyOf(digits.values) })
            .toInt()
    }

private fun solvePart2(input: List<String>): Int =
    input.sumOf { line ->
        (extractNumberFromString(line) { line.findAnyOf(digits.values + digits.keys) } +
                extractNumberFromString(line) { line.findLastAnyOf(digits.values + digits.keys) })
            .toInt()
    }

fun main() {
    val firstCases =
        listOf(
            Case(resourceAsListOfString("day1-example-part1.txt"), 142),
            Case(resourceAsListOfString("day1.txt"), 56465)
        )

    testCases(firstCases) { input -> solvePart1(input) }

    val secondCases =
        listOf(
            Case(resourceAsListOfString("day1-example-part2.txt"), 281),
            Case(resourceAsListOfString("day1.txt"), 55902)
        )

    testCases(secondCases) { input -> solvePart2(input) }
}
