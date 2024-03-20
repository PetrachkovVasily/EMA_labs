package com.example.myapplication

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.Exception


class CalculatorTest
{

    private lateinit var calculator: Calculator


    @Before
    fun setUp()
    {
        calculator = Calculator()
    }

    @After
    fun tearDown()
    {

    }

    @Test
    fun unfinishedDecimal()
    {

        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "111."),
            Token(TokenType.OPERATION, value = "+"),
            Token(TokenType.NUMBER, value = "22"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("133", res)
    }

    @Test
    fun addition()
    {

        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "111"),
            Token(TokenType.OPERATION, value = "+"),
            Token(TokenType.NUMBER, value = "222"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("333", res)
    }

    @Test
    fun negativeAddition()
    {

        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "-111"),
            Token(TokenType.OPERATION, value = "+"),
            Token(TokenType.NUMBER, value = "-222"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("-333", res)
    }

    @Test
    fun decimalAddition()
    {

        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "111.23"),
            Token(TokenType.OPERATION, value = "+"),
            Token(TokenType.NUMBER, value = "222.51"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("333.74", res)
    }

    @Test
    fun diffSignDecimalAddition()
    {

        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "-111.23"),
            Token(TokenType.OPERATION, value = "+"),
            Token(TokenType.NUMBER, value = "222.51"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("111.28", res)
    }

    @Test
    fun subtraction()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "333"),
            Token(TokenType.OPERATION, value = "-"),
            Token(TokenType.NUMBER, value = "222"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("111", res)
    }

    @Test
    fun negativeSubtraction()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "333"),
            Token(TokenType.OPERATION, value = "-"),
            Token(TokenType.NUMBER, value = "-222"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("555", res)
    }


    @Test
    fun multiplication()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "4"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.NUMBER, value = "5"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("20", res)
    }

    @Test
    fun zeroMultiplication()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "4"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.NUMBER, value = "0"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("0", res)
    }

    @Test
    fun negativeMultiplication()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "-4"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.NUMBER, value = "-5"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("20", res)
    }

    @Test
    fun diffSignedMultiplication()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "-4"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.NUMBER, value = "5"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("-20", res)
    }

    @Test
    fun decimalMultiplication()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "4.432"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.NUMBER, value = "5.867"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("26.002544", res)
    }

    @Test
    fun multiplicationByZero()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "4"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.NUMBER, value = "0"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("0", res)
    }

    @Test
    fun division()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "100"),
            Token(TokenType.OPERATION, value = "/"),
            Token(TokenType.NUMBER, value = "5"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("20", res)
    }

    @Test
    fun diffSignedDivision()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "-20"),
            Token(TokenType.OPERATION, value = "/"),
            Token(TokenType.NUMBER, value = "5"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("-4", res)
    }

    @Test
    fun divisionWithRemainder()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "100"),
            Token(TokenType.OPERATION, value = "/"),
            Token(TokenType.NUMBER, value = "6"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("16.66666667", res)
    }

    @Test
    fun negativeRemainder()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "-100"),
            Token(TokenType.OPERATION, value = "/"),
            Token(TokenType.NUMBER, value = "-5"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("20", res)
    }

    @Test
    fun decimalDivision()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "10.61632"),
            Token(TokenType.OPERATION, value = "/"),
            Token(TokenType.NUMBER, value = "-2.616"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("-4.0582263", res)
    }

    @Test
    fun leadingZerosRemoval()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "000010.61632")
        )

        val res = calculator.calculate(tokens)

        assertEquals("10.61632", res)
    }

    @Test
    fun trailingZerosRemoval()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "10.616320000")
        )

        val res = calculator.calculate(tokens)

        assertEquals("10.61632", res)
    }

    @Test(expected = Exception::class)
    fun zeroDivision()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "100"),
            Token(TokenType.OPERATION, value = "/"),
            Token(TokenType.NUMBER, value = "0"),
        )

        calculator.calculate(tokens)
    }

    @Test(expected = Exception::class)
    fun zeroDivisionInBrackets()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "100"),
            Token(TokenType.OPERATION, value = "/"),
            Token(TokenType.OPEN_BRACKET, value = "("),
            Token(TokenType.NUMBER, value = "3"),
            Token(TokenType.OPERATION, value = "-"),
            Token(TokenType.NUMBER, value = "3"),
            Token(TokenType.CLOSE_BRACKET, value = ")"),
        )

        calculator.calculate(tokens)
    }

    @Test
    fun power()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "^"),
            Token(TokenType.NUMBER, value = "4"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("16", res)
    }

    @Test
    fun bracketsPower()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "^"),
            Token(TokenType.OPEN_BRACKET, value = "("),
            Token(TokenType.NUMBER, value = "4"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.CLOSE_BRACKET, value = ")"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("256", res)
    }


    @Test
    fun zeroPower()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "^"),
            Token(TokenType.NUMBER, value = "0"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("1", res)
    }

    @Test
    fun zeroInPower()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "0"),
            Token(TokenType.OPERATION, value = "^"),
            Token(TokenType.NUMBER, value = "100"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("0", res)
    }

    @Test
    fun negativePower()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "^"),
            Token(TokenType.NUMBER, value = "-2"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("0.25", res)
    }

    @Test
    fun decimalPower()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "^"),
            Token(TokenType.NUMBER, value = "2.4"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("5.27803164", res)
    }

    @Test
    fun negativeDecimalPower()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "^"),
            Token(TokenType.NUMBER, value = "-2.4"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("0.18946457", res)
    }

    @Test
    fun decimalNumberInPower()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2.4"),
            Token(TokenType.OPERATION, value = "^"),
            Token(TokenType.NUMBER, value = "2.4"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("8.17536178", res)
    }

    @Test
    fun ordering()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "+"),
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "/"),
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "^"),
            Token(TokenType.NUMBER, value = "2"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("3", res)
    }

    @Test
    fun brackets()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.OPEN_BRACKET, value = "("),
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "+"),
            Token(TokenType.NUMBER, value = "3"),
            Token(TokenType.CLOSE_BRACKET, value = ")"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("10", res)
    }

    @Test
    fun nestedBrackets()
    {
        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.OPEN_BRACKET, value = "("),
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "+"),
            Token(TokenType.OPEN_BRACKET, value = "("),
            Token(TokenType.NUMBER, value = "2"),
            Token(TokenType.OPERATION, value = "-"),
            Token(TokenType.OPEN_BRACKET, value = "("),
            Token(TokenType.NUMBER, value = "3"),
            Token(TokenType.OPERATION, value = "*"),
            Token(TokenType.NUMBER, value = "7"),
            Token(TokenType.CLOSE_BRACKET, value = ")"),
            Token(TokenType.CLOSE_BRACKET, value = ")"),
            Token(TokenType.CLOSE_BRACKET, value = ")"),
        )

        val res = calculator.calculate(tokens)

        assertEquals("-34", res)
    }
}