package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var inputOperations : TextView
    lateinit var inputSolution : TextView

    var actionPossibility = true
    var decimalPossibility = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputOperations = findViewById<TextView>(R.id.inputOperations)
        inputSolution = findViewById<TextView>(R.id.inputSolution)
    }


    fun inputNumbers(view: View) {
        if(view is Button) {
            if(view.text == ".") {
                if(decimalPossibility) {
                    inputOperations.append(view.text)
                }
                decimalPossibility = false
            } else {
                inputOperations.append(view.text)
            }
            actionPossibility = true
        }
    }

    fun inputActions(view: View) {
        if(view is Button && actionPossibility) {
            inputOperations.append(view.text)
            actionPossibility = false
            decimalPossibility = true
        }
    }

    fun deleteAll(view: View) {
        inputOperations.text = ""
        inputSolution.text = ""
    }

    fun deleteLast(view: View) {
        inputOperations.text = inputOperations.text.substring(0, inputOperations.text.length - 1)
    }

    fun getResult(view: View) {
        var digitalList = digitReading(view)
        var operationList = operationReading(view)
        //var newResult = calculateFirst(digitalList, operationList)
        inputSolution.text = calculateFirst(digitalList, operationList).joinToString("")
    }

    fun calculateFirst(digitalList: MutableList<String>, operationList: MutableList<String>): MutableList<String> {
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

    fun digitReading(view: View): MutableList<String> {
        var digitalList: MutableList<String> = inputOperations.text.split("+", "-", "*", "/").toMutableList()

        return digitalList
    }

    fun operationReading(view: View): MutableList<String> {
        var operationList: MutableList<String> = inputOperations.text.split("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".").toMutableList()
        operationList.removeAt(0)
        operationList.removeAt(operationList.size-1)
        return operationList
    }
}