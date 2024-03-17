package com.example.toxic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SetPathActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_path_activity)
        val button: Button = findViewById(R.id.confirm_path_btn)
        button.setOnClickListener {
            val intent = Intent(this@SetPathActivity, CallTaxiActivity::class.java)
            startActivity(intent)
        }
    }
}