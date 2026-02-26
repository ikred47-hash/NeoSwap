package com.neoswap

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Registers the photo picker for the Source face
    private val pickSourceMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            Toast.makeText(this, "Source Selected: $uri", Toast.LENGTH_SHORT).show()
        }
    }

    // Registers the photo picker for the Target image
    private val pickTargetMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            Toast.makeText(this, "Target Selected: $uri", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSource = findViewById<Button>(R.id.btnSource)
        val btnTarget = findViewById<Button>(R.id.btnTarget)
        val btnSwap = findViewById<Button>(R.id.btnSwap)

        btnSource.setOnClickListener {
            // Launches picker for images only
            pickSourceMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        btnTarget.setOnClickListener {
            pickTargetMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        btnSwap.setOnClickListener {
            Toast.makeText(this, "Swap Logic Coming Soon!", Toast.LENGTH_LONG).show()
        }
    }
}
