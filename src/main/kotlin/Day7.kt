import Resources.resourceAsListOfString

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIRS,
    ONE_PAIR,
    HIGH_CARD
}

private fun characterFrequencyMap(str: String): Map<Char, Int> = str.groupingBy { it }.eachCount()

private fun detectHandType(hand: String): HandType {
    val frequency = characterFrequencyMap(hand)
    val pairs = frequency.count { it.value == 2 }

    return when {
        frequency.containsValue(5) -> HandType.FIVE_OF_A_KIND
        frequency.containsValue(4) -> HandType.FOUR_OF_A_KIND
        frequency.containsValue(3) && frequency.containsValue(2) -> HandType.FULL_HOUSE
        frequency.containsValue(3) -> HandType.THREE_OF_A_KIND
        pairs == 2 -> HandType.TWO_PAIRS
        pairs == 1 -> HandType.ONE_PAIR
        else -> HandType.HIGH_CARD
    }
}

private fun detectHandTypeWithJokers(hand: String): HandType {
    val frequency = characterFrequencyMap(hand)
    val numberOfJokers = frequency.getOrDefault('J', 0)

    if (numberOfJokers == 0) {
        return detectHandType(hand)
    }

    if (numberOfJokers == 5) {
        return HandType.FIVE_OF_A_KIND
    }

    val nonJokerFrequencies = frequency.filterKeys { it != 'J' }
    val hasFourOfAKind = nonJokerFrequencies.containsValue(4)
    val fullHouse = nonJokerFrequencies.containsValue(3) && nonJokerFrequencies.containsValue(2)
    val hasThreeOfAKind = nonJokerFrequencies.containsValue(3)
    val pairs = nonJokerFrequencies.count { it.value == 2 }

    return when (numberOfJokers) {
        1 ->
            when {
                hasFourOfAKind -> HandType.FIVE_OF_A_KIND
                fullHouse || hasThreeOfAKind -> HandType.FOUR_OF_A_KIND
                pairs == 2 -> HandType.FULL_HOUSE
                pairs == 1 -> HandType.THREE_OF_A_KIND
                else -> HandType.ONE_PAIR
            }
        2 ->
            when {
                fullHouse || hasThreeOfAKind -> HandType.FIVE_OF_A_KIND
                pairs == 1 -> HandType.FOUR_OF_A_KIND
                else -> HandType.THREE_OF_A_KIND
            }
        3 ->
            when {
                pairs == 1 -> HandType.FIVE_OF_A_KIND
                else -> HandType.FOUR_OF_A_KIND
            }
        4 -> HandType.FIVE_OF_A_KIND
        else -> throw IllegalArgumentException("Invalid hand format or size")
    }
}

private fun handStrengthComparator(labelToStrength: Map<Char, Int>): Comparator<Pair<String, Int>> =
    compareBy(
        { labelToStrength[it.first[0]]!! },
        { labelToStrength[it.first[1]]!! },
        { labelToStrength[it.first[2]]!! },
        { labelToStrength[it.first[3]]!! },
        { labelToStrength[it.first[4]]!! }
    )

private fun calculateWinnings(
    input: List<String>,
    detectHandType: (hand: String) -> HandType,
    labels: List<Char>
): Int {
    val labelToStrength = labels.withIndex().associate { (index, value) -> value to (labels.size - index) }
    val handsGroupByType =
        input
            .map { line ->
                val (hand, bid) = line.split(" ")
                detectHandType(hand) to (hand to bid.toInt())
            }
            // group by label and removed type from pair
            .groupBy({ it.first }, { it.second })
            .toSortedMap()

    var highestRank = input.size
    return handsGroupByType.values.sumOf { listOfHands ->
        val sortedHandsInGroup = listOfHands.sortedWith(handStrengthComparator(labelToStrength)).reversed()

        sortedHandsInGroup.sumOf { (_, bid) ->
            val result = highestRank * bid
            highestRank--
            result
        }
    }
}

private fun solvePart1(input: List<String>): Int =
    calculateWinnings(input, ::detectHandType, listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'))

private fun solvePart2(input: List<String>): Int =
    calculateWinnings(
        input,
        ::detectHandTypeWithJokers,
        listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
    )

fun main() {
    val example = resourceAsListOfString("day7-example.txt")
    val puzzleInput = resourceAsListOfString("day7.txt")

    val firstCases =
        listOf(
            Case(example, 6440),
            Case(puzzleInput, 256448566),
        )

    testCases(firstCases) { input -> solvePart1(input) }

    val secondCases =
        listOf(
            Case(example, 5905),
            Case(puzzleInput, 254412181),
        )

    testCases(secondCases) { input -> solvePart2(input) }
}
