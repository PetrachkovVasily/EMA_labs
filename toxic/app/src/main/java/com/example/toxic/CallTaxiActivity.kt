package com.example.toxic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

@Suppress("DEPRECATION")
class CallTaxiActivity : AppCompatActivity() {


    private lateinit var startForResult : ActivityResultLauncher<Intent>

    private lateinit var wayInfo : TextView
    private lateinit var callBtn : Button

    private var from : Waypoint? = null
    private var to : Waypoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_taxi_activity)

        Log.d("testLogs", "onCreate in CallTaxiActivity")

        //Getting userData from intent
        val currIntent = intent
        val userData = currIntent.getSerializableExtra("userInfo") as UserData

        findViewById<TextView>(R.id.name_last_name).text = userData.fName + " " + userData.lName
        findViewById<TextView>(R.id.phone_number).text = userData.phone

        wayInfo = findViewById(R.id.path_info)

        val toSetPath: Button = findViewById(R.id.set_path_btn)
        toSetPath.setOnClickListener { toCallTaxiActivity() }

        val callTaxi: Button = findViewById(R.id.call_taxi_btn)
        callTaxi.setOnClickListener { callTaxi() }

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            setResult(it)
        }

    }

    private fun setResult(result : ActivityResult) {
        from = result.data?.getSerializableExtra("from") as Waypoint
        to = result.data?.getSerializableExtra("to") as Waypoint

        if (from == null && to == null)
        {
            wayInfo.text = ""
            callBtn.visibility = View.INVISIBLE
        }
        else
        {
            callBtn.visibility = View.VISIBLE
            wayInfo.text = "Откуда: $from\nКуда: $to"
        }

    }

    private fun toCallTaxiActivity()
    {
        val intent = Intent(this@CallTaxiActivity, SetPathActivity::class.java)

        startForResult.launch(intent)

        Log.d("testLogs", "toSetPath CLICK")
    }

    private fun callTaxi()
    {
        val text = "Wait for taxi. Good luck!"

        Log.d("testLogs", "callTaxi CLICK succeeded")

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}