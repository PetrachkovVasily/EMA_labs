package com.example.myapplication

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


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
    fun addition()
    {

        val tokens : MutableList<Token> = mutableListOf(
            Token(TokenType.NUMBER, value = "111"),
            Token(TokenType.OPERATION, value = "+"),
            Token(TokenType.NUMBER, value = "222"),
        )

        val res = calculator.calculate(tokens)

        assertEquals(res, "333")
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

        assertEquals(res, "111")
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

        assertEquals(res, "20")
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

        assertEquals(res, "20")
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

        assertEquals(res, "16")
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

        assertEquals(res, "3")
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

        assertEquals(res, "10")
    }
}