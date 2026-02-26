package com.neoswap

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSource = findViewById<Button>(R.id.btnSource)
        val btnTarget = findViewById<Button>(R.id.btnTarget)
        val btnSwap = findViewById<Button>(R.id.btnSwap)

        btnSource.setOnClickListener {
            Toast.makeText(this, "Source Photo Selected", Toast.LENGTH_SHORT).show()
        }

        btnTarget.setOnClickListener {
            Toast.makeText(this, "Target Photo Selected", Toast.LENGTH_SHORT).show()
        }

        btnSwap.setOnClickListener {
            Toast.makeText(this, "Swapping Faces...", Toast.LENGTH_LONG).show()
        }
    }
}
