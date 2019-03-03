package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.util.*

class Rational(numerator: BigInteger, denominator: BigInteger) : Comparable<Rational> {
    companion object {
        val ZERO = Rational(BigInteger.ZERO, BigInteger.ONE)
    }

    private val numerator: BigInteger
    private val denominator: BigInteger

    constructor(number: Int) : this(number.toBigInteger(), BigInteger.ONE)
    constructor(number: Long) : this(number.toBigInteger(), BigInteger.ONE)
    constructor(number: BigInteger) : this(number, BigInteger.ONE)

    init {
        if (denominator == BigInteger.ZERO) {
            throw IllegalArgumentException("Denominator is zero")
        }
        val (num, den) = normalize(numerator, denominator)
        this.numerator = num
        this.denominator = den
    }

    private fun normalize(numerator: BigInteger, denominator: BigInteger): Pair<BigInteger, BigInteger> {
        val gcd = gcd(numerator, denominator)
        var num = numerator / gcd
        var den = denominator / gcd
        if (den < BigInteger.ZERO) {
            num = -num
            den = -den
        }
        return Pair(num, den)
    }

    private fun gcd(m: BigInteger, n: BigInteger): BigInteger {
        val a = m.abs()
        val b = n.abs()
        return if (b == BigInteger.ZERO) a else gcd(b, a % b)
    }

    private fun lcm(m: BigInteger, n: BigInteger): BigInteger {
        val a = m.abs()
        val b = n.abs()
        return a * (b / gcd(a, b))
    }

    operator fun unaryMinus(): Rational = Rational(-numerator, denominator)

    operator fun plus(b: Rational): Rational {
        val a = this

        // special cases
        if (a == ZERO) return b
        if (b == ZERO) return a

        // Find gcd of numerators and denominators
        val f = gcd(a.numerator, b.numerator)
        val g = gcd(a.denominator, b.denominator)

        // add cross-product terms for numerator
        val num = f * ((a.numerator / f) * (b.denominator / g) + (b.numerator / f) * (a.denominator / g))
        val den = lcm(a.denominator, b.denominator)
        return Rational(num, den)
    }

    private fun negate(): Rational = Rational(-numerator, denominator)

    operator fun minus(b: Rational): Rational {
        val a = this
        return a.plus(b.negate())
    }

    operator fun times(b: Rational): Rational {
        val a = this
        // reduce p1/q2 and p2/q1, then multiply
        val c = Rational(a.numerator, b.denominator)
        val d = Rational(b.numerator, a.denominator)
        return Rational(c.numerator * d.numerator, c.denominator * d.denominator)
    }

    private fun reciprocal(): Rational = Rational(denominator, numerator)

    operator fun div(b: Rational): Rational {
        val a = this
        return a.times(b.reciprocal())
    }

    override fun toString(): String =
        if (denominator == BigInteger.ONE) "$numerator" else "$numerator/$denominator"

    override fun hashCode(): Int {
        return Objects.hash(numerator, denominator)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return compareTo(other as Rational) == 0
    }

    override fun compareTo(other: Rational): Int {
        val lhs = this.numerator * other.denominator
        val rhs = this.denominator * other.numerator
        return lhs.compareTo(rhs)
    }
}

fun String.toRational(): Rational {
    val args = this.split('/', limit = 2)
    val num = args[0].toBigInteger()
    return if (args.size == 1)
        Rational(num)
    else {
        val den = args[1].toBigInteger()
        Rational(num, den)
    }
}

infix fun Int.divBy(number: Int): Rational = Rational(this) / Rational(number)
infix fun Long.divBy(number: Long): Rational = Rational(this) / Rational(number)
infix fun BigInteger.divBy(number: BigInteger): Rational = Rational(this) / Rational(number)

fun main() {
    val r1 = 1 divBy 2
    val r2 = 2000000000L divBy 4000000000L
    println(r1 == r2)

    println((2 divBy 1).toString() == "2")

    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    println("1/2".toRational() - "1/3".toRational() == "1/6".toRational())
    println("1/2".toRational() + "1/3".toRational() == "5/6".toRational())

    println(-(1 divBy 2) == (-1 divBy 2))

    println((1 divBy 2) * (1 divBy 3) == "1/6".toRational())
    println((1 divBy 2) / (1 divBy 4) == "2".toRational())

    println((1 divBy 2) < (2 divBy 3))
    println((1 divBy 2) in (1 divBy 3)..(2 divBy 3))

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
                "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}