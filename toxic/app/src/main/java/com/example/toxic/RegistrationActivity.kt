package com.example.toxic

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonWriter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.Serializable

class RegistrationActivity : AppCompatActivity() {

    private lateinit var phoneInput : EditText
    private lateinit var fNameInput : EditText
    private lateinit var lNameInput : EditText
    private lateinit var continueBtn : Button


    private lateinit var phoneRegex : Regex

    private var userData : UserData? = null

    private lateinit var mSettings : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_activity)

        phoneRegex = Regex(getString(R.string.phone_regex))

        continueBtn = findViewById(R.id.reg_btn)
        continueBtn.setOnClickListener { toCallTaxiActivity() }

        setInputs()

        mSettings = getSharedPreferences("userData", MODE_PRIVATE)

        getUserData()
    }

    private fun toCallTaxiActivity()
    {
        val intent = Intent(this@RegistrationActivity, CallTaxiActivity::class.java)

        if(!checkAndSetUserData())
        {
            Toast.makeText(this, "Some values incorrect", Toast.LENGTH_SHORT).show()
            return
        }


        intent.putExtra("userInfo", userData)

        startActivity(intent)
    }

    private fun setInputs()
    {
        phoneInput = findViewById(R.id.phone)
        fNameInput = findViewById(R.id.name)
        lNameInput = findViewById(R.id.last_name)
    }

    private fun checkAndSetUserData() : Boolean
    {
        val phone = phoneInput.text.toString()

        if(!phoneRegex.matches(phone)) return false

        if(fNameInput.text.trim().isEmpty() || lNameInput.text.trim().isEmpty()) return false

        setUserData()
        return true
    }

    private fun getUserData()
    {
        val savedPhone = mSettings.getString("phone", null)
        val savedFName = mSettings.getString("fName", null)
        val savedLName = mSettings.getString("lName", null)

        if(savedPhone == null || savedFName == null || savedLName == null) return

        phoneInput.setText(savedPhone)
        fNameInput.setText(savedFName)
        lNameInput.setText(savedLName)

        continueBtn.text = "Войти"
    }

    private fun setUserData()
    {
        userData = UserData(phoneInput.text.toString(),
            fNameInput.text.toString(),
            lNameInput.text.toString())

        var editor = mSettings.edit()
        editor.putString("phone", userData!!.phone)
        editor.putString("fName", userData!!.fName)
        editor.putString("lName", userData!!.lName)
        editor.commit()

        Toast.makeText(this, "User data saved", Toast.LENGTH_SHORT).show()
    }
}