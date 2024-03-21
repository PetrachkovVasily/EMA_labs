package com.example.toxic

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonWriter
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
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

        Log.d(getString(R.string.testLogs), "${getString(R.string.onCreate)} ${getString(R.string.`in`)} ${getString(R.string.RegistrationActivity)}");

        phoneRegex = Regex(getString(R.string.phone_regex))

        continueBtn = findViewById(R.id.reg_btn)
        continueBtn.setOnClickListener { toCallTaxiActivity() }

        setInputs()

        mSettings = getSharedPreferences(getString(R.string.userInfo), MODE_PRIVATE)

        getUserData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        super.onStart()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onStart)} ${getString(R.string.`in`)} ${getString(R.string.RegistrationActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onResume)} ${getString(R.string.`in`)} ${getString(R.string.RegistrationActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onPause)} ${getString(R.string.`in`)} ${getString(R.string.RegistrationActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onStop)} ${getString(R.string.`in`)} ${getString(R.string.RegistrationActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        super.onDestroy()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onDestroy)} ${getString(R.string.`in`)} ${getString(R.string.RegistrationActivity)}");
    }

    private fun toCallTaxiActivity()
    {
        val intent = Intent(this@RegistrationActivity, CallTaxiActivity::class.java)

        if(!checkAndSetUserData())
        {
            Toast.makeText(this, getString(R.string.incorrectValues), Toast.LENGTH_SHORT).show()
            return
        }


        intent.putExtra(getString(R.string.userInfo), userData)

        startActivity(intent)

        Log.d(getString(R.string.testLogs), "${getString(R.string.continueBtn)} ${getString(R.string.CLICK)}");
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

        Log.d(getString(R.string.testLogs), "${getString(R.string.userInfo)} ${getString(R.string.check)}");
        return true
    }

    private fun getUserData()
    {
        val savedPhone = mSettings.getString(getString(R.string.phone), null)
        val savedFName = mSettings.getString(getString(R.string.fName), null)
        val savedLName = mSettings.getString(getString(R.string.lName), null)

        if(savedPhone == null || savedFName == null || savedLName == null) return

        phoneInput.setText(savedPhone)
        fNameInput.setText(savedFName)
        lNameInput.setText(savedLName)

        continueBtn.text = getString(R.string.enter)

        Log.d(getString(R.string.testLogs), "${getString(R.string.userInfo)} ${getString(R.string.get)}");
    }

    private fun setUserData()
    {
        userData = UserData(phoneInput.text.toString(),
            fNameInput.text.toString(),
            lNameInput.text.toString())

        var editor = mSettings.edit()
        editor.putString(getString(R.string.phone), userData!!.phone)
        editor.putString(getString(R.string.fName), userData!!.fName)
        editor.putString(getString(R.string.lName), userData!!.lName)
        editor.commit()

        Toast.makeText(this, getString(R.string.saveUserData), Toast.LENGTH_SHORT).show()
    }
}