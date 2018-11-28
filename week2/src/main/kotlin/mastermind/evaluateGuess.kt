package mastermind

data class Evaluation(val positions: Int, val letters: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val positions = secret.zip(guess).count { p -> p.first == p.second }
    val commonLetters = "ABCDEF".sumBy { ch ->
        Math.min(secret.count { it -> it == ch }, guess.count { it -> it == ch })
    }
    return Evaluation(positions, commonLetters - positions)
}
