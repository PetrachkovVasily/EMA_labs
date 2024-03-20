package com.example.toxic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SetPathActivity : AppCompatActivity() {

    private var inputs : MutableMap<String, EditText> = mutableMapOf()

    private var from : Waypoint? = null
    private var to : Waypoint? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_path_activity)


        val button: Button = findViewById(R.id.confirm_path_btn)
        button.setOnClickListener { confirmAddress() }

        setInputs()
    }

    private fun setInputs()
    {
        inputs.putIfAbsent("from_street", findViewById(R.id.where_from_street))
        inputs.putIfAbsent("from_house", findViewById(R.id.where_from_house))
        inputs.putIfAbsent("from_flat", findViewById(R.id.where_from_flat))

        inputs.putIfAbsent("to_street", findViewById(R.id.where_to_street))
        inputs.putIfAbsent("to_house", findViewById(R.id.where_to_house))
        inputs.putIfAbsent("to_flat", findViewById(R.id.where_to_flat))

    }

    private fun confirmAddress()
    {
        val intent = Intent(this@SetPathActivity, CallTaxiActivity::class.java)

        if(!checkAndSetWaypoints())
        {
            Toast.makeText(this, "Some fields empty!", Toast.LENGTH_LONG).show()
            return
        }

        intent.putExtra("from", from)
        intent.putExtra("to", to)

        setResult(0, intent)
        finish()
    }

    private fun checkAndSetWaypoints() : Boolean
    {
        for(kvp in inputs)
        {
            if(kvp.value.text.toString().trim().isEmpty()) return false
        }

        from = Waypoint(
            inputs["from_street"]!!.text.toString(),
            inputs["from_house"]!!.text.toString(),
            inputs["from_flat"]!!.text.toString())

        to = Waypoint(
            inputs["to_street"]!!.text.toString(),
            inputs["to_house"]!!.text.toString(),
            inputs["to_flat"]!!.text.toString())

        return true
    }


}