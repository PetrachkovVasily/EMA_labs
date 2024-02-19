package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var calculationView : TextView
    private lateinit var resultView : TextView



    private var currNumIndex : Int = 0
    private var currOpIndex : Int = -1
    private var isNumber : Boolean = true

    private var numbers : MutableList<StringBuilder> = mutableListOf( StringBuilder())
    private var operations : MutableList<String> = mutableListOf()




    private var actionPossibility = true
    private var decimalPossibility = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setEventListeners()

        calculationView = findViewById<TextView>(R.id.inputOperations)
        resultView = findViewById<TextView>(R.id.inputSolution)
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
            getString(R.string.opBtnTag) -> inputActions(v as Button)
            getString(R.string.equalBtnTag) -> getResult(v as Button)
            getString(R.string.clearBtnTag) -> deleteAll(v as Button)
            getString(R.string.backspaceBtnTag) -> deleteLast(v as Button)
        }

    }

    private fun inputDigit(digitButton: Button)
    {
        if(!isNumber)
        {
            currNumIndex++
            numbers.add( StringBuilder() )
            isNumber = true
        }

        val digit = digitButton.text
        if(digit == "." && !isPointPermitted()) return

        numbers[currNumIndex].append(digit)
        calculationView.append(digit)
    }

    private fun inputActions(opButton: Button)
    {
        if( isNumber && !canWriteOperation() ) return

        if( isNumber )
        {
            currOpIndex++
            operations.add( String() )
            isNumber = false
        }

        val operation = opButton.text.toString()

        if(operations[currOpIndex].isNotBlank())
        {
            val currViewText = calculationView.text
            calculationView.text = currViewText.substring(0, currViewText.length - 1)
        }

        calculationView.append(operation)
        operations[currOpIndex] = operation
    }

    private fun canWriteOperation() : Boolean
    {
        return numbers.isNotEmpty() && numbers[currNumIndex].isNotBlank()
    }

    private fun isPointPermitted() : Boolean
    {
        return !(numbers[currNumIndex].isNullOrBlank() || numbers[currNumIndex].contains('.'))
    }

    private fun deleteLast(view: Button)
    {
        if(calculationView.text.isNullOrBlank()) return

        if(isNumber)
        {
            numbers[currNumIndex].deleteAt(numbers[currNumIndex].length - 1)
            if(numbers[currNumIndex].isNullOrBlank() && operations.isNotEmpty())
            {
                isNumber = false
                numbers.removeAt(currNumIndex--)
            }
        }
        else
        {
            operations.removeAt(currOpIndex--)
            isNumber = true
        }

        calculationView.text = calculationView.text.substring(0, calculationView.text.length - 1)
    }


    private fun deleteAll(view: Button) {
        calculationView.text = ""
        operations.clear()
        numbers = mutableListOf( StringBuilder() )
        resultView.text = ""
    }


    private fun getResult(view: Button) {
        var digitalList = digitReading(view)
        var operationList = operationReading(view)
        //var newResult = calculateFirst(digitalList, operationList)
        resultView.text = calculateFirst(digitalList, operationList).joinToString("")
    }

    private fun calculateFirst(digitalList: MutableList<String>, operationList: MutableList<String>): MutableList<String> {
        var newList: MutableList<String>
        var restart = 0
        while(operationList.contains("*") || operationList.contains("/")) {
            var index = 0
            for(operation in operationList) {
                var newNumber: Float
                var currentNumber = digitalList[index].toFloat()
                var nextNumber = digitalList[index+1].toFloat()
                when(operation) {
                    "*" -> {
                        newNumber = currentNumber*nextNumber
                        digitalList[index]=newNumber.toString()
                        digitalList.removeAt(index+1)
                        operationList.removeAt(index)
                        break
                    }
                    "/" -> {
                        newNumber = currentNumber/nextNumber
                        digitalList[index]=newNumber.toString()
                        digitalList.removeAt(index+1)
                        operationList.removeAt(index)
                        break
                    }
                }
                index += 1
            }
        }
        var index = 0
        for(operation in operationList) {
            var newNumber: Float
            var currentNumber = digitalList[index].toFloat()
            var nextNumber = digitalList[index+1].toFloat()
            when(operation) {
                "+" -> {
                    newNumber = currentNumber+nextNumber
                    digitalList[index]=newNumber.toString()
                    digitalList.removeAt(index+1)
                    operationList.removeAt(index)
                    break
                }
                "-" -> {
                    newNumber = currentNumber-nextNumber
                    digitalList[index]=newNumber.toString()
                    digitalList.removeAt(index+1)
                    operationList.removeAt(index)
                    break
                }
            }
            index += 1
        }
        return digitalList
    }

    private fun digitReading(view: View): MutableList<String> {
        var digitalList: MutableList<String> = this.calculationView.text.split("+", "-", "*", "/").toMutableList()

        return digitalList
    }

    private fun operationReading(view: View): MutableList<String> {
        var operationList: MutableList<String> = this.calculationView.text.split("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".").toMutableList()
        operationList.removeAt(0)
        operationList.removeAt(operationList.size-1)
        return operationList
    }

}