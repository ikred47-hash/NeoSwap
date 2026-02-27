package com.neoswap

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imgSource: ImageView
    private lateinit var imgTarget: ImageView
    private lateinit var progressBar: ProgressBar

    private val pickSourceMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imgSource.setImageURI(uri)
        }
    }

    private val pickTargetMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imgTarget.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgSource = findViewById(R.id.imgSource)
        imgTarget = findViewById(R.id.imgTarget)
        progressBar = findViewById(R.id.progressBar)
        
        val btnSource = findViewById<Button>(R.id.btnSource)
        val btnTarget = findViewById<Button>(R.id.btnTarget)
        val btnSwap = findViewById<Button>(R.id.btnSwap)

        btnSource.setOnClickListener {
            pickSourceMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        btnTarget.setOnClickListener {
            pickTargetMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        btnSwap.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            Toast.makeText(this, "Processing Swap...", Toast.LENGTH_SHORT).show()
            // Next step will be adding the C++ engine here
        }
    }
}
