package com.example.toxic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent

class SetPathActivity : AppCompatActivity() {

    private var inputs : MutableMap<String, EditText> = mutableMapOf()

    private var from : Waypoint? = null
    private var to : Waypoint? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_path_activity)

        Log.d(getString(R.string.testLogs), "${getString(R.string.onCreate)} ${getString(R.string.`in`)} ${getString(R.string.SetPathActivity)}");

        val button: Button = findViewById(R.id.confirm_path_btn)
        button.setOnClickListener { confirmAddress() }

        setInputs()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        super.onStart()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onStart)} ${getString(R.string.`in`)} ${getString(R.string.SetPathActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onResume)} ${getString(R.string.`in`)} ${getString(R.string.SetPathActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onPause)} ${getString(R.string.`in`)} ${getString(R.string.SetPathActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onStop)} ${getString(R.string.`in`)} ${getString(R.string.SetPathActivity)}");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        super.onDestroy()
        Log.d(getString(R.string.testLogs), "${getString(R.string.onDestroy)} ${getString(R.string.`in`)} ${getString(R.string.SetPathActivity)}");
    }

    private fun setInputs()
    {
        inputs.putIfAbsent(getString(R.string.from_street), findViewById(R.id.where_from_street))
        inputs.putIfAbsent(getString(R.string.from_house), findViewById(R.id.where_from_house))
        inputs.putIfAbsent(getString(R.string.from_flat), findViewById(R.id.where_from_flat))

        inputs.putIfAbsent(getString(R.string.to_street), findViewById(R.id.where_to_street))
        inputs.putIfAbsent(getString(R.string.to_house), findViewById(R.id.where_to_house))
        inputs.putIfAbsent(getString(R.string.to_flat), findViewById(R.id.where_to_flat))

    }

    private fun confirmAddress()
    {
        val intent = Intent(this@SetPathActivity, CallTaxiActivity::class.java)

        if(!checkAndSetWaypoints())
        {
            Toast.makeText(this, getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
            Log.d(getString(R.string.testLogs), "${getString(R.string.confirmAddress)} ${getString(R.string.CLICK)} ${getString(R.string.failed)}");
            return
        }

        intent.putExtra(getString(R.string.from), from)
        intent.putExtra(getString(R.string.to), to)

        setResult(0, intent)

        Log.d(getString(R.string.testLogs), "${getString(R.string.confirmAddress)} ${getString(R.string.CLICK)} ${getString(R.string.succeeded)}");

        finish()
    }

    private fun checkAndSetWaypoints() : Boolean
    {
        for(kvp in inputs)
        {
            if(kvp.value.text.toString().trim().isEmpty()) return false
        }

        from = Waypoint(
            inputs[getString(R.string.from_street)]!!.text.toString(),
            inputs[getString(R.string.from_house)]!!.text.toString(),
            inputs[getString(R.string.from_flat)]!!.text.toString())

        to = Waypoint(
            inputs[getString(R.string.to_street)]!!.text.toString(),
            inputs[getString(R.string.to_house)]!!.text.toString(),
            inputs[getString(R.string.to_flat)]!!.text.toString())

        return true
    }


}