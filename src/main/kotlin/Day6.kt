import Resources.resourceAsListOfString

private data class Race(val time: Long, val distanceRecord: Long)

private fun extractNumbers(str: String): List<Long> = "\\d+".toRegex().findAll(str).map { it.value.toLong() }.toList()

private fun extractAsOneNumber(str: String): Long = extractNumbers(str).joinToString(separator = "").toLong()

private fun solvePart1(input: List<String>): Int {
    val (timeList, distanceList) = input.map { extractNumbers(it) }
    val races = timeList.zip(distanceList) { time, distance -> Race(time, distance) }

    return races.fold(1) { acc, race -> acc * findNumberOfTimesToBeatRecord(race.time, race.distanceRecord) }
}

private fun findNumberOfTimesToBeatRecord(time: Long, distanceRecord: Long): Int =
    (0..time).map { i -> i * (time - i) }.filter { distance -> distance > distanceRecord }.size

private fun solvePart2(input: List<String>): Int {
    val time = extractAsOneNumber(input[0])
    val distance = extractAsOneNumber(input[1])

    return findNumberOfTimesToBeatRecord(time, distance)
}

fun main() {
    val example = resourceAsListOfString("day6-example.txt")
    val puzzleInput = resourceAsListOfString("day6.txt")

    val firstCases =
        listOf(
            Case(example, 288),
            Case(puzzleInput, 2612736),
        )

    testCases(firstCases) { input -> solvePart1(input) }

    val secondCases =
        listOf(
            Case(example, 71503),
            Case(puzzleInput, 29891250),
        )

    testCases(secondCases) { input -> solvePart2(input) }
}
