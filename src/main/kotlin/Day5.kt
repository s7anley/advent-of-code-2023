import Resources.resourceAsListOfString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

private typealias Almanac = MutableMap<String, MutableList<Pair<LongRange, Long>>>

private data class Interval(val start: Long, val end: Long)

private fun createAlmanacFromInput(input: List<String>): Almanac {
    var currentSection = ""
    val almanac: Almanac = mutableMapOf()

    input.forEach lineEach@{ line ->
        if (line.isEmpty()) {
            currentSection = ""
            return@lineEach
        }

        val titleParts = line.split(" map:")
        if (titleParts.size == 2) {
            currentSection = titleParts.first()
            almanac[currentSection] = mutableListOf()
            return@lineEach
        }

        val (first, second, range) = extractNumbers(line)
        val keyRange = second ..< second + range
        almanac[currentSection]!!.add(keyRange to first)
    }

    return almanac
}

private fun calculateLocation(seed: Long, almanac: Almanac): Long {
    var value = seed
    almanac.forEach mappingsEach@{ _, mappingRanges ->
        mappingRanges.forEach { mappingRange ->
            if (value in mappingRange.first) {
                val offset = value - mappingRange.first.first
                value = mappingRange.second + offset
                return@mappingsEach
            }
        }
    }

    return value
}

private fun solvePart1(input: List<String>): Long {
    val seeds = extractNumbers(input.first())
    val almanac = createAlmanacFromInput(input.drop(2))

    return seeds.minOf { seed -> calculateLocation(seed, almanac) }
}

private fun solvePart2(input: List<String>): Long {
    val seeds = extractNumbers(input.first())
    val almanac = createAlmanacFromInput(input.drop(2))

    val intervalWithLowestLocation =
        seeds
            .windowed(size = 2, step = 2)
            .map { (start, duration) -> Interval(start, start + duration - 1) }
            .minBy { interval ->
                listOf(interval.start, interval.end).minOf { seed -> calculateLocation(seed, almanac) }
            }

    val finalSeeds = intervalWithLowestLocation.start..intervalWithLowestLocation.end
    return finalSeeds.minOf { seed -> calculateLocation(seed, almanac) }
}

private fun solvePart2WithCoroutines(input: List<String>): Long {
    val seeds = extractNumbers(input.first())
    val almanac = createAlmanacFromInput(input.drop(2))
    return runBlocking {
        seeds
            .windowed(size = 2, step = 2)
            .map { (start, duration) -> Interval(start, start + duration - 1) }
            .map { interval ->
                async(Dispatchers.Default) {
                    (interval.start..interval.end).minOf { seed -> calculateLocation(seed, almanac) }
                }
            }
            .awaitAll()
            .min()
    }
}

fun main() {
    val example = resourceAsListOfString("day5-example.txt")
    val puzzleInput = resourceAsListOfString("day5.txt")

    val firstCases =
        listOf(
            Case(example, 35L),
            Case(puzzleInput, 825516882L),
        )

    testCases(firstCases) { input -> solvePart1(input) }

    val secondCases =
        listOf(
            Case(example, 46L),
            Case(puzzleInput, 136096660L),
        )

    testCases(secondCases) { input -> solvePart2(input) }
    testCases(secondCases) { input -> solvePart2WithCoroutines(input) }
}
