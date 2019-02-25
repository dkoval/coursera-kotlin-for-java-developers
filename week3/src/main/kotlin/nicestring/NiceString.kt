package nicestring

fun String.isNice(): Boolean {
    val predicates = listOf(
        ::doesNotContainPredefinedSubstrings,
        ::containsAtLeastThreeVowels,
        ::containsDoubleLetter
    )
    return predicates.count { run(it) } >= 2
}

private fun String.doesNotContainPredefinedSubstrings(): Boolean {
    val containsPredefinedStrings = this.contains("bu", true)
            || this.contains("ba", true)
            || this.contains("be", true)
    return !containsPredefinedStrings
}

private fun String.containsAtLeastThreeVowels(): Boolean {
    return this.count { it == 'a' || it == 'e' || it == 'i' || it == 'o' || it == 'u' } >= 3
}

private fun String.containsDoubleLetter(): Boolean {
    for (i in 0 until this.length - 1) {
        if (this[i] == this[i + 1]) return true
    }
    return false
}
