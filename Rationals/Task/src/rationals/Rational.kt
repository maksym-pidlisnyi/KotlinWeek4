package rationals

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

fun String.toRational(): Rational {
    return if ('/' in this) {
        val index = this.indexOf('/')
        val n = this.substring(0, index).toBigInteger()
        val d = this.substring(index + 1).toBigInteger()
        Rational(n, d)
    } else
        Rational(this.toBigInteger(), 1.toBigInteger())
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

data class Rational(var numerator: BigInteger, var denominator: BigInteger) : Comparable<Rational> {

    init {
        normalize(this)
//        println("Rational number $numerator/$denominator was created!")
    }

    private fun normalize(rational: Rational) : Rational {
        val gcd = rational.numerator.gcd(denominator)
        val sign = rational.denominator.signum().toBigInteger()
        rational.numerator /= gcd * sign
        rational.denominator /= gcd * sign
        return rational
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

}
