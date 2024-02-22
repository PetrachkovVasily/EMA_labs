package com.example.myapplication

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children


class MainActivity : AppCompatActivity(), View.OnClickListener
{

    private lateinit var calculationView : TextView
    private lateinit var resultView : TextView


    private var tokens : MutableList<Token> = mutableListOf()
    private var bracketsCountDiff: Int = 0
    private var calculationException : Exception? = null

    private lateinit var calculator : Calculator



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setEventListeners()

        calculationView = findViewById(R.id.inputOperations)
        calculationView.movementMethod = ScrollingMovementMethod()
        resultView = findViewById(R.id.inputSolution)
        resultView.movementMethod = ScrollingMovementMethod()

        if(savedInstanceState?.isEmpty == false)
        {
            restoreData(savedInstanceState)
        }

        calculator = Calculator()
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
        if(digit == getString(R.string.point))
        {
            if(!isPointPermitted()) return
        }
        else
        {
            if(!isDigitPermitted()) return
        }

        if(tokens.isEmpty() || tokens.last().type != TokenType.NUMBER)
        {
            tokens.add( Token(TokenType.NUMBER, ""))
        }

        tokens.last().value += digit
        calculationView.append(digit)

        try
        {
            resultView.text = calculator.calculate(getValidExpression())
            calculationException = null
        }
        catch (ex : Exception)
        {
            resultView.text = ex.message
            calculationException = ex
        }
    }

    private fun isDigitPermitted() : Boolean
    {

        if(tokens.isEmpty()) return true

        when (tokens.last().type) {
            TokenType.CLOSE_BRACKET -> return false
            TokenType.OPEN_BRACKET, TokenType.OPERATION -> return true

            else -> {}
        }

        var digitsBeforePoint = 0
        var digitsAfterPoint = 0
        var pointFaced = false

        for (symbol in tokens.last().value)
        {
            if(symbol.toString() == getString(R.string.point))
            {
                pointFaced = true
                continue
            }

            if(pointFaced)
            {
                digitsAfterPoint++
            }
            else
            {
                digitsBeforePoint++
            }
        }

        return if(pointFaced) digitsAfterPoint < 8
        else digitsBeforePoint < 6
    }

    private fun inputAction(opButton: Button)
    {
        if( tokens.isEmpty() || tokens.last().type == TokenType.OPEN_BRACKET) return

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

        if(tokens.last().type == TokenType.OPEN_BRACKET) bracketsCountDiff--
        else if(tokens.last().type == TokenType.CLOSE_BRACKET) bracketsCountDiff++

        tokens.last().value = currToken.substring(0, currToken.lastIndex)

        if (tokens.last().value.isBlank())
        {
            tokens.removeLast()
        }

        calculationView.text = calculationView.text.substring(0, calculationView.text.lastIndex)

        try
        {
            resultView.text = calculator.calculate(getValidExpression())
            calculationException = null
        }
        catch (ex : Exception)
        {
            resultView.text = ""
            calculationException = ex
        }
    }

    private fun deleteAll()
    {
        calculationView.text = ""
        resultView.text = ""
        bracketsCountDiff = 0
        tokens.clear()
    }

    private fun getValidExpression(): MutableList<Token>
    {
        val validExpression = tokens.toMutableList()

        var bracketDiff = bracketsCountDiff

        while (validExpression.last().type == TokenType.OPERATION ||
            validExpression.last().type == TokenType.OPEN_BRACKET)
        {
            if(validExpression.last().type == TokenType.OPEN_BRACKET) bracketDiff--
            validExpression.removeLast()
        }
        for (i in 1..bracketDiff)
        {
            validExpression.add(Token(TokenType.CLOSE_BRACKET, getString(R.string.closeBracket)))
        }

        return validExpression
    }

    private fun inputBrackets()
    {
        if (tokens.isEmpty() ||
            (tokens.last().type != TokenType.CLOSE_BRACKET && tokens.last().type != TokenType.NUMBER))
        {
            tokens.add(Token(TokenType.OPEN_BRACKET, getString(R.string.openBracket)))
            calculationView.append(getString(R.string.openBracket))
            bracketsCountDiff++
        }
        else if(bracketsCountDiff > 0)
        {
            tokens.add(Token(TokenType.CLOSE_BRACKET, getString(R.string.closeBracket)))
            calculationView.append(getString(R.string.closeBracket))
            bracketsCountDiff--
        }
    }


    private fun execute()
    {
        lateinit var res : String
        try
        {
            res = calculator.calculate(getValidExpression())
            calculationException = null
        }
        catch (ex : Exception)
        {
            calculationException = ex
        }

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