package com.example.toxic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CallTaxiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_taxi_activity)
        val button: Button = findViewById(R.id.set_path_btn)
        button.setOnClickListener {
            val intent = Intent(this@CallTaxiActivity, SetPathActivity::class.java)
            startActivity(intent)
        }
    }
}