import Resources.resourceAsListOfString

private data class Directions(private val directions: String) {
    private var index = 0

    fun next(): Char {
        val direction = directions[index]
        index = (index + 1) % directions.length
        return direction
    }
}

private fun createNetwork(input: List<String>): Map<String, Pair<String, String>> {
    return input.associate { line ->
        val (start, left, right) = "[A-Z0-9]+".toRegex().findAll(line).map { it.value }.toList()
        start to (left to right)
    }
}

private fun findNumberOfSteps(
    startingNode: String,
    network: Map<String, Pair<String, String>>,
    directions: Directions
): Long {
    var steps = 0L
    var node = startingNode
    while (!node.endsWith("Z")) {
        val direction = directions.next()
        val (left, right) = network[node] ?: error("Cannot find node $node in network.")

        node = if (direction == 'R') right else left

        steps++
    }

    return steps
}

private fun solvePart1(input: List<String>): Long {
    val directions = Directions(input.first())
    val network = createNetwork(input.drop(2))
    val startingNode = "AAA"

    return findNumberOfSteps(startingNode, network, directions)
}

private fun solvePart2(input: List<String>): Long {
    val directions = Directions(input.first())
    val map = createNetwork(input.drop(2))
    val startingNodes = map.keys.filter { it.endsWith("A") }

    return startingNodes
        .map { startingNode -> findNumberOfSteps(startingNode, map, directions) }
        .fold(1L) { acc, i -> lcm(acc, i) }
}

fun main() {
    val puzzleInput = resourceAsListOfString("day8.txt")

    val firstCases =
        listOf(
            Case(resourceAsListOfString("day8-example.txt"), 6L),
            Case(puzzleInput, 20221L),
        )

    testCases(firstCases) { input -> solvePart1(input) }

    val secondCases =
        listOf(
            Case(resourceAsListOfString("day8-example-part2.txt"), 6L),
            Case(puzzleInput, 14616363770447L),
        )

    testCases(secondCases) { input -> solvePart2(input) }
}
