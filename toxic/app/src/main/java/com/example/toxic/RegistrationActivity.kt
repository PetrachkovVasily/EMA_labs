package com.example.toxic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_activity)
        val button: Button = findViewById(R.id.reg_btn)
        button.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, CallTaxiActivity::class.java)
            startActivity(intent)
        }
    }
}