package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

//fun String.toRational(): Rational {
//    return if ('/' in this) {
//        val index = this.indexOf('/')
//        val n = this.substring(0, index).toBigInteger()
//        val d = this.substring(index + 1).toBigInteger()
//        Rational(n, d)
//    } else
//        Rational(this.toBigInteger(), 1.toBigInteger())
//}

fun String.toRational(): Rational {
    fun String.toBigIntegerOrFail() =
            toBigIntegerOrNull() ?: throw IllegalArgumentException(
                    "Expecting rational in the form ig 'n/d', or 'n', was: '${this@toRational}'"
            )
    if (!contains("/"))
        return Rational(toBigIntegerOrFail(), BigInteger.ONE)
    val parts = split("/")
    return Rational(parts[0].toBigInteger(), parts[1].toBigInteger())
}

///
infix fun BigInteger.divBy(denominator: BigInteger) : Rational {
    return Rational(this, denominator)
}

infix fun Int.divBy(denominator: Int) : Rational {
    return Rational(this.toBigInteger(), denominator.toBigInteger())
}

infix fun Long.divBy(denominator: Long) : Rational {
    return Rational(this.toBigInteger(), denominator.toBigInteger())
}
///

//@Suppress("DataClassPrivateConstructor")
//data class Rational
//private constructor(private val numerator: BigInteger, private val denominator: BigInteger) : Comparable<Rational> {
//
//    companion object {
//        fun create(numerator: BigInteger, denominator: BigInteger) = normalize(numerator, denominator)
//        private fun normalize(numerator: BigInteger, denominator: BigInteger) : Rational {
//            val gcd = numerator.gcd(denominator)
//            val sign = denominator.signum().toBigInteger()
//            return Rational(numerator / gcd * sign, denominator / gcd * sign)
//        }
//    }

class Rational(n: BigInteger, d: BigInteger) : Comparable<Rational> {
    val numerator : BigInteger
    val denominator : BigInteger

    init {
        require(d != BigInteger.ZERO) {
            "Denominator must be non-zero"
        }
        val g = n.gcd(d)
        val sign = d.signum().toBigInteger()
        numerator = n / g * sign
        denominator = d / g * sign
//        println("Rational number $numerator/$denominator was created!")
    }

    operator fun unaryMinus() = Rational(-numerator, denominator)

    operator fun plus(rational: Rational) =
            (numerator.times(rational.denominator)
                    .plus((rational.numerator).times(denominator))) divBy rational.denominator * denominator

    operator fun minus(rational: Rational) =
            (numerator.times(rational.denominator)
                    .minus((rational.numerator).times(denominator))) divBy rational.denominator * denominator

    operator fun times(rational: Rational) =
            numerator.times(rational.numerator) divBy denominator.times(rational.denominator)

    operator fun div(rational: Rational) =
            numerator.times(rational.denominator) divBy denominator.times(rational.numerator)

    override fun compareTo(other: Rational) =
            numerator.times(other.denominator).compareTo(other.numerator.times(denominator))

    override fun toString(): String {
        return if (denominator == 1.toBigInteger()) {
            "$numerator"
        }else
            "$numerator/$denominator"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        if (numerator != other.numerator) return false
        if (denominator != other.denominator) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

}