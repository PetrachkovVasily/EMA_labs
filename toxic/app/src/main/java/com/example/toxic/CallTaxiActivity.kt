package com.example.toxic

import android.annotation.SuppressLint
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent

@Suppress("DEPRECATION")
class CallTaxiActivity : AppCompatActivity() {


    private lateinit var startForResult : ActivityResultLauncher<Intent>

    private lateinit var wayInfo : TextView
    private lateinit var callBtn : Button

    private var from : Waypoint? = null
    private var to : Waypoint? = null


    @SuppressLint("CutPasteId", "SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_taxi_activity)

        Log.d(getString(R.string.testLogs), "${getString(R.string.onCreate)} ${getString(R.string.`in`)} ${getString(R.string.CallTaxiActivity)}");


        //Getting userData from intent
        val currIntent = intent
        val userData = currIntent.getSerializableExtra(getString(R.string.userInfo)) as UserData

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

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        super.onStart()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onStart)} ${getString(R.string.`in`)} ${getString(R.string.CallTaxiActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onResume)} ${getString(R.string.`in`)} ${getString(R.string.CallTaxiActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onPause)} ${getString(R.string.`in`)} ${getString(R.string.CallTaxiActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onStop)} ${getString(R.string.`in`)} ${getString(R.string.CallTaxiActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        super.onDestroy()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onDestroy)} ${getString(R.string.`in`)} ${getString(R.string.CallTaxiActivity)}");
    }

    private fun setResult(result : ActivityResult) {
        from = result.data?.getSerializableExtra(getString(R.string.from)) as Waypoint
        to = result.data?.getSerializableExtra(getString(R.string.to)) as Waypoint

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

        Log.d(getString(R.string.testLogs), "${getString(R.string.toSetPath)} ${getString(R.string.CLICK)}");
    }

    private fun callTaxi()
    {

        val text = "Wait for taxi. Good luck!"

        Log.d("testLogs", "callTaxi CLICK succeeded")

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}