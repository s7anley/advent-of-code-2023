import Resources.resourceAsListOfString

private val cubeLimits =
    mapOf(
        "blue" to 14,
        "green" to 13,
        "red" to 12,
    )

private val pattern = Regex("(\\d+)\\s*(red|blue|green)")

private fun emptyCubes() = mutableMapOf("blue" to 0, "green" to 0, "red" to 0)

private fun solvePart1(input: List<String>): Int {
    var result = 0
    input.forEachIndexed lineEach@{ index, line ->
        result += index + 1
        line.split(";").forEach { part ->
            val cubesInSet = emptyCubes()
            pattern.findAll(part).forEach { match ->
                val (number, color) = match.destructured
                cubesInSet[color] = cubesInSet[color]!! + number.toInt()
            }

            val limitReached = cubeLimits.all { (color, limit) -> cubesInSet[color]!! <= limit }

            if (!limitReached) {
                result -= index + 1
                return@lineEach
            }
        }
    }

    return result
}

private fun solvePart2(input: List<String>): Int =
    input.sumOf { line ->
        val cubesInGame = emptyCubes()
        pattern.findAll(line).forEach { match ->
            val (number, color) = match.destructured
            cubesInGame[color] = maxOf(cubesInGame[color]!!, number.toInt())
        }
        cubesInGame.values.reduce { acc, i -> acc * i }
    }

fun main() {
    val example = resourceAsListOfString("day2-example.txt")
    val puzzleInput = resourceAsListOfString("day2.txt")

    val firstCases =
        listOf(
            Case(example, 8),
            Case(puzzleInput, 2156),
        )

    testCases(firstCases) { input -> solvePart1(input) }

    val secondCases =
        listOf(
            Case(example, 2286),
            Case(puzzleInput, 66909),
        )

    testCases(secondCases) { input -> solvePart2(input) }
}
