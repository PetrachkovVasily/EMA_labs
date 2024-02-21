package com.example.myapplication

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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

    private class Token : Parcelable
    {
        var type : TokenType
        var value : String
        constructor(type : TokenType, value : String)
        {
            this.type = type
            this.value = value
        }

        constructor(parcel : Parcel)
        {
            val data : Array<String?> = arrayOfNulls(2)
            parcel.readStringArray(data)
            this.type = TokenType.valueOf(data[0]!!)
            this.value = data[1]!!
        }

        override fun writeToParcel(parcel: Parcel, flags: Int)
        {
            parcel.writeStringArray(arrayOf(type.toString(), value))
        }

        override fun describeContents(): Int
        {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Token>
        {
            override fun createFromParcel(parcel: Parcel): Token
            {
                return Token(parcel)
            }

            override fun newArray(size: Int): Array<Token?> {
                return arrayOfNulls(size)
            }
        }

    }

    private enum class TokenType
    {
        NUMBER, OPERATION, OPENBRACKET, CLOSEBRACKET
    }

    private lateinit var operationPriority : Map<String, Int>

    private lateinit var operations : Map<String, (first : String, second : String) -> String>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setEventListeners()

        calculationView = findViewById(R.id.inputOperations)
        resultView = findViewById(R.id.inputSolution)

        initializeMaps()

        if(savedInstanceState?.isEmpty == false)
        {
            restoreData(savedInstanceState)
        }
    }

    private fun initializeMaps()
    {
        operationPriority = mapOf(
            getString(R.string.openBracket) to 1,
            getString(R.string.plus) to 2,
            getString(R.string.minus) to 2,
            getString(R.string.multiplication) to 3,
            getString(R.string.division) to 3,
            getString(R.string.pow) to 4,
        )

        operations = mapOf(
            getString(R.string.plus) to ::calculatePlus,
            getString(R.string.minus) to ::calculateMinus,
            getString(R.string.multiplication) to ::calculateMultiplication,
            getString(R.string.division) to ::calculateDivision,
            getString(R.string.pow) to ::calculateDegree,
        )
    }

    private fun restoreData(bundle: Bundle)
    {
        calculationView.text = bundle.getString("calculationText")
        resultView.text = bundle.getString("resultText")
        val exMessage = bundle.getString("calculationExceptionMessage")
        if(exMessage != null)
        {
            calculationException = Exception(exMessage)
        }

        bracketsCountDiff = bundle.getInt("bracketsCountDiff")

        @Suppress("DEPRECATION")
        tokens = bundle.getParcelableArrayList("tokens")!!
    }

    override fun onSaveInstanceState(outState: Bundle)
    {

        outState.putParcelableArrayList("tokens", ArrayList(tokens))
        outState.putInt("bracketsCountDiff", bracketsCountDiff)
        outState.putString("calculationExceptionMessage", calculationException?.message)
        outState.putString("calculationText", calculationView.text.toString())
        outState.putString("resultText", resultView.text.toString())

        super.onSaveInstanceState(outState)
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
        if(digit == getString(R.string.point) && !isPointPermitted()) return

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
                !tokens.last().value.contains(getString(R.string.point))
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
            validExpression.add(Token(TokenType.CLOSEBRACKET, getString(R.string.closeBracket)))
        }

        return validExpression
    }

    private fun inputBrackets()
    {
        if (tokens.isEmpty() ||
            (tokens.last().type != TokenType.CLOSEBRACKET && tokens.last().type != TokenType.NUMBER))
        {
            tokens.add(Token(TokenType.OPENBRACKET, getString(R.string.openBracket)))
            calculationView.append(getString(R.string.openBracket))
            bracketsCountDiff++
        }
        else if(bracketsCountDiff > 0)
        {
            tokens.add(Token(TokenType.CLOSEBRACKET, getString(R.string.closeBracket)))
            calculationView.append(getString(R.string.closeBracket))
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
            throw Exception(getString(R.string.divideByZeroMessage))
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