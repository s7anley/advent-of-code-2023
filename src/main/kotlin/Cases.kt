import org.junit.jupiter.api.Assertions.assertEquals

data class Case<I, O>(val input: I, val expected: O)

fun <I, O> testCases(cases: List<Case<I, O>>, testFunc: (I) -> O) {
    cases.forEach { case ->
        val result = testFunc(case.input)
        println(result)
        assertEquals(case.expected, result) { "Test failed. Expected: ${case.expected}, but got: $result" }
    }
}
