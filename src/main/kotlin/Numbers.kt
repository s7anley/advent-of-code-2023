fun extractNumbers(str: String): List<Long> = "-?\\d+".toRegex().findAll(str).map { it.value.toLong() }.toList()

fun extractAsOneNumber(str: String): Long = extractNumbers(str).joinToString(separator = "").toLong()
