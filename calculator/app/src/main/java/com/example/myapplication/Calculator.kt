package com.example.myapplication

import kotlin.math.pow

private const val PLUS = "+"
private const val MINUS = "-"
private const val MULTIPLICATION = "*"
private const val DIVISION = "/"
private const val POW = "^"
private const val OPEN_BRACKET = "("
private const val DIVISION_BY_ZERO_EXCEPTION = "Divide by zero"
private const val LIMIT_EXCEPTION = "Beyond limit"
private const val OUTPUT_FORMAT = "0.########"
private const val MAX_VALUE : Double  = 99999999999999.0

open class Calculator
{


    private var operations : Map<String, (first : String, second : String) -> String> = mapOf(
        PLUS to ::calculatePlus,
        MINUS to ::calculateMinus,
        MULTIPLICATION to ::calculateMultiplication,
        DIVISION to ::calculateDivision,
        POW to ::calculateDegree,
    )

    private var operationPriority : Map<String, Int> = mapOf(
        OPEN_BRACKET to 1,
        PLUS to 2,
        MINUS to 2,
        MULTIPLICATION to 3,
        DIVISION to 3,
        POW to 4,
    )


    fun calculate(tokens : MutableList<Token>) : String
    {
        if(tokens.isEmpty()) return ""

        val rpn : MutableList<Token> = buildRPN(tokens)

        var ind = 0
        while (rpn.count() > 1)
        {
            if(rpn[ind].type != TokenType.OPERATION)
            {
                ind++
                continue
            }

            val firstOperand : String = rpn[ind - 2].value
            val secondOperand : String = rpn[ind - 1].value
            val operation = operations[rpn[ind].value]!!


            rpn[ind] = Token(TokenType.NUMBER, operation.invoke(firstOperand, secondOperand))


            rpn.removeAt(ind - 1)
            rpn.removeAt(ind - 2)

            ind--
        }

        val format = java.text.DecimalFormat(OUTPUT_FORMAT)

        val doubleVal = rpn.last().value.toDouble()

        if(doubleVal > MAX_VALUE || doubleVal < -MAX_VALUE)
        {
            throw Exception(LIMIT_EXCEPTION)
        }

        var res = format.format(doubleVal)

        if(res == "-0") res = "0"

        return res
    }

    private fun buildRPN(tokens : MutableList<Token>) : MutableList<Token>
    {
        val rpn: MutableList<Token> = mutableListOf()

        val opStack: ArrayDeque<Token> = ArrayDeque()

        for (token: Token in tokens)
        {
            when (token.type)
            {
                TokenType.OPEN_BRACKET -> opStack.addLast(token)
                TokenType.CLOSE_BRACKET -> {
                    while (opStack.last().type != TokenType.OPEN_BRACKET) {
                        rpn.add(opStack.removeLast())
                    }
                    opStack.removeLast()
                }

                TokenType.NUMBER -> rpn.add(token)
                TokenType.OPERATION -> {
                    while (opStack.isNotEmpty() &&
                        operationPriority[opStack.last().value]!! >= operationPriority[token.value]!!
                    ) {
                        rpn.add(opStack.removeLast())
                    }
                    opStack.addLast(token)
                }
            }
        }

        while (opStack.isNotEmpty())
        {
            rpn.add(opStack.removeLast())
        }

        return rpn
    }

    private fun calculatePlus(first: String, second: String): String
    {
        return (first.toDouble() + second.toDouble()).toString()
    }

    private fun calculateMinus(first: String, second: String): String
    {
        return (first.toDouble() - second.toDouble()).toString()
    }

    private fun calculateMultiplication(first: String, second: String): String
    {
        return (first.toDouble() * second.toDouble()).toString()
    }

    private fun calculateDivision(first: String, second: String): String
    {
        if (second.toDouble() == 0.0) {
            throw Exception(DIVISION_BY_ZERO_EXCEPTION)
        }
        return (first.toDouble() / second.toDouble()).toString()
    }

    private fun calculateDegree(first: String, second: String): String
    {
        return ( first.toDouble().pow(second.toDouble()) ).toString()
    }
}