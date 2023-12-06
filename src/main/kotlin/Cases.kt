import kotlin.system.measureTimeMillis
import org.junit.jupiter.api.Assertions.assertEquals

data class Case<I, O>(val input: I, val expected: O)

fun <I, O> testCases(cases: List<Case<I, O>>, testFunc: (I) -> O) {
    cases.forEachIndexed { index, case ->
        val timeTaken = measureTimeMillis {
            val result = testFunc(case.input)
            println(result)
            assertEquals(case.expected, result) { "Test failed. Expected: ${case.expected}, but got: $result" }
        }
        val timeTakenSeconds = timeTaken / 1000.0
        println("Time taken for case ${index + 1}:  ${"%.3f".format(timeTakenSeconds)} seconds")
    }
}
