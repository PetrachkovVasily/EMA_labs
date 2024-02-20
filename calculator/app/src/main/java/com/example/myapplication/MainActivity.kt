package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import kotlin.math.pow

class MainActivity : AppCompatActivity(), View.OnClickListener
{

    private lateinit var calculationView : TextView
    private lateinit var resultView : TextView


    private var tokens : MutableList<Token> = mutableListOf()
    private var bracketsCountDiff: Int = 0

    private var calculationException : Exception? = null

    private class Token(var type : TokenType, var value : String)

    private enum class TokenType
    {
        NUMBER, OPERATION, OPENBRACKET, CLOSEBRACKET
    }

    private val operationPriority : Map<String, Int> = mapOf(
        "(" to 1,
        "+" to 2,
        "-" to 2,
        "*" to 3,
        "/" to 3,
        "^" to 4,
    )

    private val operations : Map<String, (first : String, second : String) -> String> = mapOf(
        "+" to ::calculatePlus,
        "-" to ::calculateMinus,
        "*" to ::calculateMultiplication,
        "/" to ::calculateDivision,
        "^" to ::calculateDegree,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setEventListeners()

        calculationView = findViewById(R.id.inputOperations)
        resultView = findViewById(R.id.inputSolution)
    }

    private fun setEventListeners()
    {
        val layout = findViewById<RelativeLayout>(R.id.layoutMain)
        for( btn in layout.children.filter { c -> c is Button })
        {
            btn.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?)
    {

        when (v?.tag){
            getString(R.string.digitBtnTag) -> inputDigit(v as Button)
            getString(R.string.opBtnTag) -> inputAction(v as Button)
            getString(R.string.equalBtnTag) -> execute()
            getString(R.string.bracketsBtnTag) -> inputBrackets()
            getString(R.string.clearBtnTag) -> deleteAll()
            getString(R.string.backspaceBtnTag) -> deleteLast()
        }

    }

    private fun inputDigit(digitButton: Button)
    {
        val digit = digitButton.text
        if(digit == "." && !isPointPermitted()) return

        if(tokens.isEmpty() || tokens.last().type != TokenType.NUMBER)
        {
            tokens.add( Token(TokenType.NUMBER, ""))
        }

        tokens.last().value += digit
        calculationView.append(digit)

        resultView.text = calculate()
    }

    private fun inputAction(opButton: Button)
    {
        if( tokens.isEmpty() || tokens.last().type == TokenType.OPENBRACKET) return

        if( tokens.last().type != TokenType.OPERATION )
        {
            tokens.add( Token(TokenType.OPERATION, ""))
        }

        val operation = opButton.text.toString()

        if(tokens.last().value.isNotBlank())
        {
            val currViewText = calculationView.text
            calculationView.text = currViewText.substring(0, currViewText.length - 1)
        }

        calculationView.append(operation)
        tokens.last().value = operation
    }

    private fun isPointPermitted() : Boolean
    {
        return tokens.isNotEmpty() &&
                tokens.last().type == TokenType.NUMBER &&
                !tokens.last().value.contains('.')
    }

    private fun deleteLast()
    {
        if (calculationView.text.isNullOrBlank()) return

        val currToken: String = tokens.last().value

        if(tokens.last().type == TokenType.OPENBRACKET) bracketsCountDiff--
        else if(tokens.last().type == TokenType.CLOSEBRACKET) bracketsCountDiff++

        tokens.last().value = currToken.substring(0, currToken.lastIndex)

        if (tokens.last().value.isBlank())
        {
            tokens.removeLast()
        }

        calculationView.text = calculationView.text.substring(0, calculationView.text.lastIndex)
        resultView.text = calculate()
    }

    private fun deleteAll()
    {
        calculationView.text = ""
        resultView.text = ""
        bracketsCountDiff = 0
        tokens.clear()
    }

    private fun calculate() : String
    {
        if(tokens.isEmpty()) return ""

        val rpn : MutableList<Token> = buildRPN()

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

            try
            {
                rpn[ind] = Token(TokenType.NUMBER, operation.invoke(firstOperand, secondOperand))
            }
            catch (ex: Exception)
            {
                calculationException = ex
                return ""
            }

            rpn.removeAt(ind - 1)
            rpn.removeAt(ind - 2)

            ind--
        }

        calculationException = null
        return rpn.last().value
    }


    private fun buildRPN() : MutableList<Token>
    {
        val rpn: MutableList<Token> = mutableListOf()

        val opStack: ArrayDeque<Token> = ArrayDeque()

        for (token: Token in getValidExpression())
        {
            when (token.type)
            {
                TokenType.OPENBRACKET -> opStack.addLast(token)
                TokenType.CLOSEBRACKET -> {
                    while (opStack.last().type != TokenType.OPENBRACKET) {
                        rpn.add(opStack.removeLast())
                    }
                    opStack.removeLast()
                }

                TokenType.NUMBER -> rpn.add(token)
                TokenType.OPERATION -> {
                    while (opStack.isNotEmpty() &&
                        operationPriority[opStack.last().value]!! > operationPriority[token.value]!!
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

    private fun getValidExpression(): MutableList<Token>
    {
        val validExpression = tokens.toMutableList()

        var bracketDiff = bracketsCountDiff

        while (validExpression.last().type == TokenType.OPERATION ||
            validExpression.last().type == TokenType.OPENBRACKET)
        {
            if(validExpression.last().type == TokenType.OPENBRACKET) bracketDiff--
            validExpression.removeLast()
        }
        for (i in 1..bracketDiff)
        {
            validExpression.add(Token(TokenType.CLOSEBRACKET, ")"))
        }

        return validExpression
    }

    private fun inputBrackets()
    {
        if (tokens.isEmpty() ||
            (tokens.last().type != TokenType.CLOSEBRACKET && tokens.last().type != TokenType.NUMBER))
        {
            tokens.add(Token(TokenType.OPENBRACKET, "("))
            calculationView.append("(")
            bracketsCountDiff++
        }
        else if(bracketsCountDiff > 0)
        {
            tokens.add(Token(TokenType.CLOSEBRACKET, ")"))
            calculationView.append(")")
            bracketsCountDiff--
        }
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
            throw Exception("Can not divide by zero")
        }
        return (first.toDouble() / second.toDouble()).toString()
    }

    private fun calculateDegree(first: String, second: String): String
    {
        return ( first.toDouble().pow(second.toDouble()) ).toString()
    }

    private fun execute()
    {
        val res : String = calculate()

        if(calculationException != null)
        {
            val toast = Toast.makeText(applicationContext, calculationException?.message,
                Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        deleteAll()
        calculationView.text = res
        tokens.add(Token(TokenType.NUMBER, res))

    }

}