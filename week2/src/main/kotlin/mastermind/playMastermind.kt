package mastermind

import java.util.*

val ALPHABET = 'A'..'F'
val CODE_LENGTH = 4

fun main(args: Array<String>) {
    playBullsAndCows()
}

fun playBullsAndCows(
        secret: String = generateSecret()
) {
    val scanner = Scanner(System.`in`)
    var evaluation: Evaluation

    do {
        print("Your guess: ")
        var guess = scanner.next()
        while (hasErrorsInInput(guess)) {
            println("Incorrect input: $guess. " +
                    "It should consist of ${CODE_LENGTH} digits. " +
                    "Try again.")
            guess = scanner.next()
        }
        evaluation = evaluateGuess(secret, guess)
        if (evaluation.isComplete()) {
            println("You are correct!")
        } else {
            println("Positions: ${evaluation.positions}; letters: ${evaluation.letters}.")
        }

    } while (!evaluation.isComplete())
}

fun Evaluation.isComplete(): Boolean = positions == CODE_LENGTH

fun hasErrorsInInput(guess: String): Boolean {
    val possibleLetters = ALPHABET.toSet()
    return guess.length != CODE_LENGTH || guess.any { it !in possibleLetters }
}

fun generateSecret(differentLetters: Boolean = false): String {
    val chars = ALPHABET.toMutableList()
    val random = Random()
    return buildString {
        for (i in 1..CODE_LENGTH) {
            val letter = chars[random.nextInt(chars.size)]
            append(letter)
            if (differentLetters) {
                chars.remove(letter)
            }
        }
    }
}
